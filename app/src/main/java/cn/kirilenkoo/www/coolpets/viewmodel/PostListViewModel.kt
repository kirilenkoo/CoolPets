package cn.kirilenkoo.www.coolpets.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cn.kirilenkoo.www.coolpets.model.PostWithContents
import cn.kirilenkoo.www.coolpets.repository.PostRepository
import cn.kirilenkoo.www.coolpets.util.AbsentLiveData
import com.android.example.github.vo.Resource
import javax.inject.Inject

class PostListViewModel @Inject constructor(val postRepository: PostRepository): ViewModel() {
    private val _page: MutableLiveData<Int> = MutableLiveData()
    val postListData:LiveData<Resource<List<PostWithContents>>> = Transformations.switchMap(_page){
        it -> if(it>0) postRepository.loadPostsWithContents(it)
                else AbsentLiveData.create()
    }
    fun initData(p: Int){
        if(p == _page.value) return
        else _page.value = p
    }
}