package com.kiit.foodfinder

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiit.foodfinder.ui.theme.*

@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    val responsive = rememberResponsiveInfo()
    val enterAnim = remember { Animatable(0f) }

    // Trigger entrance animation on launch
    LaunchedEffect(Unit) {
        enterAnim.animateTo(1f, animationSpec = tween(800, easing = EaseOutCubic))
    }

    // Infinite breathing glow for the background
    val infiniteTransition = rememberInfiniteTransition(label = "bg_glow")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(modifier = Modifier.fillMaxSize().background(Surface900)) {
        // Futuristic Layered Background
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(BrandPrimary.copy(alpha = 0.15f * glowPulse), Color.Transparent),
                    center = Offset(size.width * 0.8f, size.height * 0.15f),
                    radius = size.width * 0.8f
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(BrandPrimary.copy(alpha = 0.08f * glowPulse), Color.Transparent),
                    center = Offset(size.width * 0.2f, size.height * 0.75f),
                    radius = size.width * 0.7f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                Text(
                    text = "About",
                    color = TextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = responsive.h2,
                    letterSpacing = (-0.5).sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Identity Section
            AnimatedVisibility(
                visible = enterAnim.value > 0.2f,
                enter = slideInVertically(tween(600)) { it / 4 } + fadeIn(tween(600))
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = responsive.globalPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(if (responsive.isSmallPhone) 100.dp else 120.dp)
                            .clip(RoundedCornerShape(28.dp))
                            // Replaced the dark gradient with a subtle, transparent Brand glow
                            .background(BrandPrimary.copy(alpha = 0.05f))
                            .border(1.dp, BrandPrimary.copy(alpha = 0.2f), RoundedCornerShape(28.dp))
                            // Removed the heavy shadow to prevent rendering artifacts on transparent PNGs
                        ,
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "App Logo",
                            // Added contentScale to ensure it fills the box cleanly
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "KIIT Food Finder",
                        color = TextPrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = if (responsive.isSmallPhone) 28.sp else 34.sp,
                        letterSpacing = (-1).sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Version 2.0.0",
                        color = BrandPrimary,
                        fontSize = responsive.label,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your ultimate campus companion. Discover the best local food spots, check real-time availability, and navigate straight to your cravings seamlessly.",
                        color = TextSecondary,
                        fontSize = responsive.body,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // How to Use Carousel Section
            AnimatedVisibility(
                visible = enterAnim.value > 0.4f,
                enter = slideInVertically(tween(600)) { it / 4 } + fadeIn(tween(600))
            ) {
                Column {
                    Text(
                        text = "HOW IT WORKS",
                        color = TextTertiary,
                        fontSize = responsive.micro,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(horizontal = responsive.globalPadding)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = responsive.globalPadding),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FeatureCard("1", "Select Hostel", "Anchor your search to find the closest bites instantly.", FoodFinderIcon.Hostel)
                        FeatureCard("2", "Filter & Search", "Find what's open, top-rated, or budget-friendly.", FoodFinderIcon.Search)
                        FeatureCard("3", "Navigate", "Get precise Google Maps directions right to the door.", FoodFinderIcon.Location)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Developer Profile Section
            AnimatedVisibility(
                visible = enterAnim.value > 0.6f,
                enter = slideInVertically(tween(600)) { it / 4 } + fadeIn(tween(600))
            ) {
                Column(modifier = Modifier.padding(horizontal = responsive.globalPadding)) {
                    Text(
                        text = "DEVELOPER",
                        color = TextTertiary,
                        fontSize = responsive.micro,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    InteractiveDeveloperCard(responsive)
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun FeatureCard(step: String, title: String, desc: String, icon: FoodFinderIcon) {
    Card(
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface800),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Surface700)
                        .border(1.dp, Color.White.copy(alpha = 0.05f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AppIcon(icon = icon, tint = BrandPrimary, modifier = Modifier.size(20.dp))
                }
                Text(
                    text = "#$step",
                    color = BrandPrimary.copy(alpha = 0.3f),
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = desc,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun InteractiveDeveloperCard(responsive: ResponsiveInfo) {
    var tiltX by remember { mutableFloatStateOf(0f) }
    var tiltY by remember { mutableFloatStateOf(0f) }
    var isPressed by remember { mutableStateOf(false) }
    var isFlipped by remember { mutableStateOf(false) }

    // Spring physics for buttery smooth 3D tilting and flipping
    val rotationYAnim by animateFloatAsState(
        targetValue = if (isFlipped) 180f else tiltY,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 120f),
        label = "rotationY"
    )
    val rotationXAnim by animateFloatAsState(
        targetValue = if (isFlipped) 0f else tiltX,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 120f),
        label = "rotationX"
    )
    val scaleAnim by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "scale"
    )

    val uriHandler = LocalUriHandler.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (responsive.isSmallPhone) 320.dp else 280.dp) // Significant height increase
            .graphicsLayer {
                rotationX = rotationXAnim
                rotationY = rotationYAnim
                scaleX = scaleAnim
                scaleY = scaleAnim
                cameraDistance = 14f * density
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    isPressed = true
                    var totalDragX = 0f
                    val width = size.width.toFloat()
                    val height = size.height.toFloat()

                    do {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull()
                        if (change != null && change.pressed) {
                            val position = change.position
                            val dragAmount = position.x - change.previousPosition.x
                            totalDragX += dragAmount

                            if (!isFlipped) {
                                tiltX = ((position.y - height / 2f) / (height / 2f)) * -12f
                                tiltY = ((position.x - width / 2f) / (width / 2f)) * 12f
                            }
                        }
                    } while (event.changes.any { it.pressed })

                    if (kotlin.math.abs(totalDragX) > 60) {
                        isFlipped = !isFlipped
                    }

                    isPressed = false
                    tiltX = 0f
                    tiltY = 0f
                }
            }
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Surface800.copy(alpha = 0.92f)),
            border = BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.4f))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(BrandPrimary.copy(alpha = 0.12f), Color.Transparent),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, size.height)
                        )
                    )
                }

                if (rotationYAnim <= 90f) {
                    Column(
                        modifier = Modifier.padding(responsive.cardPadding).fillMaxSize(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(if (responsive.isSmallPhone) 56.dp else 64.dp)
                                    .clip(CircleShape)
                                    .background(BrandGradient)
                                    .border(2.dp, Surface900, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.dev_pfp),
                                    contentDescription = "Developer Profile",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Sayak Bhattasali",
                                    color = TextPrimary,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = if (responsive.isSmallPhone) 18.sp else 22.sp,
                                    letterSpacing = (-0.5).sp
                                )
                                Text(
                                    text = "Web & App developer",
                                    color = BrandPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = if (responsive.isSmallPhone) 12.sp else 13.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Passionate about building fluid, user-centric Android applications and crafting modern UI architectures.",
                            color = TextSecondary,
                            fontSize = if (responsive.isSmallPhone) 12.sp else 13.sp,
                            lineHeight = if (responsive.isSmallPhone) 16.sp else 18.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { uriHandler.openUri("https://www.linkedin.com/in/sayak-bhattasali-5345542a7/") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (responsive.isSmallPhone) 42.dp else 46.dp)
                                .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = BrandPrimary.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize().background(BrandGradient),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    AppIcon(icon = FoodFinderIcon.Rocket, tint = Color.Black, modifier = Modifier.size(16.dp))
                                    Text(
                                        text = "Connect on LinkedIn",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = if (responsive.isSmallPhone) 13.sp else 14.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .graphicsLayer {
                                    val pulse = (kotlin.math.sin(System.currentTimeMillis() / 400.0) * 0.2 + 0.8).toFloat()
                                    alpha = pulse
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = null,
                                tint = BrandPrimary.copy(alpha = 0.5f),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Swipe to flip",
                                color = TextTertiary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { rotationY = 180f }
                            .padding(responsive.cardPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = BrandPrimary.copy(alpha = 0.6f),
                                modifier = Modifier.size(if (responsive.isSmallPhone) 24.dp else 28.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "I MADE THIS APP AS A MEMORY OF MY YEARS SPENT IN KiiT... THIS APP CAN BE USED AS A GUIDE FOR NEW KiiTIANS TO SET THEIR EVENING HANGOUTS (SPEAKING FROM PERSONAL EXPERIENCE) ...",
                                color = TextPrimary,
                                fontSize = if (responsive.isSmallPhone) 13.sp else 14.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                lineHeight = if (responsive.isSmallPhone) 18.sp else 20.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "~ Sayak-2328045",
                                color = BrandPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = if (responsive.isSmallPhone) 12.sp else 13.sp,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
