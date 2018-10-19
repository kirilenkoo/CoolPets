package cn.kirilenkoo.www.coolpets.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import cn.kirilenkoo.www.coolpets.model.PostContent

@Dao
interface PostContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(postContent: PostContent)
}