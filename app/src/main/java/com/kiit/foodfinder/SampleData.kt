package com.kiit.foodfinder
import kotlin.math.*
// ─────────────────────────────────────────
//  All hostels available in the dropdown
// ─────────────────────────────────────────
val ALL_HOSTELS = listOf(
    Hostel("kp2", "KP 2", 20.3546856825232, 85.8189894790152),
    Hostel("kp5", "KP 5", 20.357063273674118, 85.82010926552181),
    Hostel("kp6", "KP 6", 20.349060303708132, 85.81615220969914),
    Hostel("kp7", "KP 7", 20.351223559466384, 85.81633709435714),
    Hostel("kp25", "KP 25", 20.36260234893656, 85.81566639435748),
    Hostel("qc4", "QC 4", 20.352814574950983, 85.81827569435721),
    Hostel("qc5", "QC 5", 20.348670807792217, 85.82085820969911)
)

// ─────────────────────────────────────────
//  Food stores per hostel
//  Each list is pre-sorted nearest → farthest
// ─────────────────────────────────────────
val ALL_STORES = listOf(
    FoodStore(
        1,
        "Zingiber",
        0,
        4.5f,
        PriceRange.MODERATE,
        FoodCategory.SOUTH_INDIAN,
        3610,
        "South Indian & Meals",
        20.354573372674484,
        85.82400725171824,
        "https://maps.app.goo.gl/mgWKbJecTozjd7su7",
        7, 0, 23, 0,
        StoreImage.Res(R.drawable.zingiber)
    ),

    FoodStore(
        2,
        "Biggies Burger",
        0,
        4.5f,
        PriceRange.MODERATE,
        FoodCategory.BURGER,
        13400,
        "Burgers & Fries",
        20.35327788408302,
        85.82470207901515,
        "https://maps.app.goo.gl/1p5tYfU3wGGQFoQx9",
        7, 0, 3, 0,
        StoreImage.Res(R.drawable.biggies)
    ),

    FoodStore(
        3,
        "KFC",
        0,
        4.3f,
        PriceRange.PREMIUM,
        FoodCategory.BURGER,
        5200,
        "Chicken Buckets",
        20.35410820867504,
        85.81738996410742,
        "https://maps.app.goo.gl/2At1JvkXhPzTrvga6",
        11, 0, 23, 0,
        StoreImage.Res(R.drawable.kfc)
    ),

    FoodStore(
        4,
        "Domino's",
        0,
        4.4f,
        PriceRange.PREMIUM,
        FoodCategory.PIZZA,
        7800,
        "Pizza & Garlic Bread",
        20.354163909620617,
        85.82453453118089,
        "https://maps.app.goo.gl/pQv8W22hmsVZu1fe6",
        11, 0, 3, 0,
        StoreImage.Res(R.drawable.dominos)
    ),

    FoodStore(
        5,
        "Wow! Momo",
        0,
        4.3f,
        PriceRange.MODERATE,
        FoodCategory.MOMOS,
        4100,
        "Steamed & Fried Momos",
        20.353448147935794,
        85.82453319435717,
        "https://maps.app.goo.gl/pQVdCMwuzfYPThko9",
        11, 0, 23, 0,
        StoreImage.Res(R.drawable.wowmomo)
    ),

    FoodStore(
        6,
        "JUGAAD JN",
        0,
        4.2f,
        PriceRange.MODERATE,
        FoodCategory.MULTI_CUISINE,
        4177,
        "Street Food & Meals",
        20.354226783784025,
        85.82234065847062,
        "https://maps.app.goo.gl/WMwvDCYErNWJ66pf9",
        10, 30, 22, 0,
        StoreImage.Res(R.drawable.jugaadjn)
    ),

    FoodStore(
        7,
        "Patiala House",
        0,
        4.3f,
        PriceRange.MODERATE,
        FoodCategory.MULTI_CUISINE,
        2680,
        "Punjabi Meals",
        20.35335093003019,
        85.82449699435719,
        "https://maps.app.goo.gl/P2pkgu78eiREpoKw5",
        8, 30, 22, 30,
        StoreImage.Res(R.drawable.patiala)
    ),

    FoodStore(
        8,
        "Banker Roll Wala",
        0,
        4.6f,
        PriceRange.BUDGET,
        FoodCategory.ROLLS,
        1890,
        "Egg & Chicken Rolls",
        20.35456775968687,
        85.82674218176264,
        "https://maps.app.goo.gl/e1dKEEwJbGVkc9WUA",
        17, 0, 23, 0,
        StoreImage.Res(R.drawable.bankerroll)
    ),

    FoodStore(
        9,
        "KIIT Food Court 7",
        0,
        4.1f,
        PriceRange.BUDGET,
        FoodCategory.SNACKS,
        950,
        "Campus Food Court",
        20.349345447697853,
        85.81562757901509,
        "https://maps.app.goo.gl/Cd4uPiGkeUqhN8wLA",
        9, 30, 20, 15,
        StoreImage.Res(R.drawable.foodcourt7)
    ),

    FoodStore(
        10,
        "99 North",
        0,
        4.4f,
        PriceRange.MODERATE,
        FoodCategory.MULTI_CUISINE,
        2140,
        "Indian & Biryani",
        20.343731794657085,
        85.80879512319241,
        "https://maps.app.goo.gl/Sbz6XcBcrT9frxqWA",
        11, 30, 23, 30,
        StoreImage.Res(R.drawable.north99)
    ),

    FoodStore(
        11,
        "Khati Time",
        0,
        4.5f,
        PriceRange.BUDGET,
        FoodCategory.CAFE,
        1180,
        "Open Air Cafe & Snacks",
        20.34665886249568,
        85.82247524012836,
        "https://maps.app.goo.gl/qBoFbtMYeU1xq5ua7",
        15, 0, 23, 0,
        StoreImage.Res(R.drawable.khatitime)
    ),

    FoodStore(
        12,
        "Prabhuji Pure Food",
        0,
        3.1f,
        PriceRange.MODERATE,
        FoodCategory.DESSERT,
        2760,
        "Sweets, Namkeen & Desserts",
        20.3559730977623,
        85.82722455240386,
        "https://maps.app.goo.gl/G6Lnn34qNUM71fwz9",
        9, 0, 22, 30,
        StoreImage.Res(R.drawable.prabhuji)
    ),

    FoodStore(
        13,
        "The Belgian Waffle Co",
        0,
        4.4f,
        PriceRange.PREMIUM,
        FoodCategory.DESSERT,
        1980,
        "Waffles & Desserts",
        20.352868188598507,
        85.82457374377236,
        "https://maps.app.goo.gl/uVpvqcL3VYYKv8Le8",
        11, 0, 2, 0,
        StoreImage.Res(R.drawable.belgianwaffle)
    ),

    FoodStore(
        14,
        "Khabar Kolkata",
        0,
        3.8f,
        PriceRange.BUDGET,
        FoodCategory.MULTI_CUISINE,
        1430,
        "Bengali Meals & Specials",
        20.355458122398225,
        85.81920958077112,
        "https://maps.app.goo.gl/Dxxf9kYa8SMgSLm7A",
        4, 0, 22, 0,
        StoreImage.Res(R.drawable.khabarkolkata)
    ),

    FoodStore(
        15,
        "Chennapatnam Filter Coffee",
        0,
        4.6f,
        PriceRange.BUDGET,
        FoodCategory.CAFE,
        910,
        "Coffee, Tea & South Indian Snacks",
        20.353698750245837,
        85.82131299476507,
        "https://maps.app.goo.gl/uLzaSrZsZBsgMC4G9",
        7, 30, 22, 0,
        StoreImage.Res(R.drawable.chennapatnam)
    ),

    FoodStore(
        16,
        "Biriyani Box",
        0,
        3.9f,
        PriceRange.MODERATE,
        FoodCategory.BIRYANI,
        1720,
        "Chicken & Mutton Biryani",
        20.35343805606943,
        85.82230427847753,
        "https://maps.app.goo.gl/2zBvE1XpjS54kL2v8",
        11, 0, 23, 30,
        StoreImage.Res(R.drawable.biriyanibox)
    ),

    FoodStore(
        17,
        "Delhi Chaat",
        0,
        4.4f,
        PriceRange.BUDGET,
        FoodCategory.SNACKS,
        1260,
        "Chaat & Street Snacks",
        20.353604927992816,
        85.8229162789492,
        "https://maps.app.goo.gl/bcerRLyQeCoibpcb6",
        12, 0, 22, 0,
        StoreImage.Res(R.drawable.delhichaat)
    ),

    FoodStore(
        18,
        "McDonald's",
        0,
        4.3f,
        PriceRange.PREMIUM,
        FoodCategory.BURGER,
        6400,
        "Burgers, Fries & Meals",
        20.338629273559995,
        85.82194261631483,
        "https://maps.app.goo.gl/iYKgQLWiBeYpYDdr7",
        11, 0, 2, 0,
        StoreImage.Res(R.drawable.mcdonalds)
    ),

    FoodStore(
        19,
        "Pahari Momo",
        0,
        4.7f,
        PriceRange.BUDGET,
        FoodCategory.MOMOS,
        980,
        "Steamed & Fried Momos",
        20.354077071511206,
        85.81569118352982,
        "https://maps.app.goo.gl/9azH16kNDbyXmCLL6",
        16, 30, 20, 30,
        StoreImage.Res(R.drawable.pahari_momo)
    ),

    FoodStore(
        20,
        "Rollicious Food Truck",
        0,
        4.2f,
        PriceRange.BUDGET,
        FoodCategory.ROLLS,
        870,
        "Egg, Chicken & Paneer Rolls",
        20.353768539558768,
        85.8174525794974,
        "https://maps.app.goo.gl/HEcM7nrpRapH5d28A",
        16, 0, 22, 30,
        StoreImage.Res(R.drawable.rollicious)
    ),
)

