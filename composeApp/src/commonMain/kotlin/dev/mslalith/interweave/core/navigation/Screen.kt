package dev.mslalith.interweave.core.navigation

import kotlinx.serialization.Serializable

sealed interface Screen

@Serializable
data object HomeScreen : Screen

@Serializable
data object ShimmerHomeScreen : Screen
