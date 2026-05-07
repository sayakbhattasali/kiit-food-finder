package com.kiit.foodfinder
import kotlin.math.*
// ─────────────────────────────────────────
//  All hostels available in the dropdown
// ─────────────────────────────────────────
val ALL_HOSTELS = listOf(
    Hostel("kp1", "KP 1", 20.353816702160223, 85.82044206476485),
    Hostel("kp2", "KP 2", 20.3546856825232, 85.8189894790152),
    Hostel("kp3", "KP 3", 20.35431276853413, 85.81948720836155),
    Hostel("kp4", "KP 4", 20.354232588857485, 85.8211044220393),
    Hostel("kp5", "KP 5", 20.357063273674118, 85.82010926552181),
    Hostel("kp6", "KP 6", 20.349060303708132, 85.81615220969914),
    Hostel("kp7", "KP 7", 20.351223559466384, 85.81633709435714),
    Hostel("kp8", "KP 8", 20.360660556183184, 85.82382114102673),
    Hostel("kp9", "KP 9", 20.349509898751258, 85.81419693712112),
    Hostel("kp10", "KP 10", 20.35459118278787, 85.81626622014575),
    Hostel("kp12", "KP 12", 20.351766701703497, 85.82143663312148),
    Hostel("kp14", "KP 14", 20.355217094547026, 85.8194479859214),
    Hostel("kp15", "KP 15", 20.356014315327986, 85.82077136852529),
    Hostel("kp16", "KP 16", 20.35901167183957, 85.81828564917811),
    Hostel("kp18", "KP 18", 20.356563206070213, 85.82392515744404),
    Hostel("kp19", "KP 19", 20.35174566812916, 85.81448391562891),
    Hostel("kp21", "KP 21", 20.351422074940746, 85.81573273602075),
    Hostel("kp22", "KP 22", 20.349376280070963, 85.82096428896497),
    Hostel("kp23", "KP 23", 20.3479323894072, 85.82062080892723),
    Hostel("kp25", "KP 25", 20.36260234893656, 85.81566639435748),
    Hostel("kp26", "KP 26", 20.36192962805571, 85.82668941230452),
    Hostel("qc1", "QC 1", 20.352443005849356, 85.81810213655656),
    Hostel("qc2", "QC 2", 20.352312154531386, 85.81878769962256),
    Hostel("qc3", "QC 3", 20.35216393668616, 85.81683717029895),
    Hostel("qc4", "QC 4", 20.352814574950983, 85.81827569435721),
    Hostel("qc5", "QC 5", 20.348670807792217, 85.82085820969911),
    Hostel("qc8", "QC 8", 20.35253394369445, 85.82132910782022),
    Hostel("qc10", "QC 10", 20.352643037177465, 85.81565415687572),
    Hostel("qc11", "QC 11", 20.35264692143039, 85.81513131235978),
    Hostel("qc12", "QC 12", 20.352090359024565, 85.81497018321832),
    Hostel("qc14", "QC 14", 20.346349115640983, 85.81887768524393),
    Hostel("qc16", "QC 16", 20.349840327876358, 85.81526532752562),
    Hostel("qc17", "QC 17", 20.348036179529704, 85.82036398325042),
    Hostel("qc18", "QC 18", 20.345896188333697, 85.81928208513015),
    Hostel("qc25", "QC 25", 20.36301562017607, 85.8177594986987)
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
        180,
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
        220,
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
        350,
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
        300,
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
        180,
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
        160,
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
        220,
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
        120,
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
        100,
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
        280,
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
        130,
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
        200,
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
        250,
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
        150,
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
        120,
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
        220,
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
        120,
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
        300,
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
        140,
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
        130,
        FoodCategory.ROLLS,
        870,
        "Egg, Chicken & Paneer Rolls",
        20.353768539558768,
        85.8174525794974,
        "https://maps.app.goo.gl/HEcM7nrpRapH5d28A",
        16, 0, 22, 30,
        StoreImage.Res(R.drawable.rollicious)
    ),
    FoodStore(
        21,
        "Burger King",
        0,
        4.4f,
        PriceRange.PREMIUM,
        300,
        FoodCategory.BURGER,
        8600,
        "Burgers, Fries & Meals",
        20.3535024018261,
        85.82449622337498,
        "https://maps.app.goo.gl/VZ4cMmnXEn762kqV7",
        11, 0, 23, 0,
        StoreImage.Res(R.drawable.burgerking)
    ),

    FoodStore(
        22,
        "Subway",
        0,
        4.4f,
        PriceRange.PREMIUM,
        400,
        FoodCategory.SNACKS,
        5400,
        "Subs, Wraps & Salads",
        20.352894444714607,
        85.82676938412756,
        "https://maps.app.goo.gl/ZZyFsPqzaLKYFWU88",
        7, 0, 2, 0,
        StoreImage.Res(R.drawable.subway)
    ),

    FoodStore(
        23,
        "Starbucks",
        0,
        4.7f,
        PriceRange.PREMIUM,
        400,
        FoodCategory.CAFE,
        9200,
        "Coffee, Frappes & Snacks",
        20.35335213653381,
        85.82428217074902,
        "https://maps.app.goo.gl/Kksfe8Jw5v1Azppq8",
        8, 0, 0, 0,
        StoreImage.Res(R.drawable.starbucks)
    ),

    FoodStore(
        24,
        "Mio Amore",
        0,
        4.1f,
        PriceRange.BUDGET,
        150,
        FoodCategory.DESSERT,
        3100,
        "Cake, Pastries & Snacks",
        20.35336546791933,
        85.8234899091378,
        "https://maps.app.goo.gl/Mrdm2byR8C8QYM5p9",
        9, 30, 21, 0,
        StoreImage.Res(R.drawable.mioamore)
    ),

    FoodStore(
        25,
        "Keventers",
        0,
        4.2f,
        PriceRange.MODERATE,
        300,
        FoodCategory.CAFE,
        2800,
        "Milkshakes, Coffee & Desserts",
        20.353293500962003,
        85.8244549610521,
        "https://maps.app.goo.gl/8WVudJZF68f2CUb36",
        11, 0, 23, 0,
        StoreImage.Res(R.drawable.keventers)
    ),

    FoodStore(
        26,
        "La Pino'z Pizza",
        0,
        4.0f,
        PriceRange.PREMIUM,
        300,
        FoodCategory.PIZZA,
        4200,
        "Pizzas, Sides & Beverages",
        20.353575598821575,
        85.82477484072696,
        "https://maps.app.goo.gl/p2nT7voVvhsbhVoy9",
        11, 0, 2, 0,
        StoreImage.Res(R.drawable.lapinoz)
    ),

    FoodStore(
        27,
        "Bengal Sweets",
        0,
        4.1f,
        PriceRange.BUDGET,
        100,
        FoodCategory.DESSERT,
        1700,
        "Authentic Bengali Sweets",
        20.35133496636536,
        85.82563858269687,
        "https://maps.app.goo.gl/MmJZRYw37DXUi7Lw8",
        10, 0, 20, 0,
        StoreImage.Res(R.drawable.bengalsweets)
    ),

    FoodStore(
        28,
        "7th Heaven",
        0,
        4.2f,
        PriceRange.MODERATE,
        200,
        FoodCategory.DESSERT,
        2500,
        "Cheesecakes, Pastries & Cakes",
        20.352068413572297,
        85.8259055898524,
        "https://maps.app.goo.gl/1NgxvP7DhRvAi3nU8",
        10, 0, 22, 0,
        StoreImage.Res(R.drawable.heaven7th)
    ),

    FoodStore(
        29,
        "Maa Ra Handishala 2.O",
        0,
        3.7f,
        PriceRange.MODERATE,
        300,
        FoodCategory.MULTI_CUISINE,
        1900,
        "Indian Meals & Starters",
        20.358881311983847,
        85.80475400303683,
        "https://maps.app.goo.gl/zj1G7XscWNrt4tpEA",
        11, 0, 23, 0,
        StoreImage.Res(R.drawable.handishala)
    ),

    FoodStore(
        30,
        "The House Of Burger",
        0,
        3.8f,
        PriceRange.MODERATE,
        200,
        FoodCategory.BURGER,
        1200,
        "Local Burgers & Fries",
        20.358069407420622,
        85.82135022358003,
        "https://maps.app.goo.gl/TqoKZAywWukyJbwT6",
        16, 0, 22, 0,
        StoreImage.Res(R.drawable.houseofburger)
    ),
)

// Apply a filter to a list of stores
fun applyFilter(stores: List<FoodStore>, filter: FilterOption): List<FoodStore> {
    return when (filter) {
        FilterOption.NEAREST    -> stores.sortedBy { it.distanceMeters }
        FilterOption.CHEAPEST   -> stores.sortedBy { it.costForOne }
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

    val correctedDistance = straightDistance * 1.4

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
