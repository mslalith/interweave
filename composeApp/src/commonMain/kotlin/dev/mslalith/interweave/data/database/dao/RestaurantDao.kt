package dev.mslalith.interweave.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import dev.mslalith.interweave.data.database.entities.RestaurantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM restaurants")
    fun observeAll(): Flow<List<RestaurantEntity>>
}
