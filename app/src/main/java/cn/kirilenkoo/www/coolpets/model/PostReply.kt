package cn.kirilenkoo.www.coolpets.model

import android.arch.persistence.room.Entity

@Entity(primaryKeys = ["replyId"])
class PostReply(val replyId:Int, val postId: Int, val posterId: Int, val content: String, val imgUrl: String?){

}