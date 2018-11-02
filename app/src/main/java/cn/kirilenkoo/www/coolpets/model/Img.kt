package cn.kirilenkoo.www.coolpets.model

import androidx.room.Entity

@Entity(primaryKeys = ["path"])
class Img (val path:String, val url: String){
}