package com.kiit.foodfinder

import android.content.Intent
import androidx.core.net.toUri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kiit.foodfinder.ui.theme.*
import com.kiit.foodfinder.viewmodel.LocalFavoritesViewModel
import java.util.Locale

@Composable
fun StoreDetailScreen(
    store: FoodStore,
    hostel: Hostel?,
    onBack: () -> Unit,
) {
    val context    = LocalContext.current
    val vm         = LocalFavoritesViewModel.current
    val responsive = rememberResponsiveInfo()
    val isOpen     = isStoreOpen(store)

    // Read favourite state reactively from the ViewModel's StateFlow.
    // The heart icon updates instantly across all screens when toggled.
    val favoriteIds by vm.favoriteIds.collectAsState()
    val isFavourite = favoriteIds.contains(store.id)

    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(700, easing = EaseOutCubic))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface900)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HeroSection(
                store       = store,
                isOpen      = isOpen,
                isFavourite = isFavourite,
                responsive  = responsive,
                onBack      = onBack,
                onFavourite = { vm.toggleFavorite(store.id) }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = responsive.globalPadding)
            ) {
                Spacer(modifier = Modifier.height(responsive.contentSpacing))

                DetailSection(visible = progress.value > 0.35f, delayMs = 0) {
                    QuickStatsRow(store = store, hostel = hostel, responsive = responsive)
                }

                DetailSpacer(responsive.contentSpacing)

                DetailSection(visible = progress.value > 0.45f, delayMs = 60) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionHeader(title = "About", responsive = responsive)
                        AboutCard(store = store, responsive = responsive)
                    }
                }

                DetailSpacer(responsive.contentSpacing)

                DetailSection(visible = progress.value > 0.55f, delayMs = 120) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionHeader(title = "Details", responsive = responsive)
                        DetailsCard(store = store, responsive = responsive)
                    }
                }

                DetailSpacer(responsive.contentSpacing)

                DetailSection(visible = progress.value > 0.65f, delayMs = 180) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionHeader(title = "Location", responsive = responsive)
                        LocationCard(store = store, responsive = responsive)
                    }
                }

                DetailSpacer(responsive.contentSpacing)

                DetailSection(visible = progress.value > 0.75f, delayMs = 240) {
                    ActionsSection(
                        store       = store,
                        isFavourite = isFavourite,
                        responsive  = responsive,
                        onFavourite = { vm.toggleFavorite(store.id) },
                        onMaps = {
                            val mapsUri = store.mapsLink.toUri()
                            val intent  = Intent(Intent.ACTION_VIEW, mapsUri)
                                .apply { setPackage("com.google.android.apps.maps") }
                            try { context.startActivity(intent) }
                            catch (_: Exception) {
                                context.startActivity(Intent(Intent.ACTION_VIEW, mapsUri))
                            }
                        }
                    )
                }

                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(32.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Hero
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HeroSection(
    store: FoodStore,
    isOpen: Boolean,
    isFavourite: Boolean,
    responsive: ResponsiveInfo,
    onBack: () -> Unit,
    onFavourite: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(
                when {
                    responsive.isSmallPhone -> 240.dp
                    responsive.isTallPhone  -> 340.dp
                    else                    -> 280.dp
                }
            )
    ) {
        when (val img = store.image) {
            is StoreImage.Res -> Image(
                painter = painterResource(id = img.resId),
                contentDescription = store.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            is StoreImage.Url -> AsyncImage(
                model = img.url,
                contentDescription = store.name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                modifier = Modifier.fillMaxSize()
            )
            StoreImage.None -> GradientHeroPlaceholder(store = store, responsive = responsive)
        }

        // Bottom scrim — image bleeds into Surface900
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.5f to Surface900.copy(alpha = 0.85f),
                            1.0f to Surface900
                        )
                    )
                )
        )

        // Back + Favourite
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = responsive.globalPadding, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeroIconButton(onClick = onBack, responsive = responsive) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary,
                    modifier = Modifier.size(if (responsive.isSmallPhone) 18.dp else 20.dp)
                )
            }
            HeroIconButton(onClick = onFavourite, responsive = responsive) {
                Icon(
                    imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favourite",
                    tint = if (isFavourite) RedClosed else TextPrimary,
                    modifier = Modifier.size(if (responsive.isSmallPhone) 18.dp else 20.dp)
                )
            }
        }

        // Name + chips overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = responsive.globalPadding, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                HeroChip(
                    text  = "${store.category.emoji}  ${store.category.label}",
                    color = BrandPrimary,
                    bg    = BrandDim,
                    responsive = responsive
                )
                HeroChip(
                    text  = if (isOpen) "🟢  Open" else "🔴  Closed",
                    color = if (isOpen) GreenOpen else RedClosed,
                    bg    = if (isOpen) GreenOpen.copy(alpha = 0.12f) else RedClosed.copy(alpha = 0.12f),
                    responsive = responsive
                )
            }
            Text(
                text          = store.name,
                color         = TextPrimary,
                fontWeight    = FontWeight.ExtraBold,
                fontSize      = responsive.h1,
                letterSpacing = (-0.6).sp,
                lineHeight    = responsive.h1,
                maxLines      = 2,
                overflow      = TextOverflow.Ellipsis
            )
            Text(
                text     = store.speciality,
                color    = TextSecondary,
                fontSize = responsive.body,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun GradientHeroPlaceholder(store: FoodStore, responsive: ResponsiveInfo) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colorStops = arrayOf(
                        0.0f to BrandGlow.copy(alpha = 0.55f),
                        0.5f to Surface800,
                        1.0f to Surface900
                    ),
                    center = Offset.Unspecified,
                    radius = 900f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(if (responsive.isSmallPhone) 100.dp else 130.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(BrandPrimary.copy(alpha = 0.22f), Color.Transparent)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text     = store.category.emoji,
                fontSize = if (responsive.isSmallPhone) 48.sp else 64.sp
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Quick stats row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun QuickStatsRow(store: FoodStore, hostel: Hostel?, responsive: ResponsiveInfo) {
    val showDistance = hostel != null && store.distanceMeters > 0
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Surface800)
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
            .padding(
                vertical = if (responsive.isSmallPhone) 12.dp else 16.dp,
                horizontal = 4.dp
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatCell(
            topContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.Star, null,
                        tint = StarYellow,
                        modifier = Modifier.size(if (responsive.isSmallPhone) 12.dp else 14.dp)
                    )
                    Text(
                        text = String.format(Locale.getDefault(), "%.1f", store.rating),
                        color = TextPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = responsive.h3
                    )
                }
            },
            label = "Rating",
            responsive = responsive
        )
        StatDivider()
        if (showDistance) {
            StatCell(
                topContent = {
                    Text(
                        text = store.formattedDistance(),
                        color = BrandPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = responsive.h3
                    )
                },
                label = "from you",
                responsive = responsive
            )
            StatDivider()
        }
        StatCell(
            topContent = {
                Text(
                    text = "₹${store.costForOne}",
                    color = TealAccent,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = responsive.h3
                )
            },
            label = "Avg Price",
            responsive = responsive
        )
    }
}

