package com.kiit.foodfinder.data.repository

import com.kiit.foodfinder.data.db.AppDatabase
import com.kiit.foodfinder.data.db.PreferencesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val MAX_RECENT_SEARCHES = 10
private const val SEPARATOR = "|"

class PreferencesRepository(db: AppDatabase) {

    private val dao = db.preferencesDao()

    // ── Last selected hostel ──────────────────────────────────────────────────

    val lastHostelId: Flow<String?> = dao.observe().map { it?.lastHostelId }

    suspend fun saveLastHostel(hostelId: String) {
        val current = dao.get() ?: PreferencesEntity()
        dao.upsert(current.copy(lastHostelId = hostelId))
    }

    // ── Recent searches ───────────────────────────────────────────────────────

    val recentSearches: Flow<List<String>> = dao.observe().map { prefs ->
        prefs?.recentSearchesRaw
            ?.split(SEPARATOR)
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }

    suspend fun addRecentSearch(query: String) {
        if (query.isBlank()) return
        val current  = dao.get() ?: PreferencesEntity()
        val existing = current.recentSearchesRaw
            .split(SEPARATOR)
            .filter { it.isNotBlank() }
            .toMutableList()

        // Move to top if already present, otherwise prepend
        existing.remove(query)
        existing.add(0, query)

        val trimmed = existing.take(MAX_RECENT_SEARCHES)
        dao.upsert(current.copy(recentSearchesRaw = trimmed.joinToString(SEPARATOR)))
    }

    suspend fun clearRecentSearches() {
        val current = dao.get() ?: PreferencesEntity()
        dao.upsert(current.copy(recentSearchesRaw = ""))
    }
}