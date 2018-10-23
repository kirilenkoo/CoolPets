package cn.kirilenkoo.www.coolpets.ui.fragments


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import androidx.databinding.DataBindingComponent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.binding.FragmentDataBindingComponent
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.model.PostContent
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.ui.adapter.PostAdapter
import cn.kirilenkoo.www.coolpets.util.AppExecutors
import cn.kirilenkoo.www.coolpets.util.AutoClearedValue
import cn.kirilenkoo.www.coolpets.util.autoCleared
import cn.kirilenkoo.www.coolpets.viewmodel.PostListViewModel
import com.android.example.github.vo.Resource
import androidx.navigation.fragment.findNavController
import com.android.example.github.vo.Status

import timber.log.Timber
import javax.inject.Inject
class PostListFragment : BaseFragment(), Injectable {
    interface FragmentFinishedCallback{
        fun finished(f: Fragment)
    }
    private var listener: FragmentFinishedCallback? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var postListViewModel: PostListViewModel

    lateinit var recyclerView: RecyclerView


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(parentFragment is FragmentFinishedCallback){
            listener = parentFragment as FragmentFinishedCallback
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        return view
    }

    var posts = listOf<PostWithContents>()
    var dataBindingComponent = FragmentDataBindingComponent(this)

    private var adapter by autoCleared<PostAdapter>()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val adapter = PostAdapter(dataBindingComponent, appExecutors){
            var bundle = Bundle()
            bundle.putParcelable("post", it)
            navController().navigate(R.id.postDetailFragment,bundle)
//            Navigation.findNavController(recyclerView)
        }
        this.adapter = adapter
        recyclerView.adapter = adapter

        postListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PostListViewModel::class.java)
        postListViewModel.initData()
        postListViewModel.postListData.observe(this, Observer<Resource<List<PostWithContents>>> {
//            it?.data?.let {
//                for(pwc in it){
//                    Timber.d("${pwc.post.title}")
//                    Timber.d("${pwc.contentList.size}")
//                }
//                adapter.submitList()
//                recyclerView.adapter.notifyDataSetChanged()
//            }
            Timber.d(it.status.name)
            when(it.status){
                Status.SUCCESS -> Timber.d("$it.data?.size")
            }
            adapter.submitList(it?.data)
        })
        listener?.finished(this)

    }
    fun navController() = findNavController()
}

