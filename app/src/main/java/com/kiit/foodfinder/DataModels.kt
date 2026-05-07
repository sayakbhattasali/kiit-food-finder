package com.kiit.foodfinder

// Represents a hostel location at KIIT University
data class Hostel(
    val id: String,
    val displayName: String,
    val latitude: Double,
    val longitude: Double
)

// Category tag for each food store
enum class FoodCategory(val label: String, val emoji: String) {
    ROLLS("Rolls", "🌯"),
    BURGER("Burger", "🍔"),
    CHINESE("Chinese", "🍜"),
    SNACKS("Snacks", "🍟"),
    CAFE("Cafe", "☕"),
    DESSERT("Dessert", "🍰"),
    SOUTH_INDIAN("South Indian", "🥞"),
    MULTI_CUISINE("Multi Cuisine", "🍽️"),
    BIRYANI("Biryani", "🍛"),
    PIZZA("Pizza", "🍕"),
    MOMOS("Momos", "🥟"),
    JUICE("Juice", "🥤"),
    THALI("Thali", "🍱"),
    MAGGI("Maggi", "🍜")
}

// Price tier indicator
enum class PriceRange(val label: String) {
    BUDGET("₹"),
    MODERATE("₹₹"),
    PREMIUM("₹₹₹")
}

// ── Image source for a store ─────────────────────────────────────────────────
// Use DrawableRes for bundled assets, or a URL string for remote images.
// When null the detail screen renders a branded gradient placeholder.
sealed class StoreImage {
    data class Res(val resId: Int) : StoreImage()
    data class Url(val url: String) : StoreImage()
    object None : StoreImage()
}

// A single food store near the hostel
data class FoodStore(
    val id: Int,
    val name: String,
    val distanceMeters: Int,
    val rating: Float,
    val priceRange: PriceRange,
    val costForOne: Int,
    val category: FoodCategory,
    val reviewCount: Int,
    val speciality: String,
    val latitude: Double,
    val longitude: Double,
    val mapsLink: String,
    val openingHour: Int,
    val openingMinute: Int,
    val closingHour: Int,
    val closingMinute: Int,
    // Image field — assign StoreImage.Res(R.drawable.store_xyz) per store,
    // or leave as StoreImage.None to show the gradient placeholder.
    val image: StoreImage = StoreImage.None
)

// Filter options shown on the Results screen
enum class FilterOption(val label: String) {
    NEAREST("Nearest"),
    CHEAPEST("Cheapest"),
    BEST_RATED("Best Rated"),
    OPEN_NOW("Open Now"),
    LATE_NIGHT("Late Night")
}

// ── Helpers ──────────────────────────────────────────────────────────────────
fun FoodStore.formattedDistance(): String =
    if (distanceMeters < 1000) "~${distanceMeters} m"
    else String.format("~%.1f km", distanceMeters / 1000.0)

fun FoodStore.formattedDistanceAndTime(): String {
    val distStr = formattedDistance()
    // 4.5 km/h = 75 meters/minute
    val walkingMinutes = (distanceMeters / 75).coerceAtLeast(1)
    return "$distStr • $walkingMinutes min"
}

fun isStoreOpen(store: FoodStore): Boolean {
    val cal = java.util.Calendar.getInstance()
    val now = cal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + cal.get(java.util.Calendar.MINUTE)
    val open  = store.openingHour  * 60 + store.openingMinute
    val close = store.closingHour  * 60 + store.closingMinute
    return if (close > open) now in open until close
    else now >= open || now < close
}