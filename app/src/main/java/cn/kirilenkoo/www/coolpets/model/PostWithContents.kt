package cn.kirilenkoo.www.coolpets.model

import androidx.room.Embedded
import androidx.room.Relation
import android.os.Parcel
import android.os.Parcelable


class PostWithContents() : Parcelable {
    @Embedded
    lateinit var post: Post

    @Relation(parentColumn = "postId", entityColumn = "postId", entity = PostContent::class)
    lateinit var contentList: List<PostContent>

    constructor(source: Parcel) : this(){
        post = source.readParcelable(post.javaClass.classLoader)
        contentList = arrayListOf<PostContent>().apply {
            source.readList(this, PostContent::class.java.classLoader)
        }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        dest.writeParcelable(post, flags)
        dest.writeParcelableArray(contentList.toTypedArray(),flags)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PostWithContents> = object : Parcelable.Creator<PostWithContents> {
            override fun createFromParcel(source: Parcel): PostWithContents = PostWithContents(source)
            override fun newArray(size: Int): Array<PostWithContents?> = arrayOfNulls(size)
        }
    }
}