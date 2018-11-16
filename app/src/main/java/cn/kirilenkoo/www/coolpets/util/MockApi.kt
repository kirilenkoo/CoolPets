package cn.kirilenkoo.www.coolpets.util

import androidx.lifecycle.MutableLiveData
import cn.kirilenkoo.www.coolpets.api.ApiErrorResponse
import cn.kirilenkoo.www.coolpets.api.ApiResponse
import cn.kirilenkoo.www.coolpets.model.*
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVObject.saveAllInBackground
import com.avos.avoscloud.SaveCallback
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

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
                postContents.add(PostContent(postId = pwc.post.postId,contentId = generatePostId(),text = "$j",url = ""))
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

fun submitPost(post: PostWithContents, appExecutors: AppExecutors): MutableLiveData<ApiResponse<ApiPHMsg>>{
    val liveData = MutableLiveData<ApiResponse<ApiPHMsg>>()
    val avList = ArrayList<AVObject>()
    for(content in post.contentList){
        avList.add(AVObject("PostContent").apply {
            put("text", content.text)
            put("img",content.url)
        })
    }
    saveAllInBackground(avList,object : SaveCallback() {
        override fun done(p0: AVException?) {
            if(p0 == null){
                val postAV = AVObject("Post").apply {
                    put("title", post.post.title)
                    put("contents",avList)
                }
                postAV.saveInBackground(object : SaveCallback(){
                    override fun done(p0: AVException?) {
                        if(p0 == null){
                            post.post.postId = postAV.objectId
                            for (i in 0 until post.contentList.size){
                                post.contentList[i].contentId = avList[i].objectId
                            }
                            liveData.value = ApiResponse.Companion.create(ApiPHMsg(""))
                        }else{
                            liveData.value = ApiErrorResponse(p0.toString())
                        }
                    }

                })
            }else{
                liveData.value = ApiErrorResponse(p0.toString())
            }


        }

    })
    return liveData
}