package cn.kirilenkoo.www.coolpets.viewmodel

import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.repository.PostRepository
import cn.kirilenkoo.www.coolpets.util.ImgUploadController
import javax.inject.Inject

class PostEditViewModel @Inject constructor(val postRepository: PostRepository, val imgUploadController: ImgUploadController): ViewModel(){
    val tmpPost:MutableLiveData<PostWithContents> = MutableLiveData()
    fun bindImageView(imageView: ImageView, localPath: String?, viewLifecycleOwner: LifecycleOwner) {
        imgUploadController.bindView(imageView, localPath, viewLifecycleOwner)
    }
    fun uploadImg(path:String?){
        if(path == null) return
        imgUploadController.uploadImg(path)
    }


}