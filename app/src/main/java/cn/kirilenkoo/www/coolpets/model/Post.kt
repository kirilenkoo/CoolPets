package cn.kirilenkoo.www.coolpets.model

import android.arch.persistence.room.Entity

/**
 * Created by huangzilong on 2018/3/22.
 */

@Entity(primaryKeys = ["postId"])
data class Post(val postId: String, val title:String) {
}