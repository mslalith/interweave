package dev.mslalith.interweave.core.base

import kotlinx.coroutines.CoroutineDispatcher

data class AppCoroutineDispatchers(
    val io: CoroutineDispatcher,
    val databaseWrite: CoroutineDispatcher,
    val databaseRead: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val main: CoroutineDispatcher,
)