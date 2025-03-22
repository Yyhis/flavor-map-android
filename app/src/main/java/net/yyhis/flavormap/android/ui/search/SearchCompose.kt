package net.yyhis.flavormap.android.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImagePainter
import net.yyhis.flavormap.android.data.model.PostItem
import net.yyhis.flavormap.android.viewmodel.SessionViewModel
import net.yyhis.flavormap.android.ui.theme.md_theme_light_background
import net.yyhis.flavormap.android.viewmodel.HomeViewModel
import net.yyhis.flavormap.android.viewmodel.SearchViewModel

@Composable
fun SearchScreen(sessionViewModel: SessionViewModel) {
    val searchViewModel: SearchViewModel = viewModel()
    val searchResult by searchViewModel.postList.observeAsState(emptyList())

    // 검색시로 변경
    LaunchedEffect(Unit) {
        searchViewModel.fetchPostWithQuery("")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            SearchContainer()

            TagButtons(tags = listOf("가벼운", "지역", "추억"))

            CardItemList(searchResult)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContainer() {
    var textFieldState by remember { mutableStateOf(TextFieldValue()) }
    var expanded by remember { mutableStateOf(false) }

    SearchBar(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        inputField = {
            // This is the text field content inside the search bar
            TextField(
                value = textFieldState,
                onValueChange = { textFieldState = it },
                placeholder = { Text("Search") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
            )
        },
        content = {},
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(64.dp),
        colors = SearchBarDefaults.colors(md_theme_light_background), // Optional: Set default colors
        windowInsets = WindowInsets.safeDrawing // Optional: Use window insets for padding adjustments
    )
}

@Composable
fun TagButtons(tags: List<String>) {
    // TODO: material3로 재구현
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 12.dp)
    ) {
        items(tags) { tag ->
            Chip(tag = tag)
        }
    }
}

@Composable
fun Chip(tag: String) {
    OutlinedButton(
        onClick = { /* Handle chip click */ },
        modifier = Modifier.padding(4.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Text(text = tag)
    }
}

@Composable
fun CardItemList(cards: List<PostItem>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // 2개씩
        items(cards.chunked(2)) { cardPair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                // horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                cardPair.forEach { card ->
                    Box (modifier = Modifier.weight(1f)) {
                        CardItem(card)
                    }
                }
            }
        }
    }
}

@Composable
fun CardItem(card: PostItem) {
    var showModal by remember { mutableStateOf(false) }

    // Card Detail
    if (showModal) {
        AlertDialog(
            onDismissRequest = { showModal = false }, // Modal dismiss
            title = {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f)
                        .padding(10.dp),
                ) {
                    Text(
                        text = "Title: ${card.title}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f) // Title takes available space
                    )
                    Text(
                        text = "User: ${card.name}",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            },
            text = {
                Column (modifier = Modifier.verticalScroll(rememberScrollState())) {
                    AsyncImage(
                        model = card.images.firstOrNull(),
                        contentDescription = "Card Image",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        onState = {},
                    )
                    Text(text = card.content)
                }
            },
            confirmButton = {
                TextButton(onClick = { showModal = false }) {
                    Text("Close")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        )
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(onClick = { showModal = true }),
        // shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(Color.Transparent),
    ) {
        Box(
            modifier = Modifier
                .height(300.dp)
        ) {
            // 이미지
            AsyncImage(
                model = card.images.firstOrNull()?: "https://dummyimage.com/300x200/000/fff.png&text=Image+Not+Found",
                contentDescription = "Card Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop,
                onState = { state ->
                    when (state) {
                        is AsyncImagePainter.State.Success -> {
                            // Log.d("img","Image Load Success")
                        }

                        is AsyncImagePainter.State.Error -> {
                            // Log.d("img", "${state.result.throwable?.message}")
                        }

                        is AsyncImagePainter.State.Loading -> {
                            // Log.d("img","Loading Image")
                        }

                        else -> {
                            // Log.d("img","Other State: $state")
                        }
                    }
                },
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .background(
                        color = Color.Transparent
                    )
            ) {
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(30.dp)
                    ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        modifier = Modifier.size(25.dp),
                        contentDescription = "Go Button",
                        tint = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .background(
                        color = Color.Transparent
                    )
            ) {
                Column {
                    Text(
                        text = card.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Text(
                        text = card.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 6.dp),thickness = 1.dp)
    }
}

@Composable
fun HorizontalScrollView(imageUrls: List<String>) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(imageUrls) { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
//            Image(
//                painter = painterResource(id = R.drawable.sample_image),
//                contentDescription = "s",
//                modifier = Modifier.height(240.dp).width(240.dp)
//            )
        }
    }
}

@Composable
fun CircleImage(imageUrl: String) {
//    Image(
//        painter = rememberImagePainter(imageUrl),
//        contentDescription = null,
//        modifier = Modifier
//            .size(50.dp) // Size of the circle
//            .clip(CircleShape) // Make it circular
//            .border(2.dp, Color.Gray, CircleShape) // Optional: Border around the image
//    )
}

@Preview
@Composable
fun PreviewSearchContainer() {
    SearchContainer()
}


