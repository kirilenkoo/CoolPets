package cn.kirilenkoo.www.coolpets.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import cn.kirilenkoo.www.coolpets.api.ApiResponseWrapper
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.util.AppExecutors
import com.android.example.github.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by huangzilong on 2018/3/22.
 */
@Singleton
class PostRepository @Inject constructor(private val appExecutor : AppExecutors){
    fun loadPost(postId: String):LiveData<Resource<Post>>{
        return object : NetworkBoundResource<Post,Post>(appExecutor){
            override fun saveCallResult(item: Post) {
            }

            override fun shouldFetch(data: Post?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<Post> {
                val liveData = MutableLiveData<Post>()
                liveData.value = Post("kkk")
                return liveData
            }

            override fun createCall(): LiveData<ApiResponseWrapper<Post>> {
                val liveData = MutableLiveData<ApiResponseWrapper<Post>>()
                val apiResponseWrapper = ApiResponseWrapper<Post>(Post("hhh"))
                liveData.value = apiResponseWrapper
                // post = getPostFromCloud
                //if post == null or return error, ApiResponseWrapper setstatus error
                //else apiresponseWrapper setbody post

                return liveData
            }

        }.asLiveData()
    }
}