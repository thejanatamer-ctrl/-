package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material.icons.outlined.VolumeOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.entities.GroupStatsEntity
import com.example.data.entities.StudentEntity
import com.example.ui.theme.DarkGreenBackground
import com.example.ui.theme.IslamicGoldBright
import com.example.ui.theme.IslamicGoldDark
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.IslamicGoldPrimary
import com.example.ui.theme.IslamicGreenDark
import com.example.ui.theme.IslamicGreenMedium
import com.example.ui.theme.IslamicGreenPrimary

@Composable
fun GroupCounterBanner(
    groupStats: GroupStatsEntity?,
    currentStudent: StudentEntity?,
    activeStudentsCount: Int,
    isVibrationEnabled: Boolean,
    isSoundEnabled: Boolean,
    onStudentClick: () -> Unit,
    onQrClick: () -> Unit,
    onAdminClick: () -> Unit,
    onToggleVibration: () -> Unit,
    onToggleSound: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
        colors = CardDefaults.cardColors(containerColor = IslamicGreenDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Optional subtle decorative banner image background with overlay
            Image(
                painter = painterResource(id = R.drawable.islamic_header_banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.18f,
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // App Title & Tagline
                    Column {
                        Text(
                            text = "معًا نذكر الله",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicGoldBright,
                                fontSize = 22.sp
                            )
                        )
                        Text(
                            text = "المسبحة الإلكترونية التفاعلية الجماعية",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = IslamicGoldLight.copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        )
                    }

                    // Action Icons: QR, Admin, Sound, Vibration
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onToggleVibration,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                                .testTag("toggle_vibration_button")
                        ) {
                            Icon(
                                imageVector = if (isVibrationEnabled) Icons.Filled.Vibration else Icons.Outlined.Vibration,
                                contentDescription = "الاهتزاز",
                                tint = if (isVibrationEnabled) IslamicGoldBright else Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = onQrClick,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(IslamicGoldPrimary.copy(alpha = 0.2f))
                                .testTag("open_qr_dialog_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "رمز QR للانضمام",
                                tint = IslamicGoldBright,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = onAdminClick,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                                .testTag("open_admin_dialog_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = "إدارة المشرف",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Active Student Profile Chip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .border(1.dp, IslamicGoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .clickable { onStudentClick() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("student_profile_chip"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    try {
                                        Color(android.graphics.Color.parseColor(currentStudent?.avatarColorHex ?: "#114B3A"))
                                    } catch (_: Exception) {
                                        IslamicGoldPrimary
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (currentStudent?.name ?: "طالب").take(1),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = currentStudent?.name ?: "اضغط لتسجيل اسمك",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "تسبيحاتك اليوم: ${currentStudent?.totalCount ?: 0}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = IslamicGoldLight,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(IslamicGoldPrimary.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "تغيير الطالب",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = IslamicGoldBright,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Real-time Group Counter Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    IslamicGreenPrimary,
                                    IslamicGreenMedium,
                                    IslamicGreenPrimary
                                )
                            )
                        )
                        .border(1.5.dp, IslamicGoldPrimary.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(IslamicGoldBright)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "مجموع أذكار الحلقة (لحظياً)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = IslamicGoldLight,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            val totalCount = groupStats?.totalGroupCount ?: 0L
                            AnimatedContent(
                                targetState = totalCount,
                                transitionSpec = {
                                    slideInVertically { it } togetherWith slideOutVertically { -it }
                                },
                                label = "group_count_anim"
                            ) { count ->
                                Text(
                                    text = String.format("%,d", count),
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 32.sp
                                    )
                                )
                            }
                        }

                        // Active students badge
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DarkGreenBackground.copy(alpha = 0.6f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Group,
                                        contentDescription = "الطلاب",
                                        tint = IslamicGoldBright,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "$activeStudentsCount طلاب في الحلقة",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "🟢 متصلون الآن",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF81C784),
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
