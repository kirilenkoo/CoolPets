package cn.kirilenkoo.www.coolpets.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.kirilenkoo.www.coolpets.api.ApiResponse
import cn.kirilenkoo.www.coolpets.api.getPostReplys
import cn.kirilenkoo.www.coolpets.api.submitComment
import cn.kirilenkoo.www.coolpets.db.PostDao
import cn.kirilenkoo.www.coolpets.db.PostReplyDao
import cn.kirilenkoo.www.coolpets.model.ApiPHMsg
import cn.kirilenkoo.www.coolpets.model.PostReply
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.util.AbsentLiveData
import cn.kirilenkoo.www.coolpets.util.AppExecutors
import cn.kirilenkoo.www.coolpets.util.mockGetPostsWithContents
import cn.kirilenkoo.www.coolpets.util.mockPostPostReply
import com.android.example.github.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostReplyRepository @Inject constructor(private val appExecutor : AppExecutors,
                                              private val postReplyDao: PostReplyDao) {
    fun savePostReply(postReply:PostReply):LiveData<Resource<PostReply>>{
        return object : NetworkBoundResource<PostReply, ApiPHMsg>(appExecutor){
            override fun saveCallResult(item: ApiPHMsg) {
                val updatedReply = PostReply(postReply.replyId,postReply.postId,postReply.posterId,postReply.content,postReply.imgUrl)
                postReplyDao.insert(updatedReply)
            }

            override fun shouldFetch(data: PostReply?): Boolean = true

            override fun loadFromDb(): LiveData<PostReply> = AbsentLiveData.create()

            override fun createCall(): LiveData<ApiResponse<ApiPHMsg>> = submitComment(postReply)

        }.asLiveData()
    }

    fun fetchPostReplys(postId: String):LiveData<Resource<List<PostReply>>>{
        return object : NetworkBoundResource<List<PostReply>,List<PostReply>>(appExecutor){
            override fun saveCallResult(item: List<PostReply>) {
            }

            override fun shouldFetch(data: List<PostReply>?): Boolean = false

            override fun loadFromDb(): LiveData<List<PostReply>> {
                return postReplyDao.findPostReply(postId)
            }

            override fun createCall(): LiveData<ApiResponse<List<PostReply>>> = getPostReplys(postId)

        }.asLiveData()
    }

}