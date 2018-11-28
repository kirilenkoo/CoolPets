package cn.kirilenkoo.www.coolpets.ui.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.auth.AuthController
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.binding.FragmentDataBindingComponent
import cn.kirilenkoo.www.coolpets.databinding.FragmentSignInBinding
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.model.Account
import cn.kirilenkoo.www.coolpets.util.ErrorHandler
import cn.kirilenkoo.www.coolpets.util.autoCleared
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.LogInCallback
import timber.log.Timber
import javax.inject.Inject
import com.avos.avoscloud.RequestMobileCodeCallback



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SignInFragment : BaseFragment(), Injectable {
    @Inject
    lateinit var auth:AuthController
    var binding by autoCleared<FragmentSignInBinding>()
    var dataBindingComponent = FragmentDataBindingComponent(this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentSignInBinding>(
                inflater,
                R.layout.fragment_sign_in,
                container,
                false,
                dataBindingComponent
        )
        binding = dataBinding
        binding.buttonSendCode.setOnClickListener {
            AVUser.requestLoginSmsCodeInBackground("18600044200", object : RequestMobileCodeCallback() {
                override fun done(e: AVException) {

                }
            })
        }
        binding.buttonSignIn.setOnClickListener {
            AVUser.signUpOrLoginByMobilePhoneInBackground("18600044200","049950",
                    object : LogInCallback<AVUser>(){
                        override fun done(p0: AVUser?, p1: AVException?) {
                            if(p1 != null){
                                Timber.d(p1.message)
                                ErrorHandler().handleHttpError(p1)
                            }else{
                                if(p0 != null){
                                    Timber.d(p0.objectId)
                                    auth.login(Account(p0.objectId,p0.mobilePhoneNumber))
                                    if(AVUser.getCurrentUser().get("avatar") == null){
                                        AVUser.getCurrentUser().apply { username = generatePHusername(username) }.saveInBackground()
                                        goSetNameAva()
                                    }

                                }
                            }
                        }

                    })
        }
        return dataBinding.root
    }

    fun generatePHusername(s: String):String{
        return s.replaceRange(s.length-5 until s.length, "****")
    }

    fun goSetNameAva(){
        findNavController().navigate(R.id.action_signInFragment_to_signInfoFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}
