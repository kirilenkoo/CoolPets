package cn.kirilenkoo.www.coolpets.model

import androidx.room.Entity
import android.os.Parcel
import android.os.Parcelable

@Entity(primaryKeys = ["contentId"])
class PostContent(val contentId: String, val postId: String, val text: String?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(contentId)
        writeString(postId)
        writeString(text)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PostContent> = object : Parcelable.Creator<PostContent> {
            override fun createFromParcel(source: Parcel): PostContent = PostContent(source)
            override fun newArray(size: Int): Array<PostContent?> = arrayOfNulls(size)
        }
    }
}