package net.yyhis.flavormap.android.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import net.yyhis.flavormap.android.viewmodel.SessionViewModel


@Preview
@Composable
fun HomeScreenPreview() {
    val sessionViewModel: SessionViewModel = viewModel()
    HomeScreen(sessionViewModel)
}
