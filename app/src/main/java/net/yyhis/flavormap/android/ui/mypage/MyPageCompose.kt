package net.yyhis.flavormap.android.ui.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
// import com.naver.maps.map.compose.ExperimentalNaverMapApi
import net.yyhis.flavormap.android.ui.search.CardItemList
import net.yyhis.flavormap.android.R
import net.yyhis.flavormap.android.data.model.PostItem
import net.yyhis.flavormap.android.data.model.UserInfo
import net.yyhis.flavormap.android.util.SecureStorage
import net.yyhis.flavormap.android.viewmodel.SessionViewModel
import net.yyhis.flavormap.android.ui.login.LoginPage
import net.yyhis.flavormap.android.viewmodel.MyPageViewModel

@Composable
fun MyPageScreen(sessionViewModel: SessionViewModel) {
    val myPageViewModel: MyPageViewModel = viewModel()
    val myPostList by myPageViewModel.postList.observeAsState(emptyList())

    val userInfo by sessionViewModel.userInfo.observeAsState()

    var context = LocalContext.current
    val secureStorage = SecureStorage(context)

    val sessionValidation by sessionViewModel.sessionValidation.observeAsState(initial = false)

    LaunchedEffect(sessionValidation) {
        if (!sessionValidation) {

        }
        sessionViewModel.checkSession(secureStorage)
        sessionViewModel.getUserInfo(secureStorage.getToken())
        myPageViewModel.fetchMysPost(secureStorage.getToken())
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (secureStorage.isTokenPresent()) {
            var selectedTabIndex by remember { mutableStateOf(0) }
            val tabData = listOf(
                "게시물" to myPostList.size, // 게시물 수
                "팔로워" to 0, // 팔로워 수
                "맛지도" to 0  // 맛지도
            )

            Column(modifier = Modifier.fillMaxSize()) {
                // 프로필 섹션
                ProfileSection(userInfo)

                // 탭 섹션
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    tabData.forEachIndexed { index, pair ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                if(pair.first == "맛지도")
                                    Text(text = pair.first, fontSize = 16.sp, color = Color.Black)
                                else
                                    Text(text = "${pair.first} ${pair.second}", fontSize = 16.sp, color = Color.Black) }
                        )
                    }
                }

                // 탭에 따라 화면 변경
                when (selectedTabIndex) {
                    0 -> MyDiary(myPostList)
                    1 -> MyFollow()
                    2 -> MyMap()
                }
            }
        }
        else {
            LoginPage(context)
        }

    }
}

@Composable
fun ProfileSection(userInfo: UserInfo?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 아바타 이미지
        AsyncImage(
            model = userInfo?.profileUrl ?: "https://dummyimage.com/300x200/000/fff.png&text=Image+Not+Found",
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 닉네임
        Text(
            text = "${userInfo?.userName}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

//        // 프로필 편집 버튼
//        Button(onClick = { /* 프로필 편집 동작 */ }) {
//            Text(text = "프로필 편집")
//        }

        Text(
            text = "${userInfo?.accountName}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MyDiary(myPostList: List<PostItem>) {
    CardItemList(myPostList)
}

@Composable
fun MyFollow() {
    val exampleFollowList = listOf(
        FollowItem("용 감", 564, R.drawable.sample_placeholder),
        FollowItem("감귤감귤감", 25, R.drawable.sample_placeholder),
        FollowItem("한라감귤", 144001, R.drawable.sample_placeholder),
        FollowItem("감제이님", 6400, R.drawable.sample_placeholder)
    )

    // 서버에서 데이터를 가져왔다고 가정
    val followList = remember { mutableStateOf(exampleFollowList) }

    val showDialog = remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(followList.value.size) { index  ->
            FollowListItem(
                followList.value[index],
                onUnfollowClick = { showDialog.value = true }
            )
        }
    }

    if (showDialog.value) {
        AlertConfirmDialog(
            onDismissRequest = { showDialog.value = false },
            onConfirmation = {
                showDialog.value = false
                // fetch update follow
            },
            dialogTitle = "확인 메시지",
            dialogText = "팔로우를 해제",
            icon = Icons.Default.Warning
        )
    }
}

data class FollowItem(
    val nickname: String,
    val followers: Int,
    val avatarResId: Int
)

@Composable
fun FollowListItem(
    item: FollowItem,
    onUnfollowClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 아바타 이미지
        Image(
            painter = painterResource(id = item.avatarResId),
            contentDescription = "Avatar Image",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 닉네임과 팔로워 정보
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.nickname,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "팔로워 ${numberUnitConverter(item.followers)}명",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        // 팔로우 버튼
        Button(
            onClick = onUnfollowClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Text(
                text = "해제",
                color = Color.White
            )
        }
    }
    HorizontalDivider(thickness = 2.dp)
}

@Composable
fun AlertConfirmDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        modifier = Modifier.height(240.dp),
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

fun numberUnitConverter(number: Int): String {
    return when {
        number >= 10_000_000 -> String.format("%.1f", number / 10_000_000.0) + "천만"
        number >= 10_000 -> String.format("%.1f", number / 10_000.0) + "만"
        number >= 1_000 -> String.format("%.1f", number / 1_000.0) + "천"
        else -> number.toString()
    }
}

// @OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MyMap() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Text("MyMap Content")
        // NaverMap(modifier = Modifier.fillMaxSize())
    }
}

@Preview
@Composable
fun MyPageScreenPreview() {
    val sessionViewModel: SessionViewModel = viewModel()
    MyPageScreen(sessionViewModel)
}