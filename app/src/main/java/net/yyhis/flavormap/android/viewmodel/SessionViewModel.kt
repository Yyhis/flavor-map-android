package net.yyhis.flavormap.android.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.yyhis.flavormap.android.data.model.PostItem
import net.yyhis.flavormap.android.data.model.UserInfo
import net.yyhis.flavormap.android.data.repository.UserRepository
import net.yyhis.flavormap.android.util.SecureStorage
import org.json.JSONObject
import java.util.Base64

class SessionViewModel: ViewModel() {
    private val userRepository = UserRepository()

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> get() = _userInfo

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // 로그인 상태 관리
    private val _sessionValidation = MutableLiveData<Boolean>()
    val sessionValidation: LiveData<Boolean> get() = _sessionValidation

    // 토큰 상태 확인 및 변경
    fun checkSession(secureStorage: SecureStorage) {
        val token = secureStorage.getToken()
        if (token != null && !isTokenExpired(token)) {
            Log.i("SessionViewModel", "Token is valid")
            _sessionValidation.value = true
        } else {
            _sessionValidation.value = false
        }
    }

    // 세션 로그아웃 처리
    fun clearSession(secureStorage: SecureStorage) {
        secureStorage.clearToken()
        _sessionValidation.value = false
    }

    // 토큰 만료 여부 확인
    private fun isTokenExpired(token: String): Boolean {
        try {
            // JWT 토큰은 "header.payload.signature" 형태로 되어있으므로, "."으로 분리합니다.
            val parts = token.split(".")
            if (parts.size == 3) {
                // payload 부분을 Base64로 디코딩하여 JSON 객체로 변환
                val payload = String(Base64.getDecoder().decode(parts[1]))
                val jsonObject = JSONObject(payload)

                // exp 클레임을 가져와 현재 시간과 비교
                val expTime = jsonObject.optLong("exp", 0)
                // 만료된 경우 true 반환
                return expTime < System.currentTimeMillis() / 1000
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true // 예외가 발생한 경우 만료된 것으로 간주
    }

    fun getUserInfo(token: String?) {
        Log.d("SessionViewModel", "getUserInfo() called with token: $token")
        userRepository.getUserInfo(
            "Bearer $token",
            callback = { user ->
                Log.d("SessionViewModel", "User info received: $user")
                _userInfo.postValue(user)
            },
            errorCallback = { error ->
                Log.e("SessionViewModel", "Error fetching user info: $error")
                _errorMessage.postValue(error)
            }
        )
    }
}