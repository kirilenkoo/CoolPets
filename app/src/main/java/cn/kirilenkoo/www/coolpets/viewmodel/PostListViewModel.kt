package cn.kirilenkoo.www.coolpets.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.repository.PostRepository
import com.android.example.github.vo.Resource
import javax.inject.Inject

class PostListViewModel @Inject constructor(val postRepository: PostRepository): ViewModel() {
    lateinit var postListData:LiveData<Resource<List<PostWithContents>>>
    fun initData(){
        postListData = postRepository.loadPostsWithContents(0)
    }
}