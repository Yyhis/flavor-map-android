package net.yyhis.flavormap.android.data.api

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @POST("v1/auth/login")
    fun login(
        @Header("Authorization") authorization: String,
        // @Body requestBody: RequestBody
    ): Call<ResponseBody>

    @GET("v1/auth/user")
    fun getCurrentUser(
        @Header("Authorization") authorization: String?,
    ): Call<ResponseBody>

    @GET("v1/post")
    fun getPosts(
        @Header("Authorization") authorization: String?,
        @Query("query") query: String?,
    ) : Call<ResponseBody>

}

object RetrofitClient {

    private var retrofit: Retrofit? = null

    fun getRetrofitInstance(): Retrofit {
        if (retrofit == null) {
            val okHttpClient = OkHttpClient.Builder().build()

            retrofit = Retrofit.Builder()
                .baseUrl("https://server.yyhis.net:8899/")  // Base URL 설정
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())  // Gson 변환기 설정
                .build()
        }
        return retrofit!!
    }
}