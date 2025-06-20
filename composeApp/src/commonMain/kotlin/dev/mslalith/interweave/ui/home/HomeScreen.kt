package dev.mslalith.interweave.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.mslalith.interweave.core.navigation.LocalNavigator
import dev.mslalith.interweave.core.navigation.Screen
import dev.mslalith.interweave.core.navigation.ShimmerHomeScreen

@Composable
fun HomeScreenContent(
) {
    val navigator = LocalNavigator.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        items(
            items = homeItems,
            key = { it.title }
        ) {
            Button(
                onClick = { navigator.goTo(it.screen) }
            ) {
                Text(it.title)
            }
        }
    }
}

private data class HomeScreenListItem(
    val title: String,
    val screen: Screen
)

private val homeItems = listOf(
    HomeScreenListItem(
        title = "Shimmer Home",
        screen = ShimmerHomeScreen
    )
)
