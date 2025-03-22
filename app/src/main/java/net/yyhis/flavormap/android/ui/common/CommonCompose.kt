package net.yyhis.flavormap.android.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import net.yyhis.flavormap.android.data.model.navItems
import net.yyhis.flavormap.android.ui.home.HomeScreen
import net.yyhis.flavormap.android.ui.mypage.MyPageScreen
import net.yyhis.flavormap.android.ui.search.SearchScreen
import net.yyhis.flavormap.android.ui.theme.md_theme_light_background
import net.yyhis.flavormap.android.ui.theme.md_theme_light_primary
import net.yyhis.flavormap.android.util.SecureStorage
import net.yyhis.flavormap.android.viewmodel.SessionViewModel
import net.yyhis.flavormap.android.util.kakaoLogout


@Composable
fun MainScreen(sessionViewModel: SessionViewModel) {
    val navController = rememberNavController()

    val context = LocalContext.current
    val secureStorage = SecureStorage(context)

    LaunchedEffect(Unit) {
        sessionViewModel.checkSession(secureStorage)
    }

    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomNavBar(
                navController
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(sessionViewModel) }
            composable("search") { SearchScreen(sessionViewModel) }
            composable("profile") { MyPageScreen(sessionViewModel) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    val context = LocalContext.current

    var itemCount by remember { mutableStateOf(0) }
    var showNotifiyDialog by remember { mutableStateOf(false) }
    var showSettingModal by remember { mutableStateOf(false) }

    TopAppBar(
        title = { },
        actions = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BadgedBox(
                    badge = {
                        if (itemCount > 0) {
                            Badge(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ) {
                                Text("$itemCount")
                            }
                        }
                    }
                ) {
                    IconButton(onClick = { showNotifiyDialog = true }) {
                        Icon(
                            imageVector = if(itemCount > 0) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                            contentDescription = "Notification Button",
                        )
                    }
                }
            }
            IconButton(onClick = { kakaoLogout(context) }) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Setting Button"
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = md_theme_light_background
        )
    )
    if (showNotifiyDialog) {
        NotificationDialog(
            onDismiss = { showNotifiyDialog = false }
        )
    }
    if (showSettingModal) {
        SettingModal(
            onDismiss = { showSettingModal = false }
        )
    }
}

@Composable
fun BottomNavBar(navController: NavController) {

    NavigationBar (
        containerColor = md_theme_light_background
    ){
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = md_theme_light_primary
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("알림") },
                        navigationIcon = {
                            IconButton(onClick = { onDismiss() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "뒤로가기"
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopStart
                ) {
                    NotificationContainer()
                }
            }
        }
    }
}

@Composable
fun NotificationContainer() {
    // 알림 박스 추가하기 - https://wwit.design/pattern/notification
    Text(
        text = "여기에 알림 내용을 추가하세요.",
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun SettingModal(
    onDismiss: () -> Unit
) {
    // modal
}

@Composable
fun SettingContainer() {

}
