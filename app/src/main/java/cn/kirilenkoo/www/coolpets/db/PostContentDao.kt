package cn.kirilenkoo.www.coolpets.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import cn.kirilenkoo.www.coolpets.model.PostContent

@Dao
interface PostContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(postContent: PostContent)
}