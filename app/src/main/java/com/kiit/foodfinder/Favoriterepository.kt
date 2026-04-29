package com.kiit.foodfinder.data.repository

import com.kiit.foodfinder.data.db.AppDatabase
import com.kiit.foodfinder.data.db.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepository(db: AppDatabase) {

    private val dao = db.favoriteDao()

    // Reactive set of favorited store IDs. The ViewModel collects this
    // as a StateFlow so every composable can observe changes instantly.
    val favoriteIds: Flow<Set<Int>> = dao.observeAll()
        .map { entities -> entities.map { it.storeId }.toSet() }

    suspend fun toggle(storeId: Int) {
        if (dao.isFavorite(storeId)) {
            dao.deleteById(storeId)
        } else {
            dao.insert(FavoriteEntity(storeId = storeId))
        }
    }

    suspend fun isFavorite(storeId: Int): Boolean = dao.isFavorite(storeId)
}