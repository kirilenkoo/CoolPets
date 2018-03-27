package cn.kirilenkoo.www.coolpets.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseActivity
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.model.Tag
import cn.kirilenkoo.www.coolpets.repository.PostRepository
import cn.kirilenkoo.www.coolpets.repository.TagRepository
import com.avos.avoscloud.AVObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rep = PostRepository()
        val ob: Observable<List<Post>> = rep.fetchData(HashMap()) as Observable<List<Post>>
        Log.d("activity","observable created")
        ob.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> dealResult(result)}
                )
//        generatePost()
    }

    fun dealResult(list: List<Post>){
        Log.d("result", list.size.toString())
        list.forEach {
            Log.d("result", it.title)
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
