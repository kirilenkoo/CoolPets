package cn.kirilenkoo.www.coolpets.model

import androidx.room.Entity

@Entity(primaryKeys = ["replyId"])
class PostReply(val replyId:String, val postId: String, val posterId: Int, val content: String, val imgUrl: String?){

}