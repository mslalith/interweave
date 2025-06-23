package dev.mslalith.interweave.koral.datasource.remote

import dev.mslalith.interweave.koral.SyncableEntity

interface RemoteDataSource<T : SyncableEntity> {
    suspend fun getAll(): List<T>
    suspend fun getById(id: String): T?
    suspend fun upsert(item: T): T
    suspend fun delete(id: String)
}