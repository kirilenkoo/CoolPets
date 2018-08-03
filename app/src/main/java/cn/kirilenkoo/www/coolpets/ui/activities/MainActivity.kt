package cn.kirilenkoo.www.coolpets.ui.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseActivity
import cn.kirilenkoo.www.coolpets.model.Comment
import cn.kirilenkoo.www.coolpets.model.LeetCode
import cn.kirilenkoo.www.coolpets.model.Pet
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.repository.CommentRepository
import com.avos.avoscloud.AVObject
import dagger.Component
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity()
        , HasSupportFragmentInjector
{
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    override fun supportFragmentInjector() = dispatchingAndroidInjector
    @Inject lateinit var mPet:Pet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPet.call()
//        val rep = CommentRepository()
//        val params = HashMap<String, Any>()
//        params["postId"] = "5aba0f317565710045876558"
//        val ob: Single<List<Comment>> = rep.fetchData(params) as Single<List<Comment>>
//        Log.d("activity","observable created")
//
//        val subscriber: Observer<List<Comment>> = object:Observer<List<Comment>>{
//            override fun onComplete() {
//            }
//
//            override fun onSubscribe(d: Disposable) {
//            }
//
//            override fun onNext(t: List<Comment>) {
//                dealCommentsResult(t)
//            }
//
//            override fun onError(e: Throwable) {
//            }
//
//        }
//        ob.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe (
//                        {result -> dealCommentsResult(result)},
//                        {error -> Timber.e(error.message)}
//                )

//        ob.retry()
//        generatePost()
    }

//    fun dealCommentsResult(list: List<Comment>){
//        Log.d("result", list.size.toString())
//        list.forEach {
//            Log.d("result", it.text)
//        }
//    }
//
//    fun dealPostResult(list: List<Post>){
//        Log.d("result", list.size.toString())
//        list.forEach {
//            Log.d("result", it.title)
//            it.contents.forEach {
//                Log.d("result", it.text)
//            }
//        }
//    }
//
//
//    fun generatePost(){
//        val post = AVObject("Post")
//        val content1 = AVObject.createWithoutData("PostContent","5aba0ec19f545400458d0c50")
//        val content2 = AVObject.createWithoutData("PostContent","5aba0eb6a22b9d00451a83f7")
//        val list = listOf<Any>(content1,content2)
//        post.put("contents",list)
//        post.saveInBackground()
//    }
}
