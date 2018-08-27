package cn.kirilenkoo.www.coolpets.model

import android.util.Log
import javax.inject.Inject

/**
 * Created by huangzilong on 2018/3/22.
 */
data class Tag @Inject constructor (val tagId: Int = 0, val name: String = "", val url: String?){
    fun showTag(){
        Log.d("tag", "this is a tag call")
    }
}