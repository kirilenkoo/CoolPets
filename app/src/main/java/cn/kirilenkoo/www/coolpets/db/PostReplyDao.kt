package cn.kirilenkoo.www.coolpets.db

import androidx.lifecycle.LiveData
import androidx.room.*
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.model.PostReply

@Dao
interface PostReplyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(postReply: PostReply)


    @Query("SELECT * FROM postReply WHERE postId = :postId")
    fun findPostReply(postId: String): LiveData<List<PostReply>>

    @Delete
    fun deletePostReply(postReply: PostReply)
}