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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.binding.FragmentDataBindingComponent
import cn.kirilenkoo.www.coolpets.databinding.PostDetailFragmentBinding
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.model.PostReply
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.ui.adapter.PostReplyAdapter
import cn.kirilenkoo.www.coolpets.util.AppExecutors
import cn.kirilenkoo.www.coolpets.util.autoCleared
import cn.kirilenkoo.www.coolpets.viewmodel.PostDetailViewModel
import cn.kirilenkoo.www.coolpets.viewmodel.PostListViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.android.example.github.vo.Status
import timber.log.Timber
import java.util.*
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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PostDetailViewModel::class.java)
        viewModel.postReplyData.observe(viewLifecycleOwner, Observer {
            Timber.d(it.status.name)
        })
        viewModel.postReplyListData.observe(viewLifecycleOwner, Observer {
//            when(it.status){
//                Status.SUCCESS -> adapter.submitList(it.data)
//            }
            Timber.d("${it.status}->${it.data?.size}")
            adapter.submitList(it.data)
        })
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
                    .negativeButton(text = "取消"){
                        it.dismiss()
                    }
        }
        dialog!!.show()
    }

    private fun submitReply(s: String) {
        viewModel.submitReply(PostReply(generateReplyId(),mPostDetail.post.postId,2,s,""))
        Timber.d("$s submitted")
    }

    private fun generateReplyId():String{
       return UUID.randomUUID().toString()
    }

    private var adapter by autoCleared<PostReplyAdapter>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = PostReplyAdapter(dataBindingComponent,appExecutors,null)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
        binding.recyclerView.adapter = adapter
        viewModel.fetchPostReplys(mPostDetail.post.postId)
    }

}