@Composable
private fun StatCell(topContent: @Composable () -> Unit, label: String, responsive: ResponsiveInfo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        topContent()
        Text(text = label, color = TextTertiary, fontSize = responsive.micro, maxLines = 1)
    }
}

@Composable
private fun StatDivider() {
    Box(modifier = Modifier.height(24.dp).width(1.dp).background(Color.White.copy(alpha = 0.1f)))
}

// ─────────────────────────────────────────────────────────────────────────────
//  Cards
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AboutCard(store: FoodStore, responsive: ResponsiveInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface800),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(responsive.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp).height(44.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Brush.verticalGradient(colors = listOf(TealAccent, BrandPrimary)))
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = store.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = responsive.body)
                Text(text = store.speciality, color = TextSecondary, fontSize = responsive.label, lineHeight = 18.sp)
            }
        }
    }
}

@Composable
private fun DetailsCard(store: FoodStore, responsive: ResponsiveInfo) {
    val rows = listOf(
        DetailRow("🕐", "Hours", formatHours(store)),
        DetailRow("🏷️", "Category", "${store.category.emoji}  ${store.category.label}"),
        DetailRow("💵", "Avg Price", "₹${store.costForOne} for one"),
        DetailRow("⭐", "Rating", String.format(Locale.getDefault(), "%.1f", store.rating))
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface800),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            rows.forEachIndexed { idx, row ->
                DetailRowItem(row, responsive)
                if (idx < rows.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.White.copy(alpha = 0.08f), thickness = 0.5.dp)
                }
            }
        }
    }
}

