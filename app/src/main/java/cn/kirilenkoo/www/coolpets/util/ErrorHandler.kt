package cn.kirilenkoo.www.coolpets.util

import android.content.Context
import cn.kirilenkoo.www.coolpets.base.BaseApplication
import cn.kirilenkoo.www.coolpets.ui.fragments.SignInFragment
import com.avos.avoscloud.AVException
import es.dmoral.toasty.Toasty

class ErrorHandler {
    fun handleHttpError(e : AVException) {
        Toasty.error(BaseApplication.applicationContext(), e.message.toString()).show()
    }
}