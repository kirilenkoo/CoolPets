package cn.kirilenkoo.www.coolpets.util

import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
class ImgUploadController @Inject constructor(){
    @Inject
    lateinit var appExecutors: AppExecutors
    val imgMap = HashMap<String, MutableLiveData<ImgUploadState>>()

    fun bindView(view: ImageView, path: String?, viewLifecycleOwner: LifecycleOwner){
        if(path == null) return
        if(checkIfPathInMap(path)){
            //has uploaded
            rebindViewState(path, view)
        }else{
            val liveData = MutableLiveData<ImgUploadState>()
            imgMap["path"] = liveData
            liveData.value = ImgUploadState(path, "").apply {
                this.wrappedImageView = WeakReference(view)
            }
            liveData.observe(viewLifecycleOwner, Observer {
                when (it.state){
                    UPLOAD_STATE.INIT -> it.wrappedImageView.get()?.visibility = View.INVISIBLE
                    UPLOAD_STATE.LOADING -> Timber.d("${it.progress}")
                    UPLOAD_STATE.SUCCESS -> it.wrappedImageView.get()?.visibility = View.VISIBLE
                }
            })
        }
    }
    fun checkIfPathInMap(path:String?):Boolean{
        if(path == null) return false
        return imgMap.containsKey(path)
    }
    fun rebindViewState(path:String?, view:ImageView){
        when (imgMap[path]?.value?.state){
            UPLOAD_STATE.INIT -> null
            UPLOAD_STATE.LOADING -> Timber.d(imgMap[path]?.value?.progress.toString())
            UPLOAD_STATE.SUCCESS -> null
        }
    }
    fun uploadImg(path:String){
        val liveData: MutableLiveData<ImgUploadState>? = if(imgMap.containsKey(path)){
            imgMap[path]
        }else{
            MutableLiveData()
        }
        val file:AVFile = AVFile.withAbsoluteLocalPath("test.jpg", path)
        file.saveInBackground(object : SaveCallback(){
            override fun done(e : AVException?) {
                if(e == null){
                    //fail
                }else{
                    //success
                }
            }

        }, object : ProgressCallback(){
            override fun done(progress : Int?) {
                // 0~100
            }

        })
    }
}

class ImgUploadState(var localPath:String = "",
                     var url:String = "",
                     val state:UPLOAD_STATE = UPLOAD_STATE.INIT,
                     var progress:Int = 0){
    lateinit var wrappedImageView:WeakReference<ImageView>
}

enum class UPLOAD_STATE{
    INIT,
    LOADING,
    SUCCESS,
    FAIL
}