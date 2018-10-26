package cn.kirilenkoo.www.coolpets.util

import androidx.lifecycle.MutableLiveData
import cn.kirilenkoo.www.coolpets.api.ApiResponse
import cn.kirilenkoo.www.coolpets.model.*
import timber.log.Timber
import java.util.*

fun mockGetPosts (page : Int, appExecutors: AppExecutors):MutableLiveData<ApiResponse<List<Post>>>{
    val liveData = MutableLiveData<ApiResponse<List<Post>>>()
    Thread {
        Thread.sleep(2000)
        val posts: ArrayList<Post> = ArrayList()
        for ( i in 1..10){
            posts.add(Post(postId = generatePostId(), title = "$i"))
        }
        appExecutors.mainThread().execute{
            liveData.value = ApiResponse.create(posts)
            Timber.d("%s : set value time", System.currentTimeMillis())
        }
    }.start()
    Timber.d("%s : return live data time", System.currentTimeMillis())
    return liveData
}

fun mockGetPostsWithContents(page : Int, appExecutors: AppExecutors): MutableLiveData<ApiResponse<List<PostWithContents>>>{
    val liveData = MutableLiveData<ApiResponse<List<PostWithContents>>>()
    Thread {
        Thread.sleep(2000)
        val posts: ArrayList<PostWithContents> = ArrayList()
        for ( i in 1..1){
            val pwc = PostWithContents()
            pwc.post = Post(generatePostId(),"$i")
            val postContents = arrayListOf<PostContent>()
            for (j in 0..5){
                postContents.add(PostContent(postId = pwc.post.postId,contentId = generatePostId(),text = "$j"))
            }
            pwc.contentList = postContents
            posts.add(pwc)
        }
        appExecutors.mainThread().execute{
            liveData.value = ApiResponse.create(posts)
        }
    }.start()
    return liveData
}

fun mockPostPostReply(postReply: PostReply, appExecutors: AppExecutors): MutableLiveData<ApiResponse<ApiPHMsg>>{
    val liveData = MutableLiveData<ApiResponse<ApiPHMsg>>()
    Thread {
        Thread.sleep(2000)
        appExecutors.mainThread().execute {
            liveData.value = ApiResponse.create(ApiPHMsg(generatePostId()))
        }
    }.start()
    return liveData
}

fun generatePostId():String = UUID.randomUUID().toString()
