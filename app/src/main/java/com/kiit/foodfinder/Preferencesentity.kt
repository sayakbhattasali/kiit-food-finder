package com.kiit.foodfinder.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Single-row key-value store for lightweight user preferences.
// We use a fixed primary key of 1 so upsert always overwrites the same row.
@Entity(tableName = "preferences")
data class PreferencesEntity(
    @PrimaryKey
    val id: Int = 1,

    @ColumnInfo(name = "last_hostel_id")
    val lastHostelId: String? = null,

    @ColumnInfo(name = "recent_searches")
    // Stored as pipe-separated string, max 10 items. Parsed in the repository.
    val recentSearchesRaw: String = ""
)