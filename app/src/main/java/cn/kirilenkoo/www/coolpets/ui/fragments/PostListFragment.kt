package cn.kirilenkoo.www.coolpets.ui.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.viewmodel.PostListViewModel
import com.android.example.github.vo.Resource
import timber.log.Timber
import javax.inject.Inject
class PostListFragment : BaseFragment(), Injectable {
    interface FragmentFinishedCallback{
        fun finished(f: Fragment)
    }
    private var listener: FragmentFinishedCallback? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = Adapter()
        postListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PostListViewModel::class.java)
        postListViewModel.initData()
        postListViewModel.postListData.observe(this, Observer<Resource<List<PostWithContents>>> {
            it?.data?.let {
                for(pwc in it){
                    Timber.d("${pwc.post.title}")
                    Timber.d("${pwc.contentList.size}")
                }
                posts = it
                recyclerView.adapter.notifyDataSetChanged()
            }
        })
        listener?.finished(this)
    }

    inner class Adapter: RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
                ViewHolder(LayoutInflater.from(activity).inflate(R.layout.card_main_hub_long_post_light,parent,false))

        override fun getItemCount(): Int = posts.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        }

    }
    class ViewHolder (view: View): RecyclerView.ViewHolder(view)
}

