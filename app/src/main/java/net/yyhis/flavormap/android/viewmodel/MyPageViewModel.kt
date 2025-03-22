package net.yyhis.flavormap.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.yyhis.flavormap.android.data.model.PostItem
import net.yyhis.flavormap.android.data.repository.PostRepository
import net.yyhis.flavormap.android.util.SecureStorage

class MyPageViewModel : ViewModel() {
    private val postRepository = PostRepository()

    private val _myPostList = MutableLiveData<List<PostItem>>()
    val postList: LiveData<List<PostItem>> get() = _myPostList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchMysPost(token: String?) {
        postRepository.getPosts(
            token,
            null,
            callback = { posts ->
                _myPostList.postValue(posts)
            },
            errorCallback = { error ->
                _errorMessage.postValue(error)
            }
        )
    }


}