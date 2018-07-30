package cn.kirilenkoo.www.coolpets.ui.fragments


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.api.ApiResponseWrapper
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.model.PostContent
import cn.kirilenkoo.www.coolpets.viewmodel.PostListViewModel
import com.android.example.github.vo.Resource
import timber.log.Timber
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PostListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PostListFragment : BaseFragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var postListViewModel: PostListViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        postListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PostListViewModel::class.java)
        postListViewModel.initData()
        postListViewModel.postListData.observe(this, Observer<Resource<Post>> {
            it?.data?.let {
                Timber.d("${it.title}")
            }
        })

    }

//    fun testLiveData(){
//        Timber.d("test")
//        val liveData = MutableLiveData<ApiResponseWrapper<Post>>()
//        liveData.observe(this, Observer {
//            Timber.d( "get")
//            var status = it?.status
//            var post = it?.data as Post
//            Timber.d("%s, %s", status, System.currentTimeMillis())
//            Timber.d("%s, %s", post.title, System.currentTimeMillis())
//        })
//        val apiResponseWrapper = ApiResponseWrapper<Post>()
//        liveData.value = apiResponseWrapper
//
////        Thread(Runnable {
////            Thread.sleep(3000)
////
////            apiResponseWrapper.status = "load"
////            val list = listOf<PostContent>()
////            apiResponseWrapper.data = Post("title", list)
////        }.toString())
//    }
}

