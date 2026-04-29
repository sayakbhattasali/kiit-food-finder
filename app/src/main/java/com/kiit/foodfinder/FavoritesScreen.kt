package com.kiit.foodfinder


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiit.foodfinder.ui.theme.*
import com.kiit.foodfinder.viewmodel.LocalFavoritesViewModel

@Composable
fun FavoritesScreen(
    onStoreClick: (FoodStore) -> Unit,
) {
    val responsive = rememberResponsiveInfo()
    val vm = LocalFavoritesViewModel.current
    val favoriteIds by vm.favoriteIds.collectAsState()

    val listState = rememberLazyListState()

    // Filter ALL_STORES to only show favorited ones
    val favoriteStores = remember(favoriteIds) {
        ALL_STORES.filter { favoriteIds.contains(it.id) }
    }

    Box(modifier = Modifier.fillMaxSize().background(Surface900)) {
        // Subtle glow effect
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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = responsive.globalPadding, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "My Favorites",
                        color = TextPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = responsive.h2,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "${favoriteStores.size} saved places",
                        color = TextSecondary,
                        fontSize = responsive.label
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = BrandDim,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("❤️", fontSize = 20.sp)
                    }
                }
            }

            if (favoriteStores.isEmpty()) {
                FavoritesEmptyState(responsive)
            } else {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(
                        start = responsive.globalPadding,
                        end = responsive.globalPadding,
                        top = 8.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(favoriteStores.size) { index ->
                        val store = favoriteStores[index]
                        EnhancedStoreCard(
                            store = store,
                            rank = index + 1,
                            hostel = null,
                            responsive = responsive,
                            isFavorite = true,
                            onCardClick = { onStoreClick(store) },
                            onNavigate = { /* Navigation handled via detail */ },
                            onFavoriteToggle = { vm.toggleFavorite(store.id) }
                        )
                    }
                    item {
                        Spacer(
                            modifier = Modifier
                                .navigationBarsPadding()
                                .height(80.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritesEmptyState(responsive: ResponsiveInfo) {
    Column(
        modifier = Modifier.fillMaxSize().padding(responsive.globalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(100.dp).clip(CircleShape).background(Surface800),
            contentAlignment = Alignment.Center
        ) {
            Text("🍱", fontSize = 42.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "No Favorites Yet",
            color = TextPrimary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = responsive.h2,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Start exploring and save your favorite food spots to see them here!",
            color = TextSecondary,
            fontSize = responsive.body,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}
