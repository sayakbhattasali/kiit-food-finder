package com.kiit.foodfinder
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import com.kiit.foodfinder.ui.theme.Surface900
import com.kiit.foodfinder.ui.theme.TextPrimary
import com.kiit.foodfinder.ui.theme.TextSecondary
import com.kiit.foodfinder.ui.theme.BrandPrimary
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset

import androidx.compose.runtime.Composable

@Composable
fun RunningSearchScreen(
    hostelName: String?,
    onFinish: () -> Unit
) {
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        contentVisible = true
        delay(1000)
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface900),
        contentAlignment = Alignment.Center
    ) {
        // Multi-layered radial glow for depth
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Outer glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(BrandPrimary.copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(size.width * 0.5f, size.height * 0.5f),
                    radius = size.width * 0.8f
                ),
                center = Offset(size.width * 0.5f, size.height * 0.5f),
                radius = size.width * 0.8f
            )
            // Inner glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(BrandPrimary.copy(alpha = 0.25f), Color.Transparent),
                    center = Offset(size.width * 0.5f, size.height * 0.5f),
                    radius = size.width * 0.4f
                ),
                center = Offset(size.width * 0.5f, size.height * 0.5f),
                radius = size.width * 0.4f
            )
        }

        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { 20 }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Finding great food near you",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 40.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Scanning nearby restaurants",
                    color = TextSecondary.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    color = BrandPrimary,
                    strokeWidth = 2.5.dp
                )
            }
        }
    }
}
