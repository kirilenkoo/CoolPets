package cn.kirilenkoo.www.coolpets.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.kirilenkoo.www.coolpets.api.ApiResponse
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
    fun loadPost(postId: String):LiveData<Resource<Post>>{
        return object : NetworkBoundResource<Post,Post>(appExecutor){
            override fun saveCallResult(item: Post) {
            }

            override fun shouldFetch(data: Post?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<Post> {
                val liveData = MutableLiveData<Post>()
//                liveData.value = Post("kkk")
                return liveData
            }

            override fun createCall(): LiveData<ApiResponse<Post>> {
                val liveData = MutableLiveData<ApiResponse<Post>>()
//                val apiResponseWrapper = ApiResponse<Post>(Post("hhh"))
//                liveData.value = apiResponseWrapper
                return liveData
            }

        }.asLiveData()
    }

    fun loadPosts(page: Int):LiveData<Resource<List<Post>>>{
        return object : NetworkBoundResource<List<Post>, List<Post>>(appExecutor){
            override fun saveCallResult(item: List<Post>) {
                for (post in item){
                    postDao.insert(post)
                }
            }

            override fun shouldFetch(data: List<Post>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Post>> = postDao.findAll()
//            {
//                val liveData = MutableLiveData<List<Post>>()
//                val posts = ArrayList<Post>()
//                posts.add(Post("hhh"))
//                liveData.value = posts
//                return liveData
//            }

            override fun createCall(): LiveData<ApiResponse<List<Post>>>  = mockGetPosts(page, appExecutor)

        }.asLiveData()
    }

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

            override fun createCall(): LiveData<ApiResponse<List<PostWithContents>>> = mockGetPostsWithContents(page, appExecutor)

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

            override fun createCall(): LiveData<ApiResponse<ApiPHMsg>> = submitPost(post, appExecutor)

        }.asLiveData()
    }
}