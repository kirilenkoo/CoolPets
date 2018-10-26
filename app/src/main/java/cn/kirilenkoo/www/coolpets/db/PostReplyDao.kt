package cn.kirilenkoo.www.coolpets.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.model.PostReply

@Dao
interface PostReplyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(postReply: PostReply)


    @Query("SELECT * FROM postReply WHERE postId = :postId")
    fun findPostReply(postId: String): LiveData<List<PostReply>>
}