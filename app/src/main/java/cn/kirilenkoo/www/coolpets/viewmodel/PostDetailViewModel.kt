package cn.kirilenkoo.www.coolpets.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel;
import cn.kirilenkoo.www.coolpets.model.ApiPHMsg
import cn.kirilenkoo.www.coolpets.model.PostReply
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.repository.PostReplyRepository
import cn.kirilenkoo.www.coolpets.repository.PostRepository
import cn.kirilenkoo.www.coolpets.util.AbsentLiveData
import com.android.example.github.vo.Resource
import javax.inject.Inject

class PostDetailViewModel @Inject constructor(val postReplyRepository: PostReplyRepository): ViewModel() {
    private val postReply:MutableLiveData<PostReply> = MutableLiveData()
    private val postIdData:MutableLiveData<String> = MutableLiveData()
    var postReplyData: LiveData<Resource<PostReply>> = Transformations.switchMap(postReply){
        it -> if(it.postId == null) AbsentLiveData.create()
                else postReplyRepository.savePostReply(it)
    }
    var postReplyListData: LiveData<Resource<List<PostReply>>> = Transformations.switchMap(postIdData){
        it -> if(it.isEmpty()) AbsentLiveData.create()
        else postReplyRepository.fetchPostReplys(it)
    }
    fun submitReply(reply:PostReply){
        if(reply == postReply.value) return
        postReply.value = reply
    }

    fun fetchPostReplys(postId: String){
        if(postId == postIdData.value) return
        postIdData.value = postId
    }
}
