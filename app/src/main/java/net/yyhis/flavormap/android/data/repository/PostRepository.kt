package net.yyhis.flavormap.android.data.repository

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import net.yyhis.flavormap.android.data.model.PostItem
import net.yyhis.flavormap.android.data.api.ApiService
import net.yyhis.flavormap.android.data.api.RetrofitClient
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostRepository {
    private val apiService = RetrofitClient.getRetrofitInstance().create(ApiService::class.java)

    fun getPosts(token: String?, query: String?, callback: (List<PostItem>) -> Unit, errorCallback: (String) -> Unit) {
        val call = apiService.getPosts(token, query)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val responseString = response.body()?.string() ?: "[]"
                        val jsonArray = JSONArray(responseString)
                        val postList = mutableListOf<PostItem>()

                        Log.i("API_Post_Response", responseString)

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val postItem = PostItem(
                                id = jsonObject.getString("id"),
                                title = jsonObject.getString("title"),
                                content = jsonObject.getString("content"),
                                tag = jsonObject.getString("tag"),
                                roadAddress = jsonObject.optString("roadAddress", null),
                                createAt = jsonObject.getString("createAt"),
                                images = jsonObject.getJSONArray("images").let { imgArray ->
                                    (0 until imgArray.length()).map { idx -> imgArray.getString(idx) }
                                },
                                profileUrl = jsonObject.getString("profileUrl"),
                                name = jsonObject.getString("name"),
                                favoriteCount = jsonObject.getInt("favoriteCount")
                            )
                            postList.add(postItem)
                        }

                        callback(postList)

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
