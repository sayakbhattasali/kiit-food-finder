package com.kiit.foodfinder.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Design System Registry: Centralized icon system to replace string emojis.
 * Maps semantic UI concepts to specific Material Icons.
 */
sealed class FoodFinderIcon(val imageVector: ImageVector) {
    // Nav & General
    object Home : FoodFinderIcon(Icons.Default.Home)
    object Search : FoodFinderIcon(Icons.Default.Search)
    object Favorite : FoodFinderIcon(Icons.Default.Favorite)
    object FavoriteBorder : FoodFinderIcon(Icons.Default.FavoriteBorder)
    
    // Semantic Concepts (Mapping from Emojis)
    object Hostel : FoodFinderIcon(Icons.Default.HomeWork)         // 🏠 -> HomeWork
    object Store : FoodFinderIcon(Icons.Default.Storefront)       // 🏪 -> Storefront
    object Location : FoodFinderIcon(Icons.Default.LocationOn)    // 📍 -> LocationOn
    object Rating : FoodFinderIcon(Icons.Default.Star)            // ⭐ -> Star
    object Time : FoodFinderIcon(Icons.Default.Schedule)          // 🕐 -> Schedule
    object Category : FoodFinderIcon(Icons.Default.Label)         // 🏷️ -> Label
    object Price : FoodFinderIcon(Icons.Default.Payments)         // 💵 -> Payments
    object Distance : FoodFinderIcon(Icons.AutoMirrored.Filled.DirectionsWalk) // 🚶 -> DirectionsWalk
    object Night : FoodFinderIcon(Icons.Default.NightsStay)       // 🌙 -> NightsStay
    object Open : FoodFinderIcon(Icons.Default.Circle)            // 🟢 -> Circle
    object Trophy : FoodFinderIcon(Icons.Default.EmojiEvents)     // 🏆 -> EmojiEvents
    object Empty : FoodFinderIcon(Icons.Default.SearchOff)        // 🔍 / 🍱 -> SearchOff
    object Rocket : FoodFinderIcon(Icons.Default.RocketLaunch)    // 🚀 -> RocketLaunch
    
    // Food Categories
    object Pizza : FoodFinderIcon(Icons.Default.LocalPizza)
    object Burger : FoodFinderIcon(Icons.Default.LunchDining)
    object Chinese : FoodFinderIcon(Icons.Default.RamenDining)
    object Snacks : FoodFinderIcon(Icons.Default.BakeryDining)
    object Cafe : FoodFinderIcon(Icons.Default.LocalCafe)
    object Dessert : FoodFinderIcon(Icons.Default.Icecream)
    object SouthIndian : FoodFinderIcon(Icons.Default.BreakfastDining)
    object MultiCuisine : FoodFinderIcon(Icons.Default.Restaurant)
    object Biryani : FoodFinderIcon(Icons.Default.DinnerDining)
    object Momos : FoodFinderIcon(Icons.Default.SoupKitchen)
    object Juice : FoodFinderIcon(Icons.Default.LocalDrink)
    object Thali : FoodFinderIcon(Icons.Default.TakeoutDining)
    object Maggi : FoodFinderIcon(Icons.Default.RiceBowl)
    object Rolls : FoodFinderIcon(Icons.Default.KebabDining)
    
    // Actions
    object Back : FoodFinderIcon(Icons.AutoMirrored.Filled.ArrowBack)
    object Close : FoodFinderIcon(Icons.Default.Close)
    object Expand : FoodFinderIcon(Icons.Default.KeyboardArrowDown)
    object Navigate : FoodFinderIcon(Icons.Default.Navigation)
}

/**
 * Unified Component Implementation: Reusable Composable for rendering [FoodFinderIcon].
 */
@Composable
fun AppIcon(
    icon: FoodFinderIcon,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    contentDescription: String? = null
) {
    Icon(
        imageVector = icon.imageVector,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint
    )
}
