package cn.kirilenkoo.www.coolpets.api

import cn.kirilenkoo.www.coolpets.model.Comment
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.model.PostContent
import cn.kirilenkoo.www.coolpets.model.Tag
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by huangzilong on 2018/3/22.
 */
@Deprecated("use NetWorkBoundResource")
class NetworkDataSource {
    companion object {
        fun fetchTags(): Observable<List<Tag>> {
            return Observable.create<List<Tag>> {
                val avList = AVQuery<AVObject>("Tag").find()
                val tagList = avList.mapNotNull {
                    it.getString("name")?.let {
                        Tag(it)
                    }
                }
                it.onNext(
                        tagList
                )
            }
        }

        fun fetchPosts(): Observable<List<Post>> {
            return Observable.create<List<Post>> {
                val avList = AVQuery<AVObject>("Post").include("contents").find()
                val postList = avList.mapNotNull {
                    val post: Post? = it.getString("title")?.let {
                        Post(it, listOf())
                    }
                    val contents = it.getList("contents", AVObject::class.java)
                    post?.let {
                        post.contents = contents.mapNotNull {
                            PostContent(img = it.getString("img"), text = it.getString("text"), textSize = it.getInt("textSize"))
                        }
                    }
                    return@mapNotNull post
                }
                it.onNext(
                        postList
                )
            }
        }

        fun fetchPostComments(postId: String?): Single<List<Comment>>{
            return Single.create {
                try {
                    val avList = AVQuery<AVObject>("Comment").whereEqualTo("targetPost", AVObject.createWithoutData("Post", postId)).find()
                    val commentList = avList.mapNotNull {
                        Comment(text = it.getString("text"), totalIndex = it.getInt("totalIndex"))
                    }
                    it.onSuccess(commentList)
                }catch (e: Exception){
                    it.onError(e)
                }

            }
//            return Observable.create<List<Comment>> {
//                val avList = AVQuery<AVObject>("Comment").whereEqualTo("targetPost", AVObject.createWithoutData("Post", postId)).find()
//                val commentList = avList.mapNotNull {
//                    Comment(text = it.getString("text"), totalIndex = it.getInt("totalIndex"))
//                }
//                it.onNext(
//                        commentList
//                )
//            }
        }
    }
}