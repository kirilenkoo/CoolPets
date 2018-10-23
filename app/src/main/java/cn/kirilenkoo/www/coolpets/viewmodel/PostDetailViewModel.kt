package cn.kirilenkoo.www.coolpets.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel;
import cn.kirilenkoo.www.coolpets.model.ApiPHMsg
import cn.kirilenkoo.www.coolpets.model.PostReply
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.repository.PostReplyRepository
import cn.kirilenkoo.www.coolpets.repository.PostRepository
import com.android.example.github.vo.Resource
import javax.inject.Inject

class PostDetailViewModel @Inject constructor(val postReplyRepository: PostReplyRepository): ViewModel() {
    lateinit var postReplyListData: LiveData<Resource<List<PostReply>>>
    fun submitReply(reply:PostReply): LiveData<Resource<PostReply>> = postReplyRepository.savePostReply(reply)

}
