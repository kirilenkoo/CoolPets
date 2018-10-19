package cn.kirilenkoo.www.coolpets.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.model.PostReply

@Dao
interface PostReplyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(postReply: PostReply)


    @Query("SELECT * FROM postReply WHERE postId = :postId")
    fun findPostReply(postId: Int): LiveData<List<PostReply>>
}