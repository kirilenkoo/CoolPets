package cn.kirilenkoo.www.coolpets.api

import androidx.lifecycle.MutableLiveData
import cn.kirilenkoo.www.coolpets.model.*
import cn.kirilenkoo.www.coolpets.util.AppExecutors
import com.avos.avoscloud.*

fun submitPost(post: PostWithContents): MutableLiveData<ApiResponse<ApiPHMsg>> {
    val liveData = MutableLiveData<ApiResponse<ApiPHMsg>>()
    val avList = ArrayList<AVObject>()
    for(content in post.contentList){
        avList.add(AVObject("PostContent").apply {
            put("text", content.text)
            put("img",content.url)
        })
    }
    AVObject.saveAllInBackground(avList, object : SaveCallback() {
        override fun done(p0: AVException?) {
            if (p0 == null) {
                val postAV = AVObject("Post").apply {
                    put("title", post.post.title)
                    put("contents", avList)
                }
                postAV.saveInBackground(object : SaveCallback() {
                    override fun done(p0: AVException?) {
                        if (p0 == null) {
                            post.post.postId = postAV.objectId
                            for (i in 0 until post.contentList.size) {
                                post.contentList[i].contentId = avList[i].objectId
                            }
                            liveData.value = ApiResponse.create(ApiPHMsg(postAV.objectId))
                        } else {
                            liveData.value = ApiErrorResponse(p0.toString())
                        }
                    }

                })
            } else {
                liveData.value = ApiErrorResponse(p0.toString())
            }


        }

    })
    return liveData
}

fun submitComment(comment: PostReply): MutableLiveData<ApiResponse<ApiPHMsg>>{
    val liveData = MutableLiveData<ApiResponse<ApiPHMsg>>()
    val replyAV = AVObject("Comment")
    replyAV.put("text", comment.content)
    replyAV.put("img", comment.imgUrl)
    replyAV.put("targetPost", AVObject.createWithoutData("Post",comment.postId))
    replyAV.saveInBackground(object : SaveCallback(){
        override fun done(p0: AVException?) {
            if (p0 == null){
                comment.replyId = replyAV.objectId
                liveData.value = ApiResponse.create(ApiPHMsg(replyAV.objectId))
            }else{
                liveData.value = ApiErrorResponse(p0.toString())
            }
        }
    })
    return liveData
}

fun getPostsWithContents(page : Int, appExecutors: AppExecutors): MutableLiveData<ApiResponse<List<PostWithContents>>>{
    val liveData = MutableLiveData<ApiResponse<List<PostWithContents>>>()
    val query: AVQuery<AVObject> = AVQuery("Post")
    val postList = ArrayList<PostWithContents>()
    query.include("contents")
    query.findInBackground(object: FindCallback<AVObject>(){
        override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
            if(p1 != null) {
                liveData.value = ApiErrorResponse(p1.toString())
                return
            }
            if(p0 != null){
                for( av in p0){
                    val post = PostWithContents().apply {
                        post = Post(av.objectId,av.getString("title"))
                        contentList = av.getList("contents").map {
                            it as AVObject
                        }.map {
                            PostContent(it.objectId,av.objectId,it.getString("text"),it.getString("img"))
                        }
                    }
                    postList.add(post)
                }
                liveData.value = ApiResponse.create(postList)
            }else{
                liveData.value = ApiEmptyResponse()
            }

        }

    })
    return liveData
}

fun getPostReplys(postId : String): MutableLiveData<ApiResponse<List<PostReply>>>{
    val liveData = MutableLiveData<ApiResponse<List<PostReply>>>()
    val query: AVQuery<AVObject> = AVQuery("Comment")
    query.whereEqualTo("targtPost",postId)
    query.findInBackground(object : FindCallback<AVObject>(){
        override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
            if(p1 != null){
                liveData.value = ApiErrorResponse(p1.toString())
                return
            }
            if(p0 != null){
                liveData.value = ApiResponse.create(p0.map {
                    PostReply(it.objectId,postId,0,it.getString("text"),it.getString("img"))
                })
            }else{
                liveData.value = ApiEmptyResponse()
            }
        }

    })
    return liveData
}