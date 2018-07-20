package cn.kirilenkoo.www.coolpets.model

import android.util.Log
import javax.inject.Inject

/**
 * Created by huangzilong on 2018/3/22.
 */
class Tag @Inject constructor(){
    constructor(name: String) : this(){

    }
    fun showTag(){
        Log.d("tag", "this is a tag call")
    }
}