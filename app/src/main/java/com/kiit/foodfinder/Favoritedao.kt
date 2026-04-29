package com.kiit.foodfinder.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    // Observe the full favorites list as a reactive Flow.
    // Emits a new list whenever the table changes — the ViewModel
    // converts this into StateFlow for Compose to collect.
    @Query("SELECT * FROM favorites ORDER BY saved_at DESC")
    fun observeAll(): Flow<List<FavoriteEntity>>

    // One-shot read — used to seed initial state without collecting a flow.
    @Query("SELECT store_id FROM favorites")
    suspend fun getAllIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE store_id = :storeId")
    suspend fun deleteById(storeId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE store_id = :storeId)")
    suspend fun isFavorite(storeId: Int): Boolean
}