package cn.kirilenkoo.www.coolpets.auth

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseApplication
import cn.kirilenkoo.www.coolpets.model.Account
import cn.kirilenkoo.www.coolpets.ui.activities.SignInActivity
import cn.kirilenkoo.www.coolpets.util.AbsentLiveData
import com.avos.avoscloud.AVUser
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthController {
    private var accountLiveData: MutableLiveData<Account> = MutableLiveData()
    @Inject constructor(){
        if(AVUser.getCurrentUser()!=null){
            accountLiveData.value = Account(AVUser.getCurrentUser().objectId,AVUser.getCurrentUser().username)
        }
    }
    fun getAccountData(): LiveData<Account> = accountLiveData

    fun login(account: Account){
        accountLiveData.value = account
    }
    fun logOut(){
        accountLiveData.value = null
    }
    fun isLogin():Boolean = accountLiveData.value != null

    fun showLogin() {
        val i = Intent(BaseApplication.applicationContext(), SignInActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        BaseApplication.applicationContext().startActivity(i)
    }


}