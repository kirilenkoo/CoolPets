package cn.kirilenkoo.www.coolpets.ui.activities

import android.os.Bundle
import android.util.Log
import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseActivity
import cn.kirilenkoo.www.coolpets.model.Comment
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.repository.CommentRepository
import com.avos.avoscloud.AVObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rep = CommentRepository()
        val params = HashMap<String, Any>()
        params["postId"] = "5aba0f317565710045876558"
        val ob: Observable<List<Comment>> = rep.fetchData(params) as Observable<List<Comment>>
        Log.d("activity","observable created")
        ob.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> dealCommentsResult(result)}
                )
//        generatePost()
    }

    fun dealCommentsResult(list: List<Comment>){
        Log.d("result", list.size.toString())
        list.forEach {
            Log.d("result", it.text)
        }
    }

    fun dealPostResult(list: List<Post>){
        Log.d("result", list.size.toString())
        list.forEach {
            Log.d("result", it.title)
            it.contents.forEach {
                Log.d("result", it.text)
            }
        }
    }


    fun generatePost(){
        val post = AVObject("Post")
        val content1 = AVObject.createWithoutData("PostContent","5aba0ec19f545400458d0c50")
        val content2 = AVObject.createWithoutData("PostContent","5aba0eb6a22b9d00451a83f7")
        val list = listOf<Any>(content1,content2)
        post.put("contents",list)
        post.saveInBackground()
    }
}
