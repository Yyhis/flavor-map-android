package net.yyhis.flavormap.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.yyhis.flavormap.android.data.model.PostItem
import androidx.lifecycle.ViewModel
import net.yyhis.flavormap.android.data.repository.PostRepository

class HomeViewModel : ViewModel() {
    private val postRepository = PostRepository()

    private val _postList = MutableLiveData<List<PostItem>>()
    val postList: LiveData<List<PostItem>> get() = _postList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // TODO: 나중에 Today나 Best 쿼리 작성하기 - 서버
    fun fetchPost() {
        postRepository.getPosts(
            null,
            null,
            callback = { posts ->
                _postList.postValue(posts)
            },
            errorCallback = { error ->
                _errorMessage.postValue(error)
            }
        )
    }
}