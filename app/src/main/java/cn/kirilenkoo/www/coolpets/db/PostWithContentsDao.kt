package cn.kirilenkoo.www.coolpets.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import cn.kirilenkoo.www.coolpets.model.PostWithContents

@Dao
interface PostWithContentsDao {
    @Query("SELECT * FROM post")
    fun findAllWithContent(): LiveData<List<PostWithContents>>
}