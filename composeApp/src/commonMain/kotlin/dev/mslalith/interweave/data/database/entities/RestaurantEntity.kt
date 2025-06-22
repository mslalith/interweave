package dev.mslalith.interweave.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mslalith.interweave.koral.SyncableEntity
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class RestaurantEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "cuisine")
    val cuisine: String?,
    @ColumnInfo(name = "locality")
    val locality: String?,
    @ColumnInfo(name = "rating")
    val rating: Double?
) : SyncableEntity
