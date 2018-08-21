package cn.kirilenkoo.www.coolpets.model

import android.arch.persistence.room.Entity

@Entity(primaryKeys = ["contentId"])
class PostContent(val contentId: String, val postId: String, val text: String?) {
}