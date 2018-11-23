package cn.kirilenkoo.www.coolpets.auth

import androidx.navigation.NavController
import androidx.navigation.Navigation
import cn.kirilenkoo.www.coolpets.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthController {

    @Inject constructor(){

    }
    var pendingFun:Unit? = null
    fun executeAuthFun(goEditPost: Unit, level: AUTHLEVEL, navController: NavController){
        val isLogin = false
//        if(!isLogin){
//            showLogin(navController)
//            pendingFun = goEditPost
//        }else{
//            goEditPost.run {  }
//        }
    }

    private fun showLogin(navController: NavController) {
        navController.navigate(R.id.signInFragment)
    }
    fun signIn(){
        pendingFun?.let {
            it.run {  }
        }
    }
}

enum class AUTHLEVEL{ SIGN, ADMIN}