package cn.kirilenkoo.www.coolpets.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import cn.kirilenkoo.www.coolpets.model.PostWithContents

@Dao
interface PostWithContentsDao {
    @Query("SELECT * FROM post")
    fun findAllWithContent(): LiveData<List<PostWithContents>>
}