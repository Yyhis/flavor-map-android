package net.yyhis.flavormap.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kakao.sdk.common.KakaoSdk
// import com.naver.maps.map.NaverMapSdk
import net.yyhis.flavormap.android.BuildConfig.kakao_native_app_key
import net.yyhis.flavormap.android.BuildConfig.naver_map_sdk_key
import net.yyhis.flavormap.android.ui.theme.MyApplicationTheme
import net.yyhis.flavormap.android.ui.theme.*
import net.yyhis.flavormap.android.ui.common.MainScreen
import net.yyhis.flavormap.android.viewmodel.SessionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 카카오 SDK 초기화
        KakaoSdk.init(this, kakao_native_app_key)

        // NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(naver_map_sdk_key)

        setContent {
            val sessionViewModel = viewModel<SessionViewModel>()
            MyApplicationTheme {
                MainScreen(sessionViewModel)
            }
        }
    }
}





