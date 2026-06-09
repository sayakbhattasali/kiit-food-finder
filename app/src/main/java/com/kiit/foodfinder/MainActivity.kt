package com.kiit.foodfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kiit.foodfinder.data.db.AppDatabase
import com.kiit.foodfinder.data.repository.FavoriteRepository
import com.kiit.foodfinder.data.repository.PreferencesRepository
import com.kiit.foodfinder.ui.theme.BrandPrimary
import com.kiit.foodfinder.ui.theme.KIITFoodFinderTheme
import com.kiit.foodfinder.ui.theme.Surface600
import com.kiit.foodfinder.ui.theme.Surface800
import com.kiit.foodfinder.ui.theme.TextSecondary
import com.kiit.foodfinder.ui.theme.FoodFinderIcon
import com.kiit.foodfinder.ui.theme.AppIcon
import com.kiit.foodfinder.viewmodel.FavoritesViewModel
import com.kiit.foodfinder.viewmodel.FavoritesViewModelFactory
import com.kiit.foodfinder.viewmodel.LocalFavoritesViewModel

sealed class Screen {
    object Loading : Screen()
    object Splash : Screen()
    object Home : Screen()
    object Favorites : Screen()
    object About : Screen()
    data class Results(val hostel: Hostel?, val initialQuery: String = "") : Screen()
    data class StoreDetail(val store: FoodStore, val hostel: Hostel?, val fromResults: Screen) : Screen()
}

enum class Tab(val icon: FoodFinderIcon, val color: Color, val label: String) {
    Home(FoodFinderIcon.Home, Color(0xFF00BFA5), "Home"),        // Emerald/Teal
    Search(FoodFinderIcon.Store, Color(0xFF2196F3), "Restaurants"), // Blue
    Favorites(FoodFinderIcon.Favorite, Color(0xFFFF5252), "Saved")  // Red
}

class MainActivity : ComponentActivity() {

    private val db by lazy { AppDatabase.getInstance(this) }
    private val favoritesViewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(
            favoriteRepo = FavoriteRepository(db),
            prefsRepo    = PreferencesRepository(db)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KIITFoodFinderTheme {
                CompositionLocalProvider(
                    LocalFavoritesViewModel provides favoritesViewModel
                ) {
                    KIITFoodFinderApp()
                }
            }
        }
    }
}

