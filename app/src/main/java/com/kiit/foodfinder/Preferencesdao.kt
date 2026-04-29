package com.kiit.foodfinder.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferencesDao {

    @Query("SELECT * FROM preferences WHERE id = 1")
    fun observe(): Flow<PreferencesEntity?>

    @Query("SELECT * FROM preferences WHERE id = 1")
    suspend fun get(): PreferencesEntity?

    // REPLACE strategy means this acts as upsert — creates the row on
    // first call, overwrites on every subsequent call.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(prefs: PreferencesEntity)
}