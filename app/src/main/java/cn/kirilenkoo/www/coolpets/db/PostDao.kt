package cn.kirilenkoo.www.coolpets.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import cn.kirilenkoo.www.coolpets.model.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)


    @Query("SELECT * FROM post ")
    fun findAll(): LiveData<List<Post>>



}