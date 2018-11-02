package cn.kirilenkoo.www.coolpets.util

import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
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
import org.json.JSONObject
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
            //has uploaded
            rebindViewState(path, view)
        } else {
            val liveData = MutableLiveData<ImgUploadState>()
            imgMap[path] = liveData
            liveData.value = ImgUploadState().apply {
                this.wrappedImageView = WeakReference(view)
            }
            liveData.observe(viewLifecycleOwner, Observer {
                if (it.wrappedImageView.get() != null) {
                    when (it.state) {
                        UPLOAD_STATE.INIT -> it.wrappedImageView.get()?.setmState(StateImageView.State.INIT)
                        UPLOAD_STATE.LOADING -> {
                            it.wrappedImageView.get()?.setmState(StateImageView.State.INPROGRESS)
                            it.wrappedImageView.get()?.setProgress(it.progress)
                        }
                        UPLOAD_STATE.SUCCESS -> {
                            it.wrappedImageView.get()?.setmState(StateImageView.State.SUCCESS)
                        }
                        UPLOAD_STATE.FAIL -> {
                            it.wrappedImageView.get()?.setmState(StateImageView.State.FAIL)
                        }
                    }
                }
            })
        }
    }

    private fun checkIfPathInMap(path: String?): Boolean {
        if (path == null) return false
        return imgMap.containsKey(path)
    }

    private fun rebindViewState(path: String?, view: ImageView) {
        when (imgMap[path]?.value?.state) {
            UPLOAD_STATE.INIT -> null
            UPLOAD_STATE.LOADING -> Timber.d(imgMap[path]?.value?.progress.toString())
            UPLOAD_STATE.SUCCESS -> null
        }
    }

    fun uploadImg(path: String?) {
        if(path == null) return
        val liveData: MutableLiveData<ImgUploadState>? = if (imgMap.containsKey(path)) {
            imgMap[path]
        } else {
            MutableLiveData()
        }
        appExecutors.diskIO().execute {
            if (imgDao.findUrl(path).isEmpty()) {
                appExecutors.networkIO().execute {
                    val file: AVFile = AVFile.withAbsoluteLocalPath("test.jpg", path)
                    file.saveInBackground(object : SaveCallback() {
                        override fun done(e: AVException?) {
                            if (e == null) {
                                liveData?.value = liveData?.value.apply {
                                    this?.state = UPLOAD_STATE.SUCCESS
                                }
                            } else {
                                liveData?.value = liveData?.value.apply {
                                    this?.state = UPLOAD_STATE.FAIL
                                }
                                Timber.d("upload img exception->${e.message}")
                            }
                            Timber.d(file.url)
                            liveData?.value?.apply {
                                url = file.url
                            }
                            appExecutors.diskIO().execute {
                                imgDao.insert(Img(path, file.url))
                            }
                        }

                    }, object : ProgressCallback() {
                        override fun done(progress: Int?) {
                            liveData?.value = liveData?.value.apply {
                                this?.state = UPLOAD_STATE.LOADING
                                this?.progress = progress ?: 0
                            }
                        }

                    })
                }
            } else {
                liveData?.postValue(liveData?.value.apply {
                    this?.state = UPLOAD_STATE.SUCCESS
                })
            }
        }


    }

    fun hasPathUpdated(localPath: String?): Boolean {
        if(localPath == null) return false
        return if (imgMap.containsKey(localPath))
            when (imgMap[localPath]?.value?.state) {
                UPLOAD_STATE.INIT, UPLOAD_STATE.FAIL -> false
                else -> true
            }
        else false
    }

    fun tradeUrl(path: String?): String? {
        return if(path == null) null
        else{
            if(imgMap.containsKey(path)){
                imgMap[path]?.value?.url
            }else null
//            else fetchUrlFromDB(path)
        }
    }

//    private fun fetchUrlFromDB(path: String):String?{
//        appExecutors.diskIO().execute {
//            if(imgDao.(path).isNotEmpty()){
//                appExecutors.mainThread().execute {
//                    return null@execute
//                }
//            }
//        }
//    }
}

class ImgUploadState(var state: UPLOAD_STATE = UPLOAD_STATE.INIT,
                     var progress: Int = 0,
                     var url: String? = null) {
    lateinit var wrappedImageView: WeakReference<StateImageView>
}

enum class UPLOAD_STATE {
    INIT,
    LOADING,
    SUCCESS,
    FAIL
}