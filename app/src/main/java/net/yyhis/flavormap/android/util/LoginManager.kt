package net.yyhis.flavormap.android.util

import android.content.Context
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import net.yyhis.flavormap.android.data.api.ApiService
import net.yyhis.flavormap.android.data.api.RetrofitClient
import net.yyhis.flavormap.android.viewmodel.SessionViewModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 서버랑 통신해서 토큰 얻기
@Composable
fun loginTestCompose(context: Context, secureStorage: SecureStorage) {
    Button(onClick = { kakaoLogin(context) }) {
        Text("카카오 로그인")
    }
    Button(onClick = { kakaoLogout(context) }) {
        Text("카카오 로그아웃")
    }
    Text(text = secureStorage.get("token") ?: "로그인되지 않았습니다.")
}

fun getTokenByKakao(kakaoToken: String, context: Context) {
    val token = kakaoToken
    val secureStorage = SecureStorage(context)
    val sessionViewModel = SessionViewModel()

    val apiService = RetrofitClient.getRetrofitInstance().create(ApiService::class.java)

    val call = apiService.login("Bearer " + token)

    call.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                try {
                    val responseString = response.body()?.string() ?: "No response body"
                    val jsonObject = JSONObject(responseString);
                    val accessToken = jsonObject.getString("accessToken");

                    Log.i("API_Response", responseString)
                    Log.i("token", accessToken)

                    secureStorage.saveToken(accessToken)
                    sessionViewModel.checkSession(secureStorage)

                    // TODO: refresh
                } catch (e: Exception) {
                    Log.e("API_Error", "Response parsing error: ${e.message}")
                }
            } else {
                // 실패한 응답 처리
                Log.i("API_Error", "Response failed: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            // 요청 실패 처리
            Log.e("API_Error", "Request failed: ${t.message}")
            t.printStackTrace()
        }
    })
}

fun kakaoLogin(context: Context) {
    // val secureStorage = SecureStorage(context)

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("kakao", "로그인 실패", error)
        } else if (token != null) {
            Log.i("kakao_token", token.accessToken)
            getTokenByKakao(token.accessToken, context)
            // secureStorage.save("token", token.accessToken)
            getKakaoUserInfo(token.toString())
        }
    }

    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context = context) { token, error ->
            if (error != null) {
                // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    return@loginWithKakaoTalk
                }
                // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            } else if (token != null) {
                getKakaoUserInfo(token.toString())
            }
        }
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context = context, callback = callback)
    }
}

fun kakaoLogout(context: Context) {
    val secureStorage = SecureStorage(context)
    val sessionViewModel = SessionViewModel()

    UserApiClient.instance.logout { error ->
        if (error != null) {
            Log.e("KakaoLogout", "로그아웃 실패", error)
        }
        else {
            Log.i("KakaoLogout", "로그아웃 성공")
//            secureStorage.clearToken()
            sessionViewModel.clearSession(secureStorage)
        }
    }
}

fun getKakaoUserInfo(accessToken: String) {
    // Log.i("kakao", "token: ${accessToken}")
    // 카카오 사용자 정보를 가져오는 예시 코드
    UserApiClient.instance.me { user, error ->
        if (error != null) {
            Log.e("KakaoLogin", "사용자 정보 가져오기 실패", error)
        } else if (user != null) {
            Log.i("KakaoLogin", "사용자 정보: ${user.id}, ${user.kakaoAccount?.profile?.nickname}")
        }
    }
}