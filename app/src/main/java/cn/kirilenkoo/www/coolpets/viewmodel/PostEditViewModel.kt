package cn.kirilenkoo.www.coolpets.viewmodel

import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.kirilenkoo.www.coolpets.model.PostContent
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.repository.PostRepository
import cn.kirilenkoo.www.coolpets.ui.view.StateImageView
import cn.kirilenkoo.www.coolpets.util.ImgUploadController
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PostEditViewModel @Inject constructor(val postRepository: PostRepository, val imgUploadController: ImgUploadController): ViewModel(){
    private var tmpPost:EditPost = EditPost()

    private fun bindImageView(imageView: ImageView, localPath: String?, viewLifecycleOwner: LifecycleOwner) {
        imgUploadController.bindView(imageView as StateImageView, localPath, viewLifecycleOwner)
    }

    fun addPostCover(imageView: ImageView, localPath: String?, viewLifecycleOwner: LifecycleOwner){
        if(localPath == null) return
        tmpPost.coverPath = localPath
        bindImageView(imageView,localPath,viewLifecycleOwner)
        if(!imgUploadController.hasPathUpdated(localPath)) imgUploadController.uploadImg(localPath)
    }

    fun rebindImageViews(coverImageView: ImageView, contentImageViews: ArrayList<ImageView>, viewLifecycleOwner: LifecycleOwner){
        if(tmpPost.coverPath!=null){
            bindImageView(coverImageView, tmpPost.coverPath, viewLifecycleOwner)
            if(!imgUploadController.hasPathUpdated(tmpPost.coverPath!!)) imgUploadController.uploadImg(tmpPost.coverPath!!)
        }
        for ( i in 0 until tmpPost.contents.size){
            if(tmpPost.contents[i] != null){
                bindImageView(contentImageViews[i], tmpPost.contents[i].url, viewLifecycleOwner)
                if(!imgUploadController.hasPathUpdated(tmpPost.contents[i].url)) imgUploadController.uploadImg(tmpPost.contents[i].url)
            }
        }
    }

    fun addImgContent(contentImage: ImageView, contentPath: String?, viewLifecycleOwner: LifecycleOwner) {
        if(contentPath == null) return
        tmpPost.contents.add(PostContent("","","",contentPath))
        bindImageView(contentImage,contentPath,viewLifecycleOwner)
        if(!imgUploadController.hasPathUpdated(contentPath)) imgUploadController.uploadImg(contentPath)
    }
    fun getTmpPost():EditPost{
        return tmpPost
    }
    fun setTmpPost(editPost: EditPost){
        tmpPost = editPost
    }
}

class EditPost(var postTitle: String? = null, var coverPath: String? = null, val contents: ArrayList<PostContent> = ArrayList()) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.createTypedArrayList(PostContent.CREATOR) as ArrayList<PostContent>
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(postTitle)
        writeString(coverPath)
        writeTypedList(contents)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<EditPost> = object : Parcelable.Creator<EditPost> {
            override fun createFromParcel(source: Parcel): EditPost = EditPost(source)
            override fun newArray(size: Int): Array<EditPost?> = arrayOfNulls(size)
        }
    }

}