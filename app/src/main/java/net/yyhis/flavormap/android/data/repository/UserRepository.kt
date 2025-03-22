package net.yyhis.flavormap.android.data.repository

import android.util.Log
import net.yyhis.flavormap.android.data.api.ApiService
import net.yyhis.flavormap.android.data.api.RetrofitClient
import net.yyhis.flavormap.android.data.model.FollowInfo
import net.yyhis.flavormap.android.data.model.UserInfo
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val apiService = RetrofitClient.getRetrofitInstance().create(ApiService::class.java)

    fun getUserInfo(token: String?, callback: (UserInfo) -> Unit, errorCallback: (String) -> Unit) {
        val call = apiService.getCurrentUser(token)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val responseString = response.body()?.string() ?: "[]"
                        Log.i("API_User_Response", responseString)

                        val jsonObject = JSONObject(responseString)
                        val user = UserInfo(
                            id = jsonObject.getString("id"),
                            profileUrl = jsonObject.getString("profileUrl"),
                            userName = jsonObject.getString("userName"),
                            accountName = jsonObject.getString("accountName")
                        )

                        callback(user)

                    } catch (e: Exception) {
                        errorCallback("Response parsing error: ${e.message}")
                    }
                } else {
                    errorCallback("Response failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                errorCallback("Request failed: ${t.message}")
            }
        })
    }

    fun updateFavorite() {

    }

    // TODO: follow는 follow userId가 아니라 follow한 유저의 간단한 정보만 출력.
    fun getFollow(callback: (List<FollowInfo>) -> Unit, errorCallback: (String) -> Unit) {
        val call = apiService.getPosts(null, null)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val responseString = response.body()?.string() ?: "[]"
                        val jsonArray = JSONArray(responseString)
                        val followInfo = mutableListOf<FollowInfo>()

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val follow = FollowInfo(
                                followUserId = jsonObject.getString("id"),
                                followProfileUrl = jsonObject.getString("profileUrl"),
                                followUserName = jsonObject.getString("name"),
                                followCount = jsonObject.getInt("followCount")
                            )
                            followInfo.add(follow)
                        }

                        callback(followInfo)

                    } catch (e: Exception) {
                        errorCallback("Response parsing error: ${e.message}")
                    }
                } else {
                    errorCallback("Response failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                errorCallback("Request failed: ${t.message}")
            }
        })
    }
}