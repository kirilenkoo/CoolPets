package cn.kirilenkoo.www.coolpets.db

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.kirilenkoo.www.coolpets.model.Img
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.model.PostContent
import cn.kirilenkoo.www.coolpets.model.PostReply

@Database(
        entities = [
            Post::class,
            PostContent::class,
            PostReply::class,
            Img::class],
        version = 8,
        exportSchema = true
)
abstract class CoolPetDb : RoomDatabase() {
    abstract fun postDao() : PostDao
    abstract fun postWithContentsDao() : PostWithContentsDao
    abstract fun postContentDao() : PostContentDao
    abstract fun postReplyDao() : PostReplyDao
    abstract fun imgDao() : ImgDao
}