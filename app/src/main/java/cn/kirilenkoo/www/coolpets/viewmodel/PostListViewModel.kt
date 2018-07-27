package cn.kirilenkoo.www.coolpets.viewmodel

import android.arch.lifecycle.ViewModel
import cn.kirilenkoo.www.coolpets.repository.PostRepository
import javax.inject.Inject

class PostListViewModel @Inject constructor(postRepository: PostRepository): ViewModel() {

}