@Composable
fun KIITFoodFinderApp() {
    val vm = LocalFavoritesViewModel.current
    val resultListState = rememberLazyListState()
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }


    // Persistent search state for the Explore tab
    var lastSearchHostel by remember { mutableStateOf<Hostel?>(null) }
    var lastSearchQuery by remember { mutableStateOf("") }

    val lastHostelId by vm.lastHostelId.collectAsState()

    // Determine current tab for the bottom navigation highlighting
    val currentTab = when (val screen = currentScreen) {
        is Screen.Home -> Tab.Home
        is Screen.Results -> Tab.Search
        is Screen.Favorites -> Tab.Favorites
        is Screen.StoreDetail -> {
            when (screen.fromResults) {
                is Screen.Home -> Tab.Home
                is Screen.Results -> Tab.Search
                is Screen.Favorites -> Tab.Favorites
                else -> Tab.Home
            }
        }
        else -> Tab.Home
    }

    BackHandler(enabled = currentScreen !is Screen.Home && currentScreen !is Screen.Splash) {
        if (currentScreen is Screen.Results) {
            lastSearchQuery = ""
            vm.resetSearchState("")
        }
        currentScreen = when (val s = currentScreen) {
            is Screen.StoreDetail -> s.fromResults
            is Screen.Results     -> Screen.Home
            is Screen.Favorites   -> Screen.Home
            is Screen.About       -> Screen.Home
            else                  -> Screen.Home
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val detailScreen = currentScreen as? Screen.StoreDetail
        val aboutScreen = currentScreen as? Screen.About
        val baseScreen = detailScreen?.fromResults ?: aboutScreen ?: currentScreen

        Crossfade(
            targetState = baseScreen,
            animationSpec = tween(220),
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                is Screen.Splash -> {
                    SplashScreen(onFinished = { currentScreen = Screen.Home })
                }
                is Screen.Home -> {
                    HomeScreen(
                        savedHostelId = lastHostelId,
                        onFindFood = { hostel, query ->
                            hostel?.let { 
                                vm.saveLastHostel(it.id)
                                lastSearchHostel = it
                            }
                            lastSearchQuery = query
                            if (query.isNotBlank()) vm.addRecentSearch(query)
                            
                            // Reset persistent results state for a fresh search from Home
                            vm.resetSearchState(query)

                            currentScreen = Screen.Loading
                        },
                        onLogoClick = { currentScreen = Screen.About }
                    )
                }
                is Screen.Loading -> {
                    RunningSearchScreen(
                        hostelName = lastSearchHostel?.displayName,
                        onFinish = {
                            currentScreen = Screen.Results(lastSearchHostel, lastSearchQuery)
                        }
                    )
                }
                is Screen.Favorites -> {
                    FavoritesScreen(
                        onStoreClick = { store ->
                            currentScreen = Screen.StoreDetail(store, null, screen)
                        }
                    )
                }
                is Screen.Results -> {
                    key("results_screen") {
                        ResultScreen(
                            hostel = screen.hostel,
                            initialSearchQuery = screen.initialQuery,
                            onBack = { 
                                lastSearchQuery = ""
                                vm.resetSearchState("")
                                currentScreen = Screen.Home 
                            },
                            onStoreClick = { store ->
                                currentScreen = Screen.StoreDetail(store, screen.hostel, screen)
                            },
                            externalListState = resultListState
                        )
                    }

                }
                is Screen.About -> {
                    // This is now handled as an overlay for cinematic effect
                }
                is Screen.StoreDetail -> {
                    // This case is now handled by baseScreen logic above,
                    // but we keep it for exhaustiveness if needed.
                }
            }
        }

        // Overlay for StoreDetailScreen
        AnimatedVisibility(
            visible = detailScreen != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            detailScreen?.let { screen ->
                StoreDetailScreen(
                    store = screen.store,
                    hostel = screen.hostel,
                    onBack = { currentScreen = screen.fromResults }
                )
            }
        }

        // Overlay for AboutScreen
        AnimatedVisibility(
            visible = currentScreen is Screen.About,
            enter = fadeIn(tween(500)) + scaleIn(tween(500, easing = EaseOutCubic), initialScale = 0.92f),
            exit = fadeOut(tween(400)) + scaleOut(tween(400, easing = EaseInCubic), targetScale = 0.92f),
            modifier = Modifier.fillMaxSize()
        ) {
            AboutScreen(onBack = { currentScreen = Screen.Home })
        }

        // --- Premium Bottom Navigation Overlay ---
        AnimatedVisibility(
            visible = currentScreen !is Screen.Splash && currentScreen !is Screen.StoreDetail && currentScreen !is Screen.Loading && currentScreen !is Screen.About,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            PremiumBottomNav(
                selectedTab = currentTab,
                onTabClick = { tab ->
                    if (currentScreen is Screen.Results && tab != Tab.Search) {
                        lastSearchQuery = ""
                        vm.resetSearchState("")
                    }
                    currentScreen = when (tab) {
                        Tab.Home -> Screen.Home
                        Tab.Search -> Screen.Results(lastSearchHostel, lastSearchQuery)
                        Tab.Favorites -> Screen.Favorites
                    }
                }
            )
        }
    }
}

@Composable
fun PremiumBottomNav(
    selectedTab: Tab,
    onTabClick: (Tab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 12.dp)
    ) {
        Surface(
            color = Surface800.copy(alpha = 0.94f),
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { alpha = 1f }
                .shadow(
                    elevation = 30.dp,
                    shape = RoundedCornerShape(28.dp),
                    spotColor = Color.Black.copy(alpha = 0.45f)
                )
                .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Tab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    TabItem(
                        tab = tab,
                        isSelected = isSelected,
                        onClick = { onTabClick(tab) }
                    )
                }
            }
        }
    }
}

@Composable
fun TabItem(
    tab: Tab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .graphicsLayer { alpha = if (isSelected) 1f else 0.65f },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(BrandPrimary.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                )
            }
            AppIcon(
                icon = tab.icon,
                tint = if (isSelected) tab.color else TextSecondary,
                modifier = Modifier
                    .size(22.dp)
                    .graphicsLayer(
                        scaleX = if (isSelected) 1.15f else 1f,
                        scaleY = if (isSelected) 1.15f else 1f
                    )
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = tab.label,
            color = if (isSelected) BrandPrimary else TextSecondary,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            letterSpacing = 0.1.sp
        )
    }
}
