package dev.mslalith.interweave.koral

interface SyncableEntity {
    val id: String
    val updatedAt: Long?

    @Transient // This field is for local use only and won't be serialized.
    var syncStatus: SyncStatus?
}