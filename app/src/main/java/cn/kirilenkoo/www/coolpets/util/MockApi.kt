package cn.kirilenkoo.www.coolpets.util

import android.arch.lifecycle.MutableLiveData
import cn.kirilenkoo.www.coolpets.api.ApiResponse
import cn.kirilenkoo.www.coolpets.model.Post
import timber.log.Timber

fun mockGetPosts (page : Int, appExecutors: AppExecutors):MutableLiveData<ApiResponse<List<Post>>>{
    val liveData = MutableLiveData<ApiResponse<List<Post>>>()
    Thread {
        Thread.sleep(2000)
        val posts: ArrayList<Post> = ArrayList()
        for ( i in 1..10){
            posts.add(Post(title = "$i"))
        }
        appExecutors.mainThread().execute{
            liveData.value = ApiResponse.create(posts)
            Timber.d("%s : set value time", System.currentTimeMillis())
        }
    }.start()
    Timber.d("%s : return live data time", System.currentTimeMillis())
    return liveData
}