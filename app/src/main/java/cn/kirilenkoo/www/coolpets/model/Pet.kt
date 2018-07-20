package cn.kirilenkoo.www.coolpets.model

import android.util.Log
import javax.inject.Inject

/**
 * Created by huangzilong on 2018/3/22.
 */
class Pet @Inject constructor(
        var mTag: Tag
) {
    fun call(){
        mTag.showTag()
    }
}