// Apply a filter to a list of stores
fun applyFilter(stores: List<FoodStore>, filter: FilterOption): List<FoodStore> {
    return when (filter) {
        FilterOption.NEAREST    -> stores.sortedBy { it.distanceMeters }
        FilterOption.CHEAPEST   -> stores.sortedBy { it.priceRange.ordinal }
        FilterOption.BEST_RATED -> stores.sortedByDescending { it.rating }
        FilterOption.OPEN_NOW   -> stores.filter { isStoreOpen(it) }.sortedBy { it.distanceMeters }
        FilterOption.LATE_NIGHT -> stores.filter { it.closingHour >= 23 || it.closingHour < 5 }.sortedBy { it.distanceMeters }
    }
}


fun calculateDistanceMeters(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Int {

    val earthRadius = 6371000.0

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a =
        sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) *
                cos(Math.toRadians(lat2)) *
                sin(dLon / 2) *
                sin(dLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    val straightDistance = earthRadius * c

    val correctedDistance = straightDistance * 1.45

    return correctedDistance.toInt()
}
fun getStoresForHostel(hostelId: String): List<FoodStore> {

    val hostel = ALL_HOSTELS.find { it.id == hostelId }
        ?: return emptyList()

    return ALL_STORES.map { store ->

        val realDistance = calculateDistanceMeters(
            hostel.latitude,
            hostel.longitude,
            store.latitude,
            store.longitude
        )

        store.copy(distanceMeters = realDistance)

    }.sortedBy { it.distanceMeters }
}