package net.yyhis.flavormap.android.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import net.yyhis.flavormap.android.data.model.PostItem
import net.yyhis.flavormap.android.viewmodel.HomeViewModel
import net.yyhis.flavormap.android.viewmodel.SessionViewModel
import kotlin.math.log

@Composable
fun HomeScreen(sessionViewModel: SessionViewModel) {
    val homeViewModel: HomeViewModel = viewModel()
    val postList by homeViewModel.postList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        homeViewModel.fetchPost()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            // loginTestCompose(context, secureStorage)
            Text(text = "나만의 맛집 일기", modifier = Modifier.fillMaxWidth().padding(16.dp), fontSize = 26.sp, fontWeight = FontWeight.Normal)
            TabMenu(postList)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabMenu(postList: List<PostItem>) {
    var state by remember { mutableStateOf(0) }
    val titles = listOf("TODAY", "BEST")

    Column(modifier = Modifier.fillMaxWidth()) {
        // TabRow 상단 바
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                titles.forEachIndexed { index, title ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { state = index }
                    ) {
                        Text(
                            text = title,
                            fontSize = 20.sp,
                            fontWeight = if (state == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (state == index) Color.Black else Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (state == index) {
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(2.dp)
                                    .background(Color.Black)
                            )
                        }
                    }
                }
            }
        }

        // 탭에 따라 변경되는 콘텐츠
        HorizontalPagerContainer(state, postList)
    }
}

@Composable
fun HorizontalPagerContainer(state: Int, items: List<PostItem>) {
    val contents = when (state) {
        0 -> {
            val pagerState = rememberPagerState(pageCount = { items.count() })

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(start = 32.dp, end = 32.dp),
                pageSpacing = 12.dp,
            ) { index ->
                val post = items[index]

                val imageUrl = post.images.firstOrNull()?.let {
                    // URL에서 확장자 앞에 .png를 추가
                    if (!it.contains(".png", ignoreCase = true)) {
                        it.substringBefore("&") + ".png" + it.substringAfter("?")
                    } else {
                        it
                    }
                } ?: "https://dummyimage.com/300x200/000/fff.png&text=Image+Not+Found"

                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Post Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
//                Image(
//                    modifier = Modifier
//                        .height(450.dp)
//                        .width(400.dp)
//                        .clip(RoundedCornerShape(16.dp)),
//                    painter = painterResource(id = item.imageResId),
//                    contentDescription = "Best " + item.title,
//                    contentScale = ContentScale.Crop
//                )
            }
            PageIndicator(currentPage = pagerState.currentPage, totalCount = items.size)
        }
        1 -> {
            val pagerState = rememberPagerState(pageCount = { items.count() })

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(start = 32.dp, end = 32.dp),
                pageSpacing = 12.dp,
            ) { index ->
                val item = items[index]

//                Image(
//                    modifier = Modifier
//                        .height(450.dp)
//                        .width(400.dp)
//                        .clip(RoundedCornerShape(16.dp)),
//                    painter = painterResource(id = item.imageResId),
//                    contentDescription = "Best " + item.title,
//                    contentScale = ContentScale.Crop
//                )

            }
            PageIndicator(currentPage = pagerState.currentPage, totalCount = items.size)
        }
        else -> {}
    }
}

@Composable
fun PageIndicator(currentPage: Int, totalCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        // 페이지 표시기 (점)
        repeat(totalCount) { index ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (currentPage == index) Color.Black else Color.Gray)
            )
        }
    }
}
