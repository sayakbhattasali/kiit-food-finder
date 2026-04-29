package com.kiit.foodfinder

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiit.foodfinder.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    savedHostelId: String? = null,
    onFindFood: (Hostel?, String) -> Unit
) {
    val responsive = rememberResponsiveInfo()
    var selectedHostelId by rememberSaveable {
        mutableStateOf(savedHostelId)
    }

    var selectedHostel by remember(selectedHostelId) {
        mutableStateOf(ALL_HOSTELS.find { it.id == selectedHostelId })
    }
    var dropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val logoOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val logoScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val enterAnim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        enterAnim.animateTo(1f, animationSpec = tween(800, easing = EaseOutCubic))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface900)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(BrandGlow.copy(alpha = 0.15f), Color.Transparent),
                    center = Offset(size.width * 0.9f, size.height * 0.1f),
                    radius = size.width * 0.55f
                ),
                center = Offset(size.width * 0.9f, size.height * 0.1f),
                radius = size.width * 0.55f
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(BrandPrimary.copy(alpha = 0.06f), Color.Transparent),
                    center = Offset(size.width * 0.1f, size.height * 0.85f),
                    radius = size.width * 0.4f
                ),
                center = Offset(size.width * 0.1f, size.height * 0.85f),
                radius = size.width * 0.4f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = responsive.globalPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(if (responsive.isShortPhone) 20.dp else 40.dp))

            // ── App Logo ─────────────────────────────────────────────
            AnimatedVisibility(
                visible = enterAnim.value > 0.1f,
                enter = scaleIn(tween(600)) + fadeIn(tween(600))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(if (responsive.isSmallPhone) 160.dp else 190.dp)
                        .offset(y = logoOffset.dp)
                        .graphicsLayer(scaleX = logoScale, scaleY = logoScale)
                )
            }

            Spacer(modifier = Modifier.height(responsive.contentSpacing))

            // ── 1. Search Bar ────────────────
            AnimatedVisibility(
                visible = enterAnim.value > 0.4f,
                enter = slideInVertically(tween(600, 100)) { it / 3 } + fadeIn(tween(600, 100))
            ) {
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (responsive.isSmallPhone) 54.dp else 60.dp)
                            .onFocusChanged { isSearchFocused = it.isFocused }
                            .shadow(
                                elevation = if (isSearchFocused) 15.dp else 0.dp,
                                shape = RoundedCornerShape(18.dp),
                                spotColor = BrandPrimary.copy(alpha = 0.4f)
                            ),
                        placeholder = {
                            Text("Search food or stores...", color = TextTertiary, fontSize = responsive.body)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = if (isSearchFocused) BrandPrimary else TextSecondary,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            imeAction = androidx.compose.ui.text.input.ImeAction.Search
                        ),
                        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                            onSearch = { onFindFood(selectedHostel, searchQuery) }
                        ),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Surface800.copy(alpha = 0.9f),
                            unfocusedContainerColor = Surface800.copy(alpha = 0.7f),
                            focusedBorderColor = BrandPrimary,
                            unfocusedBorderColor = Surface600,
                            cursorColor = BrandPrimary,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = responsive.body,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    AnimatedVisibility(
                        visible = searchQuery.isNotEmpty() && isSearchFocused,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        val suggestions = remember(searchQuery) {
                            ALL_STORES.filter {
                                it.name.contains(searchQuery, ignoreCase = true) ||
                                it.category.label.contains(searchQuery, ignoreCase = true) ||
                                it.speciality.contains(searchQuery, ignoreCase = true)
                            }.take(5)
                        }

                        if (suggestions.isNotEmpty()) {
                            Card(
                                modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Surface800),
                                border = BorderStroke(1.dp, Surface600)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    suggestions.forEach { store ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    searchQuery = store.name
                                                    onFindFood(selectedHostel, store.name)
                                                }
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = store.category.emoji, fontSize = responsive.body)
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = store.name,
                                                color = TextPrimary,
                                                fontSize = responsive.body,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(responsive.contentSpacing))

            // ── 2. Card: Hostel Selector ─────────────────────────────
            AnimatedVisibility(
                visible = enterAnim.value > 0.6f,
                enter = slideInVertically(tween(600, 200)) { it / 3 } + fadeIn(tween(600, 200))
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface800),
                    border = BorderStroke(1.dp, Surface600)
                ) {
                    Column(modifier = Modifier.padding(responsive.cardPadding)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(BrandPrimary.copy(alpha = 0.12f))
                            ) {
                                Text("🏠", fontSize = responsive.h3)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Select Your Hostel",
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = responsive.h3
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        ExposedDropdownMenuBox(
                            expanded = dropdownExpanded,
                            onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Surface700)
                                    .border(
                                        width = 1.dp,
                                        color = if (dropdownExpanded) BrandPrimary else Surface600,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = selectedHostel?.displayName ?: "Choose hostel…",
                                        color = if (selectedHostel != null) TextPrimary else TextTertiary,
                                        fontWeight = if (selectedHostel != null) FontWeight.Medium else FontWeight.Normal,
                                        fontSize = responsive.body
                                    )
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Expand",
                                        tint = if (dropdownExpanded) BrandPrimary else TextSecondary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            MaterialTheme(
                                colorScheme = MaterialTheme.colorScheme.copy(surface = Surface700),
                                shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp))
                            ) {
                                DropdownMenu(
                                    expanded = dropdownExpanded,
                                    onDismissRequest = { dropdownExpanded = false },
                                    offset = androidx.compose.ui.unit.DpOffset(y = 4.dp, x = 0.dp),
                                    modifier = Modifier
                                        .exposedDropdownSize()
                                        .heightIn(max = 260.dp)
                                        .background(Surface700)
                                        .border(1.dp, Surface600, RoundedCornerShape(16.dp))
                                ) {
                                    ALL_HOSTELS.forEach { hostel ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = "🏘️", fontSize = responsive.body, modifier = Modifier.padding(end = 10.dp))
                                                    Text(
                                                        text = hostel.displayName,
                                                        color = if (selectedHostel?.id == hostel.id) BrandPrimary else TextPrimary,
                                                        fontWeight = if (selectedHostel?.id == hostel.id) FontWeight.SemiBold else FontWeight.Normal,
                                                        fontSize = responsive.body
                                                    )
                                                }
                                            },
                                            onClick = {
                                                selectedHostelId = hostel.id
                                                selectedHostel = hostel
                                                dropdownExpanded = false
                                            },
                                            modifier = Modifier.background(
                                                if (selectedHostel?.id == hostel.id) BrandPrimary.copy(alpha = 0.08f) else Color.Transparent
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = { selectedHostel?.let { onFindFood(it, searchQuery) } },
                            enabled = selectedHostel != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (responsive.isSmallPhone) 48.dp else 54.dp)
                                .then(
                                    if (selectedHostel != null) {
                                        Modifier.shadow(elevation = 8.dp, shape = RoundedCornerShape(14.dp), spotColor = BrandPrimary.copy(alpha = 0.5f))
                                    } else Modifier
                                ),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                disabledContainerColor = Surface600,
                                contentColor = Color.Black,
                                disabledContentColor = TextTertiary
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(if (selectedHostel != null) BrandGradient else Brush.linearGradient(listOf(Surface600, Surface600))),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🚀 Explore",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = responsive.h3,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(responsive.contentSpacing))

            // ── Quick stats row ──────────────────────────────────────
            AnimatedVisibility(
                visible = enterAnim.value > 0.8f,
                enter = fadeIn(tween(600, 400))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickStat(emoji = "🏪", number = "20+", label = "Food Spots", responsive = responsive)
                    StatDivider()
                    QuickStat(emoji = "🏠", number = "7", label = "Hostels", responsive = responsive)
                    StatDivider()
                    QuickStat(emoji = "⚡", number = "<1km", label = "Avg. Distance", responsive = responsive)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun QuickStat(emoji: String, number: String, label: String, responsive: ResponsiveInfo) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = if (responsive.isSmallPhone) 18.sp else 22.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = number,
            color = TextPrimary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = responsive.h3
        )
        Text(
            text = label,
            color = TextSecondary,
            fontSize = responsive.micro,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .height(36.dp)
            .width(1.dp)
            .background(Surface600)
    )
}

@Composable
private fun CategoryPill(label: String, responsive: ResponsiveInfo) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Surface700)
            .border(1.dp, Surface600, RoundedCornerShape(50.dp))
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = responsive.label,
            maxLines = 1,
            fontWeight = FontWeight.Medium
        )
    }
}
