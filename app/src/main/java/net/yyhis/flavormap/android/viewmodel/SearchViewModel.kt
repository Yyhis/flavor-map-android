package net.yyhis.flavormap.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.yyhis.flavormap.android.data.model.PostItem
import net.yyhis.flavormap.android.data.repository.PostRepository

class SearchViewModel : ViewModel() {
    private val postRepository = PostRepository()

    private val _postList = MutableLiveData<List<PostItem>>()
    val postList: LiveData<List<PostItem>> get() = _postList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchPostWithQuery(query: String) {
        postRepository.getPosts(
            null,
            query,
            callback = { posts ->
                _postList.postValue(posts)
            },
            errorCallback = { error ->
                _errorMessage.postValue(error)
            }
        )
    }
}