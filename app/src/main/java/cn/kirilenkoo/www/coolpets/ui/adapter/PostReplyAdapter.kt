package cn.kirilenkoo.www.coolpets.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.databinding.PostItemBinding
import cn.kirilenkoo.www.coolpets.databinding.PostReplyItemBinding
import cn.kirilenkoo.www.coolpets.model.PostReply
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.util.AppExecutors

class PostReplyAdapter(private val dataBindingComponent: DataBindingComponent,
                       appExecutors: AppExecutors,
                       private val callback: ((PostReply) -> Unit)?): DataBoundListAdapter<PostReply, PostReplyItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<PostReply>() {
            override fun areItemsTheSame(oldItem: PostReply, newItem: PostReply): Boolean =
                    newItem?.replyId == oldItem?.replyId
            override fun areContentsTheSame(oldItem: PostReply, newItem: PostReply): Boolean =
                    newItem?.content == oldItem?.content
        }
)  {
    override fun createBinding(parent: ViewGroup): PostReplyItemBinding {
        val binding = DataBindingUtil
                .inflate<PostReplyItemBinding>(
                        LayoutInflater.from(parent.context),
                        R.layout.post_reply_item,
                        parent,
                        false,
                        dataBindingComponent
                )
        return binding
    }

    override fun bind(binding: PostReplyItemBinding, item: PostReply) {
        binding.postReply = item
    }

}