package cn.kirilenkoo.www.coolpets.api

import android.util.Log
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.model.PostContent
import cn.kirilenkoo.www.coolpets.model.Tag
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import io.reactivex.Observable

/**
 * Created by huangzilong on 2018/3/22.
 */
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
                val avList = AVQuery<AVObject>("Post").find()
                val postList = avList.mapNotNull {
                    val post: Post? = it.getString("title")?.let {
                        Post(it, listOf())
                    }
                    val contents = it.getList("contents", AVObject::class.java)
                    post?.let {
                        post.contents = contents.mapNotNull {
                            PostContent()
                        }
                    }
                    return@mapNotNull post
                }
                it.onNext(
                        postList
                )
            }
        }
    }
}