private data class DetailRow(val icon: String, val label: String, val value: String)

@Composable
private fun DetailRowItem(row: DetailRow, responsive: ResponsiveInfo) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = responsive.cardPadding, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(if (responsive.isSmallPhone) 32.dp else 36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Surface700)
        ) {
            Text(text = row.icon, fontSize = responsive.body)
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(text = row.label, color = TextTertiary, fontSize = responsive.micro, fontWeight = FontWeight.Medium)
            Text(text = row.value, color = TextPrimary, fontSize = responsive.body, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun LocationCard(store: FoodStore, responsive: ResponsiveInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface800),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(responsive.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(if (responsive.isSmallPhone) 40.dp else 46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(colors = listOf(BrandDim, Surface700)))
                    .border(1.dp, BrandPrimary.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            ) {
                Text("📍", fontSize = if (responsive.isSmallPhone) 18.sp else 20.sp)
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = "Near KIIT University", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = responsive.body)
                Text(text = "Patia, Bhubaneswar, Odisha", color = TextSecondary, fontSize = responsive.label)
                Text(
                    text = "${String.format(Locale.getDefault(), "%.4f", store.latitude)}°N, " +
                            "${String.format(Locale.getDefault(), "%.4f", store.longitude)}°E",
                    color = TextTertiary, fontSize = responsive.micro
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Action buttons
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ActionsSection(
    @Suppress("UNUSED_PARAMETER") store: FoodStore,
    isFavourite: Boolean,
    responsive: ResponsiveInfo,
    onFavourite: () -> Unit,
    onMaps: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = onMaps,
            modifier = Modifier
                .fillMaxWidth()
                .height(if (responsive.isSmallPhone) 48.dp else 54.dp)
                .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = BrandPrimary.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(BrandGradient),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "📍", fontSize = responsive.body)
                    Text(
                        text = "Open in Google Maps",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = responsive.h3,
                        color = Color.Black
                    )
                }
            }
        }

        OutlinedButton(
            onClick = onFavourite,
            modifier = Modifier.fillMaxWidth().height(if (responsive.isSmallPhone) 44.dp else 50.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, if (isFavourite) RedClosed.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.12f)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = if (isFavourite) RedClosed else TextSecondary)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.size(if (responsive.isSmallPhone) 16.dp else 18.dp)
                )
                Text(
                    text = if (isFavourite) "Saved to Favourites" else "Save to Favourites",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = responsive.label
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Reusable helpers
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String, responsive: ResponsiveInfo) {
    Text(text = title.uppercase(), color = TextTertiary, fontSize = responsive.micro, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
}

@Composable
private fun DetailSpacer(height: Dp = 20.dp) = Spacer(modifier = Modifier.height(height))

@Composable
private fun DetailSection(visible: Boolean, delayMs: Int, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(tween(480, delayMillis = delayMs, easing = EaseOutCubic)) { it / 5 } +
                fadeIn(tween(480, delayMillis = delayMs))
    ) { content() }
}

@Composable
private fun HeroChip(text: String, color: Color, bg: Color, responsive: ResponsiveInfo) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(bg)
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(50.dp))
            .padding(horizontal = if (responsive.isSmallPhone) 8.dp else 12.dp, vertical = 5.dp)
    ) {
        Text(text = text, color = color, fontWeight = FontWeight.SemiBold, fontSize = responsive.micro)
    }
}

@Composable
private fun HeroIconButton(onClick: () -> Unit, responsive: ResponsiveInfo, content: @Composable () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(if (responsive.isSmallPhone) 36.dp else 40.dp)
            .clip(CircleShape)
            .background(Surface900.copy(alpha = 0.65f))
            .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
    ) { content() }
}

private fun formatHours(store: FoodStore): String {
    fun fmt(h: Int, m: Int): String {
        val suffix = if (h < 12) "AM" else "PM"
        val hour = when { h == 0 -> 12; h > 12 -> h - 12; else -> h }
        return if (m == 0) "$hour $suffix" else "$hour:${m.toString().padStart(2, '0')} $suffix"
    }
    val open     = fmt(store.openingHour, store.openingMinute)
    val close    = fmt(store.closingHour, store.closingMinute)
    val midnight = store.closingHour < store.openingHour || (store.closingHour == 0 && store.closingMinute == 0)
    return if (midnight) "$open – $close (next day)" else "$open – $close"
}