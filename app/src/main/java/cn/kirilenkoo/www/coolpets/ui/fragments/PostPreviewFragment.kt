package cn.kirilenkoo.www.coolpets.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.binding.FragmentDataBindingComponent
import cn.kirilenkoo.www.coolpets.databinding.FragmentPostPreviewBinding
import cn.kirilenkoo.www.coolpets.databinding.PostDetailFragmentBinding
import cn.kirilenkoo.www.coolpets.databinding.PostEditFragmentBinding
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.util.ImgUploadController
import cn.kirilenkoo.www.coolpets.util.autoCleared
import cn.kirilenkoo.www.coolpets.viewmodel.EditPost
import javax.inject.Inject

class PostPreviewFragment : BaseFragment() ,Injectable{
    private var mEditPost: EditPost? = null
    @Inject
    lateinit var imgUploadController: ImgUploadController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mEditPost = it.getParcelable("editPost")
        }
    }
    var dataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<FragmentPostPreviewBinding>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val dataBinding = DataBindingUtil.inflate<FragmentPostPreviewBinding>(
                inflater,
                R.layout.fragment_post_preview,
                container,
                false,
                dataBindingComponent
        )
        binding = dataBinding
        binding.post = generateRealPost(mEditPost)
        return dataBinding.root
    }

    private fun generateRealPost(editPost: EditPost?) =  PostWithContents().apply {
            post = Post("",editPost?.postTitle?:"")
            editPost?.apply {
                for(postContent in contents){
                    if(postContent.url != null){
                        postContent.url = imgUploadController.tradeUrl(postContent.url)
                    }
                }
                contentList = contents
            }
        }


    companion object {
        @JvmStatic
        fun newInstance(editPost: EditPost) =
                PostPreviewFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("editPost", editPost)
                    }
                }
    }
}
