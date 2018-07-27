package cn.kirilenkoo.www.coolpets.ui.fragments


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.api.ApiResponseWrapper
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.model.PostContent
import timber.log.Timber

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
class PostListFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PostListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                PostListFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        testLiveData()
    }

    fun testLiveData(){
        Timber.d("test")
        val liveData = MutableLiveData<ApiResponseWrapper<Post>>()
        liveData.observe(this, Observer {
            Timber.d( "get")
            var status = it?.status
            var post = it?.data as Post
            Timber.d("%s, %s", status, System.currentTimeMillis())
            Timber.d("%s, %s", post.title, System.currentTimeMillis())
        })
        val apiResponseWrapper = ApiResponseWrapper<Post>()
        liveData.value = apiResponseWrapper

//        Thread(Runnable {
//            Thread.sleep(3000)
//
//            apiResponseWrapper.status = "load"
//            val list = listOf<PostContent>()
//            apiResponseWrapper.data = Post("title", list)
//        }.toString())
    }
}

