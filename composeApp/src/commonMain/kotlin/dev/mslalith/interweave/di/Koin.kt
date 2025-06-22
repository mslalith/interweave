package dev.mslalith.interweave.di

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Single

@org.koin.core.annotation.Module(
    includes = [
        CoreModule::class,
        DataModule::class,
        DomainModule::class
    ]
)
class AppModule

@org.koin.core.annotation.Module
@ComponentScan("dev.mslalith.interweave.data")
class DataModule

@org.koin.core.annotation.Module
@ComponentScan("dev.mslalith.interweave.domain")
class DomainModule
