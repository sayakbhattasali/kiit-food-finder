package com.kiit.foodfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kiit.foodfinder.data.repository.FavoriteRepository
import com.kiit.foodfinder.data.repository.PreferencesRepository
import com.kiit.foodfinder.FilterOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoriteRepo: FavoriteRepository,
    private val prefsRepo: PreferencesRepository
) : ViewModel() {

    // ── Search & Results State Persistence ───────────────────────────────────

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow<FilterOption?>(null)
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _scrollIndex = MutableStateFlow(0)
    val scrollIndex = _scrollIndex.asStateFlow()

    private val _scrollOffset = MutableStateFlow(0)
    val scrollOffset = _scrollOffset.asStateFlow()

    fun updateSearchState(query: String, filter: FilterOption, index: Int, offset: Int) {
        _searchQuery.value = query
        _selectedFilter.value = filter
        _scrollIndex.value = index
        _scrollOffset.value = offset
    }

    fun resetSearchState(query: String = "") {
        _searchQuery.value = query
        _selectedFilter.value = null // Reset to default based on hostel context
        _scrollIndex.value = 0
        _scrollOffset.value = 0
    }

    // ── Favorites ─────────────────────────────────────────────────────────────

    // Shared with every screen. Each composable calls isFavorite(id) to read.
    val favoriteIds: StateFlow<Set<Int>> = favoriteRepo.favoriteIds
        .stateIn(
            scope          = viewModelScope,
            started        = SharingStarted.WhileSubscribed(5_000),
            initialValue   = emptySet()
        )

    fun toggleFavorite(storeId: Int) {
        viewModelScope.launch {
            favoriteRepo.toggle(storeId)
        }
    }

    fun isFavorite(storeId: Int): Boolean = favoriteIds.value.contains(storeId)

    // ── Preferences ───────────────────────────────────────────────────────────

    val lastHostelId: StateFlow<String?> = prefsRepo.lastHostelId
        .stateIn(
            scope        = viewModelScope,
            started      = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val recentSearches: StateFlow<List<String>> = prefsRepo.recentSearches
        .stateIn(
            scope        = viewModelScope,
            started      = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun saveLastHostel(hostelId: String) {
        viewModelScope.launch { prefsRepo.saveLastHostel(hostelId) }
    }

    fun addRecentSearch(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch { prefsRepo.addRecentSearch(query) }
    }

    fun clearRecentSearches() {
        viewModelScope.launch { prefsRepo.clearRecentSearches() }
    }
}

// ── Factory ───────────────────────────────────────────────────────────────────
// Needed because our ViewModel takes constructor parameters. This factory is
// instantiated once in MainActivity and provided via CompositionLocal so
// every screen can obtain the same ViewModel instance.
class FavoritesViewModelFactory(
    private val favoriteRepo: FavoriteRepository,
    private val prefsRepo: PreferencesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(favoriteRepo, prefsRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}