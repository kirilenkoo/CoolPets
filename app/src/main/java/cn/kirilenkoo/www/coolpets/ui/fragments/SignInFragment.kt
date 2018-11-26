package cn.kirilenkoo.www.coolpets.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.auth.AuthController
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.model.Account
import javax.inject.Inject

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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}
