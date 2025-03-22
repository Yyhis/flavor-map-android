package net.yyhis.flavormap.android.data.model

import android.util.Log
import androidx.annotation.DrawableRes
import net.yyhis.flavormap.android.R
import net.yyhis.flavormap.android.data.api.ApiService
import net.yyhis.flavormap.android.data.api.RetrofitClient
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class PostItem(
    val id: String,
    val title: String,
    val content: String,
    val tag: String,
    val roadAddress: String?,
    val createAt: String,
    val images: List<String>,
    val profileUrl: String,
    val name: String,
    val favoriteCount: Int
)
