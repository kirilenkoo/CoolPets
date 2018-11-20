package cn.kirilenkoo.www.coolpets.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.kirilenkoo.www.coolpets.api.ApiResponse
import cn.kirilenkoo.www.coolpets.api.getPostsWithContents
import cn.kirilenkoo.www.coolpets.api.submitPost
import cn.kirilenkoo.www.coolpets.db.PostContentDao
import cn.kirilenkoo.www.coolpets.db.PostDao
import cn.kirilenkoo.www.coolpets.db.PostWithContentsDao
import cn.kirilenkoo.www.coolpets.model.ApiPHMsg
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.util.*
import com.android.example.github.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by huangzilong on 2018/3/22.
 */
@Singleton
class PostRepository @Inject constructor(
        private val appExecutor : AppExecutors,
        private val postDao: PostDao,
        private val postWithContentsDao: PostWithContentsDao,
        private val postContentDao: PostContentDao){

    fun loadPostsWithContents(page: Int):LiveData<Resource<List<PostWithContents>>>{
        return object : NetworkBoundResource<List<PostWithContents>, List<PostWithContents>>(appExecutor){
            override fun saveCallResult(item: List<PostWithContents>) {
                for (pwc in item){
                    postDao.insert(pwc.post)
                    for (pc in pwc.contentList){
                        postContentDao.insert(pc)
                    }
                }
            }

            override fun shouldFetch(data: List<PostWithContents>?): Boolean = true

            override fun loadFromDb(): LiveData<List<PostWithContents>> = postWithContentsDao.findAllWithContent()

            override fun createCall(): LiveData<ApiResponse<List<PostWithContents>>> = getPostsWithContents(page, appExecutor)

        }.asLiveData()
    }
    fun savePostWithContents(post: PostWithContents):LiveData<Resource<PostWithContents>>{
        return object : NetworkBoundResource<PostWithContents, ApiPHMsg>(appExecutor){
            override fun saveCallResult(item: ApiPHMsg) {
                postDao.insert(post.post)
                for (pc in post.contentList){
                    postContentDao.insert(pc)
                }
            }

            override fun shouldFetch(data: PostWithContents?): Boolean = true

            override fun loadFromDb(): LiveData<PostWithContents> = AbsentLiveData.create()

            override fun createCall(): LiveData<ApiResponse<ApiPHMsg>> = submitPost(post)

        }.asLiveData()
    }
}