package cn.kirilenkoo.www.coolpets.ui.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.model.Post
import cn.kirilenkoo.www.coolpets.viewmodel.PostListViewModel
import com.android.example.github.vo.Resource
import timber.log.Timber
import javax.inject.Inject
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

