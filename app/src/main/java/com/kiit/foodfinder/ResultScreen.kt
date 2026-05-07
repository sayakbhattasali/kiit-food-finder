package com.kiit.foodfinder
import android.util.Log
import android.content.Intent
import androidx.core.net.toUri
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kiit.foodfinder.ui.theme.*
import com.kiit.foodfinder.viewmodel.LocalFavoritesViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    hostel: Hostel?,
    initialSearchQuery: String = "",
    onBack: () -> Unit,
    onStoreClick: (FoodStore) -> Unit,
    externalListState: LazyListState
) {
    val responsive    = rememberResponsiveInfo()
    val vm            = LocalFavoritesViewModel.current
    val haptic        = LocalHapticFeedback.current
    LaunchedEffect(Unit) {
        Log.d("SCROLLTEST", "RESULT_SCREEN_CREATED")
    }
    val favoriteIds   by vm.favoriteIds.collectAsState()

    // Read persistent state from ViewModel
    val savedQuery by vm.searchQuery.collectAsState()
    val savedFilter by vm.selectedFilter.collectAsState()
    val savedIndex by vm.scrollIndex.collectAsState()
    val savedOffset by vm.scrollOffset.collectAsState()


    var selectedFilter by rememberSaveable {
        mutableStateOf(savedFilter ?: if (hostel != null) FilterOption.NEAREST else FilterOption.BEST_RATED)
    }
    // Only use initialSearchQuery if the saved one is empty, to support "Explore" clicks
    var searchQuery by rememberSaveable {
        mutableStateOf(if (savedQuery.isNotEmpty()) savedQuery else initialSearchQuery)
    }

    val context = LocalContext.current
    val listState = externalListState

    // ── Scroll save / restore ────────────────────────────────────────────────
    // isRestored gates the snapshotFlow collector so it never overwrites
    // a valid saved position with (0,0) during the very first layout pass.
    var isRestored by remember { mutableStateOf(false) }

    // RESTORE: run once when the screen enters the composition.
    // scrollToItem is safe to call even if the list has not laid out yet —
    // LazyColumn will honour it on the first measure pass.
    LaunchedEffect(Unit) {
        if (savedIndex > 0 || savedOffset > 0) {
            listState.scrollToItem(savedIndex, savedOffset)
        }
        isRestored = true
    }

    // SAVE on dispose: fires exactly once when ResultScreen leaves the tree
    // (i.e. when the user navigates to StoreDetail or presses back).
    // This is the only reliable moment to capture the final scroll position
    // because it runs AFTER all recompositions but BEFORE the state is gone.
    DisposableEffect(Unit) {
        onDispose {
            vm.updateSearchState(
                searchQuery,
                selectedFilter,
                listState.firstVisibleItemIndex,
                listState.firstVisibleItemScrollOffset
            )
        }
    }

    // SAVE on filter/search change: keeps metadata in sync when the user
    // changes the filter or search query (non-scroll state changes).
    LaunchedEffect(searchQuery, selectedFilter) {
        if (isRestored) {
            vm.updateSearchState(
                searchQuery,
                selectedFilter,
                listState.firstVisibleItemIndex,
                listState.firstVisibleItemScrollOffset
            )
        }
    }

    // SAVE on scroll: persists position while the user is actively scrolling.
    // The isRestored + isScrollInProgress guards prevent (0,0) from being
    // written during the initial layout pass before restore has run.
    LaunchedEffect(listState) {
        snapshotFlow {
            Triple(
                listState.firstVisibleItemIndex,
                listState.firstVisibleItemScrollOffset,
                listState.isScrollInProgress
            )
        }.collect { (index, offset, scrolling) ->
            if (isRestored && scrolling) {
                vm.updateSearchState(searchQuery, selectedFilter, index, offset)
            }
        }
    }

    // Automatically scroll to top when filters change
    LaunchedEffect(searchQuery, selectedFilter) {
        if (isRestored) {
            listState.animateScrollToItem(0)
        }
    }

    val rawStores = remember(hostel) {
        if (hostel != null) getStoresForHostel(hostel.id) else ALL_STORES
    }

    val displayedStores by remember(selectedFilter, rawStores, searchQuery) {
        derivedStateOf {
            val filtered = if (searchQuery.isBlank()) rawStores
            else rawStores.filter { store ->
                store.name.contains(searchQuery, ignoreCase = true) ||
                        store.category.label.contains(searchQuery, ignoreCase = true) ||
                        store.speciality.contains(searchQuery, ignoreCase = true)
            }
            applyFilter(filtered, selectedFilter)
        }
    }


    val openCount = rawStores.count { isStoreOpen(it) }

    Box(modifier = Modifier.fillMaxSize().background(Surface900)) {

        Canvas(modifier = Modifier.fillMaxWidth().height(300.dp)) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(BrandPrimary.copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(size.width * 0.5f, 0f),
                    radius = size.width * 0.8f
                ),
                center = Offset(size.width * 0.5f, 0f),
                radius = size.width * 0.8f
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = responsive.globalPadding, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(if (responsive.isSmallPhone) 38.dp else 42.dp)
                        .clip(CircleShape)
                        .background(Surface800)
                        .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary,
                        modifier = Modifier.size(if (responsive.isSmallPhone) 18.dp else 20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (hostel != null) "Nearby Food" else "Results",
                        color = TextPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = responsive.h2,
                        letterSpacing = (-0.5).sp
                    )
                    val subtitle = if (hostel != null) {
                        when (selectedFilter) {
                            FilterOption.NEAREST -> "Near ${hostel.displayName} • $openCount open"
                            FilterOption.BEST_RATED -> "Top rated near ${hostel.displayName}"
                            FilterOption.CHEAPEST -> "Budget eats near ${hostel.displayName}"
                            FilterOption.OPEN_NOW -> "Open now near ${hostel.displayName}"
                            FilterOption.LATE_NIGHT -> "Late night spots near ${hostel.displayName}"
                        }
                    } else {
                        "Found ${displayedStores.size} matches"
                    }
                    Text(
                        text = subtitle,
                        color = TextSecondary,
                        fontSize = responsive.label
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BrandPrimary.copy(alpha = 0.12f),
                    modifier = Modifier.border(0.5.dp, BrandPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = "${displayedStores.size}",
                        color = BrandPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = responsive.body,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            val fullOrder = listOf(
                FilterOption.NEAREST,
                FilterOption.BEST_RATED,
                FilterOption.LATE_NIGHT,
                FilterOption.OPEN_NOW,
                FilterOption.CHEAPEST
            )
            val availableFilters = if (hostel != null) fullOrder
            else fullOrder.filter { it != FilterOption.NEAREST }

            LazyRow(
                contentPadding = PaddingValues(horizontal = responsive.globalPadding, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(availableFilters.size) { index ->
                    val filter = availableFilters[index]
                    PremiumFilterChip(filter, selectedFilter == filter, responsive) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        selectedFilter = filter
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (displayedStores.isEmpty()) {
                EmptyState(responsive)
            } else {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(
                        start = responsive.globalPadding,
                        end = responsive.globalPadding,
                        top = 4.dp,
                        bottom = 0.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(displayedStores.size) { index ->
                        val store = displayedStores[index]
                        EnhancedStoreCard(
                            store       = store,
                            rank        = index + 1,
                            hostel      = hostel,
                            responsive  = responsive,
                            isFavorite  = favoriteIds.contains(store.id),
                            onCardClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onStoreClick(store)
                            },
                            onNavigate  = {
                                val mapsUri = it.mapsLink.toUri()
                                val intent  = Intent(Intent.ACTION_VIEW, mapsUri)
                                    .apply { setPackage("com.google.android.apps.maps") }
                                try { context.startActivity(intent) }
                                catch (_: Exception) { context.startActivity(Intent(Intent.ACTION_VIEW, mapsUri)) }
                            },
                            onFavoriteToggle = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                vm.toggleFavorite(store.id)
                            },
                            isResultScreen = (hostel != null && searchQuery.isEmpty()),
                            currentFilter = selectedFilter
                        )
                    }
                    item {
                        Spacer(
                            modifier = Modifier
                                .navigationBarsPadding()
                                .height(84.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PremiumFilterChip(
    filter: FilterOption,
    isSelected: Boolean,
    responsive: ResponsiveInfo,
    onClick: () -> Unit
) {
    val chipEmoji = when (filter) {
        FilterOption.NEAREST    -> "📍"
        FilterOption.BEST_RATED -> "⭐"
        FilterOption.LATE_NIGHT -> "🌙"
        FilterOption.OPEN_NOW   -> "🟢"
        FilterOption.CHEAPEST   -> "💸"
    }
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        color = if (isSelected) Color.Transparent else Surface800,
        border = BorderStroke(1.dp, if (isSelected) Color.Transparent else Color.White.copy(alpha = 0.10f)),
        modifier = Modifier.then(
            if (isSelected) Modifier.background(BrandGradient, RoundedCornerShape(50.dp)) else Modifier
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = chipEmoji, fontSize = responsive.label)
            Text(
                text = filter.label,
                color = if (isSelected) Color.Black else TextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = responsive.label
            )
        }
    }
}

@Composable
fun EnhancedStoreCard(
    store: FoodStore,
    rank: Int,
    hostel: Hostel?,
    responsive: ResponsiveInfo,
    isFavorite: Boolean,
    onCardClick: () -> Unit,
    onNavigate: (FoodStore) -> Unit,
    onFavoriteToggle: (() -> Unit)? = null, // Added optional toggle
    isResultScreen: Boolean = false,
    currentFilter: FilterOption? = null
) {
    val shouldShowHero =
        isResultScreen &&
                rank == 1 &&
                (
                        currentFilter == FilterOption.NEAREST ||
                                currentFilter == FilterOption.BEST_RATED ||
                                currentFilter == FilterOption.CHEAPEST
                        )

    val isHero = shouldShowHero
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val baseScale = if (isHero) 1.02f else 1f
    val scale by animateFloatAsState(
        targetValue = if (isPressed) baseScale * 0.97f else baseScale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "scale"
    )

    val delay = (rank * 50).coerceAtMost(300)
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(tween(400, delay)) { it / 6 } + fadeIn(tween(400, delay))
    ) {
        Card(
            onClick = onCardClick,
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Surface800),
            border = BorderStroke(
                width = if (isHero) 1.5.dp else 1.dp,
                color = if (isHero) BrandPrimary.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.14f)
            ),
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .shadow(
                    elevation = if (isHero) (if (isPressed) 8.dp else 20.dp) else (if (isPressed) 4.dp else 12.dp),
                    shape = RoundedCornerShape(24.dp),
                    spotColor = if (isHero) BrandPrimary.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.4f),
                    ambientColor = if (isHero) BrandPrimary.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.1f)
                )
        ) {
            Column {
                val imageHeight = if (isHero) {
                    if (responsive.isSmallPhone) 160.dp else 185.dp
                } else {
                    if (responsive.isSmallPhone) 140.dp else 160.dp
                }

                Box(modifier = Modifier.fillMaxWidth().height(imageHeight)) {
                    StoreThumbnail(store)

                    Box(modifier = Modifier.fillMaxSize().background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Surface800.copy(alpha = 0.85f)),
                            startY = 100f
                        )
                    ))

                    if (isHero) {
                        Surface(
                            shape = RoundedCornerShape(bottomEnd = 16.dp),
                            color = BrandPrimary.copy(alpha = 0.9f),
                            modifier = Modifier.align(Alignment.TopStart)
                        ) {
                            Text(
                                text = "🏆 Best Pick",
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    // Rank, status, and favourite indicator all in one overlay row
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        if (!isHero) {
                            Surface(
                                shape = CircleShape,
                                color = Surface900.copy(alpha = 0.85f),
                                modifier = Modifier.size(32.dp).border(1.2.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("#$rank", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Favourite indicator / Toggle
                            Surface(
                                shape = CircleShape,
                                color = Surface900.copy(alpha = 0.85f),
                                onClick = { onFavoriteToggle?.invoke() },
                                enabled = onFavoriteToggle != null || isFavorite,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Favorite",
                                        tint = if (isFavorite) RedClosed else Color.White.copy(alpha = 0.4f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            PremiumOpenBadge(isStoreOpen(store), responsive)
                        }
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = store.name,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = responsive.h3,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${store.category.emoji} ${store.category.label} • ${store.speciality}",
                                color = TextSecondary.copy(alpha = 0.7f),
                                fontSize = responsive.label,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Surface700,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Star, null, tint = StarYellow, modifier = Modifier.size(14.dp))
                                Text(
                                    String.format(Locale.getDefault(), "%.1f", store.rating),
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = responsive.label
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Surface600.copy(alpha = 0.5f), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (hostel != null) {
                                InfoItem("📍", store.formattedDistanceAndTime(), responsive)
                            }
                            InfoItem("💵", "₹${store.costForOne} for one", responsive)
                        }
                        Button(
                            onClick = { onNavigate(store) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.height(36.dp).width(90.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize().background(BrandGradient), contentAlignment = Alignment.Center) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Default.Navigation, null, tint = Color.Black, modifier = Modifier.size(15.dp))
                                    Text("Go", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StoreThumbnail(store: FoodStore) {
    when (val img = store.image) {
        is StoreImage.Res -> Image(
            painter = painterResource(id = img.resId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        is StoreImage.Url -> AsyncImage(
            model = img.url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        StoreImage.None -> Box(
            modifier = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Surface700, Surface600))),
            contentAlignment = Alignment.Center
        ) {
            Text(store.category.emoji, fontSize = 48.sp)
        }
    }
}

@Composable
private fun InfoItem(emoji: String, text: String, responsive: ResponsiveInfo) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(emoji, fontSize = responsive.label)
        Text(text, color = TextSecondary.copy(alpha = 0.7f), fontSize = responsive.label, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PremiumOpenBadge(isOpen: Boolean, responsive: ResponsiveInfo) {
    val color = if (isOpen) GreenOpen else RedClosed
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Surface900.copy(alpha = 0.85f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(color))
            Text(
                text = if (isOpen) "OPEN" else "CLOSED",
                color = color,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                letterSpacing = 0.6.sp
            )
        }
    }
}

@Composable
private fun EmptyState(responsive: ResponsiveInfo) {
    Column(
        modifier = Modifier.fillMaxSize().padding(responsive.globalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(100.dp).clip(CircleShape).background(Surface800),
            contentAlignment = Alignment.Center
        ) {
            Text("🔍", fontSize = 42.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("No results found", color = TextPrimary, fontWeight = FontWeight.ExtraBold, fontSize = responsive.h2, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "We couldn't find any stores matching your search. Try different keywords or filters.",
            color = TextSecondary,
            fontSize = responsive.body,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}