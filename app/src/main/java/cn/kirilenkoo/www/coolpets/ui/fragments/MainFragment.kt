package cn.kirilenkoo.www.coolpets.ui.fragments


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.databinding.FragmentMainBinding
import cn.kirilenkoo.www.coolpets.di.Injectable


class MainFragment : Fragment(),Injectable {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<FragmentMainBinding>(
                inflater,
                R.layout.fragment_main,
                container,
                false
        )
        return dataBinding.root
    }


}
