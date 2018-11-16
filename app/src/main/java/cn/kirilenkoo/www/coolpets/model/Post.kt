package cn.kirilenkoo.www.coolpets.model

import androidx.room.Entity
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by huangzilong on 2018/3/22.
 */

@Entity(primaryKeys = ["postId"])
data class Post(var postId: String, val title: String) : Parcelable {
    var coverUrl: String? = null
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    ){
        coverUrl = source.readString()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(postId)
        writeString(title)
        writeString(coverUrl)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Post> = object : Parcelable.Creator<Post> {
            override fun createFromParcel(source: Parcel): Post = Post(source)
            override fun newArray(size: Int): Array<Post?> = arrayOfNulls(size)
        }
    }
}