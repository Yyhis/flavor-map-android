package net.yyhis.flavormap.android.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
fun TopAppBarPreview() {
    TopBar()
}

@Preview
@Composable
fun NavigationBarPreview() {
    val navController = rememberNavController()
    BottomNavBar(
        navController
    )
}

@Preview
@Composable
fun NotificationDialogPreview() {
    var showNotifiyDialog by remember { mutableStateOf(true) }

    if (showNotifiyDialog) {
        NotificationDialog(
            onDismiss = { showNotifiyDialog = false }
        )
    }
}