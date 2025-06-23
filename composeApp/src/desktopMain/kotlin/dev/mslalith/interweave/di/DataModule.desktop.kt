package dev.mslalith.interweave.di

import androidx.room.Room
import androidx.room.RoomDatabase
import dev.mslalith.interweave.data.database.AppDatabase
import org.koin.core.annotation.Single
import java.io.File

@Single
actual class DataPlatformProviderFactory {
    actual fun createRoomDatabaseBuilder(dbName: String): RoomDatabase.Builder<AppDatabase> {
        val dbFile = File(System.getProperty("java.io.tmpdir"), dbName)
        return Room.databaseBuilder<AppDatabase>(
            name = dbFile.absolutePath,
        )
    }
}