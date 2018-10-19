package cn.kirilenkoo.www.coolpets.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cn.kirilenkoo.www.coolpets.model.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)


    @Query("SELECT * FROM post ")
    fun findAll(): LiveData<List<Post>>



}