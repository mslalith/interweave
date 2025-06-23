package dev.mslalith.interweave.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.mslalith.interweave.BuildKonfig
import dev.mslalith.interweave.data.database.AppDatabase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [
        DataPlatformModule::class
    ]
)
@ComponentScan("dev.mslalith.interweave.data")
class DataModule {

    @Single
    fun provideSupabaseClient(): SupabaseClient = createSupabaseClient(
        supabaseUrl = BuildKonfig.SUPABASE_URL,
        supabaseKey = BuildKonfig.SUPABASE_KEY
    ) {}

    @Single
    fun provideAppDatabase(
        builder: RoomDatabase.Builder<AppDatabase>
    ): AppDatabase = builder
        .addMigrations()
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

expect class DataPlatformProviderFactory {
    fun createRoomDatabaseBuilder(dbName: String): RoomDatabase.Builder<AppDatabase>
}

@Module
class DataPlatformModule {

    @Single
    fun provideDataPlatformProviderFactory(
        dataPlatformProviderFactory: DataPlatformProviderFactory
    ): RoomDatabase.Builder<AppDatabase> = dataPlatformProviderFactory.createRoomDatabaseBuilder(dbName = "interweave_room.db")
}

