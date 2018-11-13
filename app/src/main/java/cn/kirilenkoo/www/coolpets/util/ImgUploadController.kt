package cn.kirilenkoo.www.coolpets.util

import android.widget.ImageView
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import cn.kirilenkoo.www.coolpets.db.ImgDao
import cn.kirilenkoo.www.coolpets.model.Img
import cn.kirilenkoo.www.coolpets.ui.view.StateImageView
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVFile
import com.avos.avoscloud.ProgressCallback
import com.avos.avoscloud.SaveCallback
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImgUploadController @Inject constructor() {
    @Inject
    lateinit var appExecutors: AppExecutors
    @Inject
    lateinit var imgDao: ImgDao

    private val imgMap = HashMap<String, MutableLiveData<ImgUploadState>>()

    fun bindView(view: StateImageView, path: String?, viewLifecycleOwner: LifecycleOwner) {
        if (path == null) return
        if (checkIfPathInMap(path)) {
            rebindViewState(path, view, viewLifecycleOwner)
        } else {
            appExecutors.diskIO().execute {
                val results = imgDao.findUrl(path)
                if (results.isEmpty()) {
                    appExecutors.mainThread().execute {
                        initMapPair(view, path, viewLifecycleOwner)
                    }
                } else {
                    appExecutors.mainThread().execute {
                        bindExistUrl(results[0], view, viewLifecycleOwner)
                    }
                }
            }

        }
    }

    //path 不在cache 在db
    private fun bindExistUrl(img: Img, view: StateImageView, viewLifecycleOwner: LifecycleOwner) {
        //save to cache
        imgMap[img.path] = MutableLiveData<ImgUploadState>().apply {
            value = ImgUploadState(state = UPLOAD_STATE.SUCCESS, url = img.url).apply {
                wrappedImageViews.add(WeakReference(view))
            }
        }.apply {
            bindObserver(this, viewLifecycleOwner)
        }

    }
    //path 不在cache 也不在db
    private fun initMapPair(view: StateImageView, path: String, viewLifecycleOwner: LifecycleOwner) {
        //init cache
        val liveData = MutableLiveData<ImgUploadState>()
        imgMap[path] = liveData
        liveData.value = ImgUploadState().apply {
            this.wrappedImageViews.add(WeakReference(view))
        }
        bindObserver(liveData, viewLifecycleOwner)
        uploadImg(path)
    }
    //path 在cache
    private fun rebindViewState(path: String?, view: StateImageView, viewLifecycleOwner: LifecycleOwner) {
        imgMap[path]?.apply {
            value?.wrappedImageViews?.add(WeakReference(view))
            bindObserver(this,viewLifecycleOwner)
        }

    }

    private fun bindObserver(liveData: MutableLiveData<ImgUploadState>,viewLifecycleOwner: LifecycleOwner){
        liveData.observe(viewLifecycleOwner, Observer {
            for (wrappedImageView in it.wrappedImageViews) {
                if (wrappedImageView.get() != null) {
                    when (it.state) {
                        UPLOAD_STATE.INIT -> wrappedImageView.get()?.setmState(StateImageView.State.INIT)
                        UPLOAD_STATE.LOADING -> {
                            wrappedImageView.get()?.setmState(StateImageView.State.INPROGRESS)
                            wrappedImageView.get()?.setProgress(it.progress)
                        }
                        UPLOAD_STATE.SUCCESS -> {
                            wrappedImageView.get()?.setmState(StateImageView.State.SUCCESS)
                        }
                        UPLOAD_STATE.FAIL -> {
                            wrappedImageView.get()?.setmState(StateImageView.State.FAIL)
                        }
                    }
                }
            }

        })
    }

    private fun checkIfPathInMap(path: String?): Boolean {
        if (path == null) return false
        return imgMap.containsKey(path)
    }

    fun uploadImg(path: String?) {
        if (path == null) return
        val liveData: MutableLiveData<ImgUploadState>? = if (imgMap.containsKey(path)) {
            imgMap[path]
        } else {
            MutableLiveData()
        }
        appExecutors.diskIO().execute {
            val results = imgDao.findUrl(path)
            if (results.isEmpty()) {
                appExecutors.mainThread().execute {
                    val file: AVFile = AVFile.withAbsoluteLocalPath("test.jpg", path)
                    file.saveInBackground(object : SaveCallback() {
                        override fun done(e: AVException?) {
                            if (e == null) {
                                liveData?.value = liveData?.value?.apply {
                                    state = UPLOAD_STATE.SUCCESS
                                    url = file.url
                                }
                                Timber.d(file.url)
                                appExecutors.diskIO().execute {
                                    imgDao.insert(Img(path, file.url))
                                }
                            } else {
                                liveData?.value = liveData?.value.apply {
                                    this?.state = UPLOAD_STATE.FAIL
                                }
                                Timber.d("upload img exception->${e.message}")
                            }

                        }

                    }, object : ProgressCallback() {
                        override fun done(progress: Int?) {
                            liveData?.value = liveData?.value.apply {
                                this?.state = UPLOAD_STATE.LOADING
                                this?.progress = progress ?: 0
                                Timber.d("$progress")
                            }
                        }

                    })
                }
            } else {
                liveData?.postValue(liveData?.value.apply {
                    this?.state = UPLOAD_STATE.SUCCESS
                    this?.url = results[0].url
                })
            }
        }


    }

    fun hasPathUpdated(localPath: String?): Boolean {
        if (localPath == null) return false
        return imgMap.containsKey(localPath)
    }

    fun tradeUrl(path: String?): String? {
        return if (path == null) null
        else {
            if (imgMap.containsKey(path)) {
                imgMap[path]?.value?.url
            } else null
//            else fetchUrlFromDB(path)
        }
    }

}
class ImgUploadState(var state: UPLOAD_STATE = UPLOAD_STATE.INIT,
                     var progress: Int = 0,
                     var url: String? = null,
                     var wrappedImageViews:ArrayList<WeakReference<StateImageView>> = ArrayList())

enum class UPLOAD_STATE {
    INIT,
    LOADING,
    SUCCESS,
    FAIL
}