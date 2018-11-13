package cn.kirilenkoo.www.coolpets.viewmodel

import android.os.Parcel
import android.os.Parcelable
import android.view.View
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
    }

    fun rebindImageViews(coverImageView: ImageView, contentViews: ArrayList<View>, viewLifecycleOwner: LifecycleOwner){
        if(tmpPost.coverPath!=null){
            bindImageView(coverImageView, tmpPost.coverPath, viewLifecycleOwner)
        }
        for ( i in 0 until tmpPost.contents.size){
            if(tmpPost.contents[i].url != null && !tmpPost.contents[i].url.equals("")){
                if(contentViews[i] is ImageView){
                    bindImageView(contentViews[i] as ImageView, tmpPost.contents[i].url, viewLifecycleOwner)
                }

            }
        }
    }

    fun addImgContent(contentImage: ImageView, contentPath: String?, viewLifecycleOwner: LifecycleOwner) {
        if(contentPath == null) return
        tmpPost.contents.add(PostContent("","","",contentPath))
        bindImageView(contentImage,contentPath,viewLifecycleOwner)
    }
    fun addTextContent(contentText: String?){
        tmpPost.contents.add(PostContent("","",contentText?:"",""))
    }
    fun getTmpPost():EditPost{
        return tmpPost
    }
    fun setTmpPost(editPost: EditPost){
        tmpPost = editPost
    }

    fun textChanged(indexOfChild: Int, s: String) {
        tmpPost.contents[indexOfChild].text = s
    }
}

data class EditPost(var postTitle: String? = null, var coverPath: String? = null, val contents: ArrayList<PostContent> = ArrayList()) : Parcelable {
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