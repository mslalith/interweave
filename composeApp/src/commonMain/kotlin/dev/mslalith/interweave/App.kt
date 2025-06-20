package dev.mslalith.interweave

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.mslalith.interweave.core.navigation.HomeScreen
import dev.mslalith.interweave.core.navigation.LocalNavigator
import dev.mslalith.interweave.core.navigation.Navigator
import dev.mslalith.interweave.core.navigation.Screen
import dev.mslalith.interweave.core.navigation.ShimmerHomeScreen
import dev.mslalith.interweave.ui.home.HomeScreenContent
import dev.mslalith.interweave.ui.shimmer.ShimmerHomeScreenContent
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigation(
            initialScreen = HomeScreen
        )
    }
}

@Composable
private fun Navigation(
    initialScreen: Screen
) {
    val navHostController = rememberNavController()
    val navigator = remember(key1 = navHostController) { Navigator(navHostController = navHostController) }

    CompositionLocalProvider(
        LocalNavigator provides navigator,
    ) {
        NavHost(
            navController = navHostController,
            startDestination = initialScreen
        ) {
            composable<HomeScreen> { HomeScreenContent() }
            composable<ShimmerHomeScreen> { ShimmerHomeScreenContent() }
        }
    }
}
