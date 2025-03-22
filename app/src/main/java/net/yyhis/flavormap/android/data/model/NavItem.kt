package net.yyhis.flavormap.android.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val navItems = listOf(
    NavItem("Home", Icons.Filled.Home, "home"),
    NavItem("Search", Icons.Filled.Search, "search"),
    NavItem("Profile", Icons.Filled.Person, "profile")
)

enum class NavRoute {
    Home,
    Login,
    Register_id,
    Register_pw
}