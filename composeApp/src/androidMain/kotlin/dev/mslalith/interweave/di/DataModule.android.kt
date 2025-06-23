package dev.mslalith.interweave.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.mslalith.interweave.data.database.AppDatabase
import org.koin.core.annotation.Single

@Single
actual class DataPlatformProviderFactory(
    private val context: Context
) {
    actual fun createRoomDatabaseBuilder(dbName: String): RoomDatabase.Builder<AppDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(dbName)
        return Room.databaseBuilder<AppDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}