package cn.kirilenkoo.www.coolpets.viewmodel

import androidx.lifecycle.ViewModel;
import cn.kirilenkoo.www.coolpets.repository.PostRepository
import javax.inject.Inject

class PostDetailViewModel @Inject constructor(val postRepository: PostRepository): ViewModel() {
    // TODO: Implement the ViewModel
}
