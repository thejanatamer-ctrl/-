package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DhikrType
import com.example.ui.theme.IslamicGoldBright
import com.example.ui.theme.IslamicGoldDark
import com.example.ui.theme.IslamicGoldPrimary
import com.example.ui.theme.IslamicGreenDark
import com.example.ui.theme.IslamicGreenMedium
import com.example.ui.theme.IslamicGreenPrimary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TasbeehButton(
    dhikr: DhikrType,
    currentCount: Int,
    lapCount: Int,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 600f),
        label = "tasbeeh_scale"
    )

    val target = dhikr.target
    val beadsTotal = if (target == 33) 33 else 30
    val activeBeads = ((currentCount.toFloat() / target) * beadsTotal).toInt()

    Box(
        modifier = modifier
            .size(280.dp)
            .scale(scale)
            .shadow(16.dp, CircleShape, spotColor = IslamicGreenDark)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        IslamicGreenMedium,
                        IslamicGreenPrimary,
                        IslamicGreenDark
                    )
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = IslamicGoldBright, radius = 150.dp),
                onClick = onTap
            )
            .testTag("tasbeeh_main_button"),
        contentAlignment = Alignment.Center
    ) {
        // Custom Canvas for ornate rings & 33 prayer beads
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val outerRadius = size.width / 2f - 14.dp.toPx()
            val innerRingRadius = size.width / 2f - 30.dp.toPx()

            // Outer gold decorative rim
            drawCircle(
                color = IslamicGoldPrimary.copy(alpha = 0.4f),
                radius = outerRadius + 8.dp.toPx(),
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            // Draw 33 prayer beads along circumference
            val angleStep = (2 * Math.PI / beadsTotal).toFloat()
            for (i in 0 until beadsTotal) {
                // start from top (-PI/2)
                val angle = -Math.PI / 2 + i * angleStep
                val x = center.x + outerRadius * cos(angle).toFloat()
                val y = center.y + outerRadius * sin(angle).toFloat()

                val isLit = i < activeBeads
                val beadColor = if (isLit) IslamicGoldBright else Color.White.copy(alpha = 0.25f)
                val beadRadius = if (isLit) 4.5.dp.toPx() else 3.5.dp.toPx()

                // Glow behind active bead
                if (isLit) {
                    drawCircle(
                        color = IslamicGoldBright.copy(alpha = 0.4f),
                        radius = beadRadius + 3.dp.toPx(),
                        center = Offset(x, y)
                    )
                }

                drawCircle(
                    color = beadColor,
                    radius = beadRadius,
                    center = Offset(x, y)
                )
            }

            // Inner ornate gold circle
            drawCircle(
                brush = Brush.sweepGradient(
                    listOf(
                        IslamicGoldDark,
                        IslamicGoldBright,
                        IslamicGoldPrimary,
                        IslamicGoldDark
                    )
                ),
                radius = innerRingRadius,
                center = center,
                style = Stroke(width = 2.5.dp.toPx())
            )

            // Dotted fine accent ring
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = innerRingRadius - 8.dp.toPx(),
                center = center,
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // Central Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // Arabic Dhikr Title
            Text(
                text = dhikr.vocalizedText,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = IslamicGoldBright,
                    fontSize = if (dhikr == DhikrType.SALAWAT) 14.sp else 18.sp,
                    lineHeight = if (dhikr == DhikrType.SALAWAT) 18.sp else 22.sp,
                    textAlign = TextAlign.Center
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Main Large Count Number
            Text(
                text = "$currentCount",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 54.sp
                ),
                textAlign = TextAlign.Center
            )

            // Target & Lap subtitle
            Text(
                text = "الهدف: $target  •  دورة: $lapCount",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = IslamicGoldPrimary.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Tap hint pill
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
                    .padding(horizontal = 12.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "المس للتسبيح",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
