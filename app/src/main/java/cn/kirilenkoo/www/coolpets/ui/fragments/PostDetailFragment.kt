package cn.kirilenkoo.www.coolpets.ui.fragments

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer

import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.binding.FragmentDataBindingComponent
import cn.kirilenkoo.www.coolpets.databinding.PostDetailFragmentBinding
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.model.PostReply
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.util.AppExecutors
import cn.kirilenkoo.www.coolpets.util.autoCleared
import cn.kirilenkoo.www.coolpets.viewmodel.PostDetailViewModel
import cn.kirilenkoo.www.coolpets.viewmodel.PostListViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import timber.log.Timber
import javax.inject.Inject

class PostDetailFragment : BaseFragment(), Injectable{
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var postListViewModel: PostListViewModel

    lateinit var recyclerView: RecyclerView

    lateinit var mPostDetail: PostWithContents

    var binding by autoCleared<PostDetailFragmentBinding>()
    companion object {
        @JvmStatic
        fun newInstance(postWithContents: PostWithContents) =
                PostDetailFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("post", postWithContents)
                    }
                }
    }

    private lateinit var viewModel: PostDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPostDetail = it.getParcelable("post")
        }
    }
    var dataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<PostDetailFragmentBinding>(
                inflater,
                R.layout.post_detail_fragment,
                container,
                false,
                dataBindingComponent
        )
        binding = dataBinding
        binding.post = mPostDetail
        binding.buttonLike.setOnClickListener {

        }
        binding.buttonReply.setOnClickListener {
            showReplyDialog()
        }
        return dataBinding.root
    }
    var dialog: MaterialDialog? = null
    private fun showReplyDialog() {
        if(dialog == null){
            dialog = MaterialDialog(activity!!).customView(R.layout.dialog_reply_post)
                    .positiveButton(text = "发布"){
                        dialog -> dialog.getCustomView()?.
                            findViewById<EditText>(R.id.editReply)?.
                            text.toString().apply {
                                submitReply(this)
                        }
                    }
        }
        dialog!!.show()
    }

    private fun submitReply(s: String) {
        viewModel.submitReply(PostReply(1,mPostDetail.post.postId,2,s,"")).
                observe(this, Observer {
                    Timber.d(it.status.name)
                    if(dialog!=null && dialog!!.isShowing) dialog?.dismiss()
                    Timber.d(it.status.name)
                })
        Timber.d("$s submitted")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PostDetailViewModel::class.java)
    }

}
