package dev.mslalith.interweave.koral.datasource.remote

import dev.mslalith.interweave.koral.SyncableEntity
import dev.mslalith.interweave.koral.utils.KoralResult

interface RemoteDataSource<T : SyncableEntity> {
    suspend fun getAll(): KoralResult<List<T>>
    suspend fun getById(id: String): KoralResult<T?>
    suspend fun upsert(item: T): KoralResult<T>
    suspend fun delete(id: String): KoralResult<Unit>
}