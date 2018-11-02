package cn.kirilenkoo.www.coolpets.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cn.kirilenkoo.www.coolpets.model.Img
import cn.kirilenkoo.www.coolpets.model.PostReply

@Dao
interface ImgDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(img: Img)

    @Query("SELECT * FROM Img WHERE path = :path")
    fun findUrl(path: String): List<Img>
}