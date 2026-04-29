package com.kiit.foodfinder.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    @ColumnInfo(name = "store_id")
    val storeId: Int,

    @ColumnInfo(name = "saved_at")
    val savedAt: Long = System.currentTimeMillis()
)