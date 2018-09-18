package cn.kirilenkoo.www.coolpets.ui.adapter

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.databinding.PostItemBinding
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.util.AppExecutors

class PostAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val callback: ((Post) -> Unit)?
): DataBoundListAdapter<PostWithContents, PostItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<PostWithContents>() {
            override fun areItemsTheSame(oldItem: PostWithContents?, newItem: PostWithContents?): Boolean =
                newItem?.post?.postId == oldItem?.post?.postId


            override fun areContentsTheSame(oldItem: PostWithContents?, newItem: PostWithContents?): Boolean =
                    newItem?.post?.title == oldItem?.post?.title

        }
        ) {
    override fun createBinding(parent: ViewGroup): PostItemBinding {
        val binding = DataBindingUtil
                .inflate<PostItemBinding>(
                        LayoutInflater.from(parent.context),
                        R.layout.post_item,
                        parent,
                        false,
                        dataBindingComponent
                )
//        binding.root.setOnClickListener {
//            binding.post?.let {
//                callback?.invoke(it)
//            }
//        }
        return binding
    }

    override fun bind(binding: PostItemBinding, item: PostWithContents) {
        binding.post = item.post
    }

}