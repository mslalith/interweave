package dev.mslalith.interweave.di

import androidx.room.Room
import androidx.room.RoomDatabase
import dev.mslalith.interweave.data.database.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.annotation.Single
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@Single
actual class DataPlatformProviderFactory {
    actual fun createRoomDatabaseBuilder(dbName: String): RoomDatabase.Builder<AppDatabase> {
        val dbFilePath = documentDirectory() + "/$dbName"
        return Room.databaseBuilder<AppDatabase>(
            name = dbFilePath,
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory?.path)
    }
}