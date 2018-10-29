package cn.kirilenkoo.www.coolpets.ui.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.binding.FragmentDataBindingComponent
import cn.kirilenkoo.www.coolpets.databinding.PostDetailFragmentBinding
import cn.kirilenkoo.www.coolpets.databinding.PostEditFragmentBinding
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.util.autoCleared
import timber.log.Timber

class PostEditFragment : BaseFragment(), Injectable {
    var binding by autoCleared<PostEditFragmentBinding>()
    var dataBindingComponent = FragmentDataBindingComponent(this)
    companion object {
        fun newInstance() = PostEditFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<PostEditFragmentBinding>(
                inflater,
                R.layout.post_edit_fragment,
                container,
                false,
                dataBindingComponent
        )
        binding = dataBinding
        binding.panelListener = View.OnClickListener {
            when (binding.containerEditPanel.indexOfChild(it)){
                0 -> Timber.d("0")
                1 -> Timber.d("1")
                2 -> Timber.d("2")
                3 -> Timber.d("3")

            }
        }
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}
