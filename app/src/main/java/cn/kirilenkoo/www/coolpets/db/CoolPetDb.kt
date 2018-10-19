package cn.kirilenkoo.www.coolpets.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.model.PostContent
import cn.kirilenkoo.www.coolpets.model.PostReply

@Database(
        entities = [
            Post::class,
            PostContent::class,
            PostReply::class],
        version = 2,
        exportSchema = true
)
abstract class CoolPetDb : RoomDatabase() {
    abstract fun postDao() : PostDao
    abstract fun postWithContentsDao() : PostWithContentsDao
    abstract fun postContentDao() : PostContentDao
    abstract fun postReplyDao() : PostReplyDao
}