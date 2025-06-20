package dev.mslalith.interweave.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

val LocalNavigator = staticCompositionLocalOf<Navigator> { error("${Navigator::class.simpleName} not provided") }

class Navigator(
    private val navHostController: NavHostController
) {
    fun goTo(screen: Screen) {
        navHostController.navigate(screen)
    }

    fun pop() {
        navHostController.popBackStack()
    }
}

inline fun <reified T: Screen> NavGraphBuilder.routeScreen(
    crossinline content: @Composable (T) -> Unit
) {
    composable<T> { backStackEntry ->
        val route: T = backStackEntry.toRoute()
        content(route)
    }
}