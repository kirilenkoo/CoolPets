package cn.kirilenkoo.www.coolpets.ui.fragments


import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.databinding.FragmentMainBinding
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.model.Tag
import cn.kirilenkoo.www.coolpets.ui.view.CoolPetHeaderView
import cn.kirilenkoo.www.coolpets.util.autoCleared
import timber.log.Timber
import javax.inject.Inject


class MainFragment : Fragment(),Injectable, PostListFragment.FragmentFinishedCallback {
    override fun finished(f: Fragment) {
        binding.header.addScrollChildFragment(f)
    }

    public var binding by autoCleared<FragmentMainBinding>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<FragmentMainBinding>(
                inflater,
                R.layout.fragment_main,
                container,
                false
        )
        binding = dataBinding
        binding.panelListener = View.OnClickListener {
            when (binding.bottomBar.indexOfChild(it)){
                1 -> findNavController().navigate(R.id.postEdeitFragment)
            }
        }
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("accr")
        binding.viewPager.adapter = MyCommonPagerAdapter(childFragmentManager!!)
        binding.viewPager.currentItem = 1
        binding.viewPager.offscreenPageLimit = 2
        val url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1536045875&di=f68c995ef8eeb805d26190cd9a09850b&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.free-icons-download.net%2Fimages%2Fweb-music-icon-84012.png"
        val tag1 = Tag(name = "turtle", url = url)
        val tag2 = Tag(name = "reptile", url = url)
        val tag3 = Tag(name = "frog", url = url)
        val tags = arrayListOf(tag1, tag2, tag3)
        binding.header.setupPagerAction(tags, binding.viewPager, binding.bottomBar)
        binding.bottomBar.getChildAt(1).setOnClickListener { Timber.d("bottom") }
    }

    class MyCommonPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            Timber.d("$position")
            val f = PostListFragment()
            return f
        }

        override fun getCount(): Int {
            return 3
        }
    }
}
