package com.kiit.foodfinder.viewmodel

import androidx.compose.runtime.compositionLocalOf

// CompositionLocal that carries the single FavoritesViewModel instance
// down the entire Compose tree without prop-drilling.
// Access it in any composable with:  val vm = LocalFavoritesViewModel.current
val LocalFavoritesViewModel = compositionLocalOf<FavoritesViewModel> {
    error("FavoritesViewModel not provided — wrap your root composable in LocalFavoritesViewModel.provides(vm)")
}