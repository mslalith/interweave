package dev.mslalith.interweave.di

import org.koin.core.annotation.ComponentScan

@org.koin.core.annotation.Module(
    includes = [
        CoreModule::class,
        DataModule::class,
        DomainModule::class
    ]
)
class AppModule

@org.koin.core.annotation.Module
@ComponentScan("dev.mslalith.interweave.domain")
class DomainModule
