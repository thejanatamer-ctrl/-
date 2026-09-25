package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.dao.DhikrSummary
import com.example.data.dao.StudentLeaderboardItem
import com.example.data.entities.GroupStatsEntity
import com.example.model.DhikrType
import com.example.ui.theme.IslamicGoldBright
import com.example.ui.theme.IslamicGoldDark
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.IslamicGoldPrimary
import com.example.ui.theme.IslamicGreenDark
import com.example.ui.theme.IslamicGreenMedium
import com.example.ui.theme.IslamicGreenPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DailyReportScreen(
    groupStats: GroupStatsEntity?,
    dhikrBreakdown: List<DhikrSummary>,
    leaderboard: List<StudentLeaderboardItem>,
    onOpenAdminReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val todayFormatted = SimpleDateFormat("EEEE، d MMMM yyyy", Locale("ar")).format(Date())
    val todayTotal = groupStats?.todayGroupCount ?: 0L

    fun generateReportText(): String {
        val sb = StringBuilder()
        sb.append("📊 *تقرير حلقة معًا نذكر الله*\n")
        sb.append("📅 التاريخ: $todayFormatted\n")
        sb.append("📿 مجموع الأذكار اليوم: ${String.format("%,d", todayTotal)} ذكر\n\n")

        sb.append("🔹 *تفاصيل الأذكار المباركة:*\n")
        DhikrType.entries.forEach { dhikr ->
            val count = dhikrBreakdown.firstOrNull { it.dhikrId == dhikr.id }?.totalCount ?: 0
            sb.append("• ${dhikr.arabicText}: $count\n")
        }

        if (leaderboard.isNotEmpty()) {
            sb.append("\n🌟 *لوحة شرف الطلاب:*\n")
            leaderboard.take(5).forEachIndexed { index, student ->
                val medal = when (index) {
                    0 -> "🥇"
                    1 -> "🥈"
                    2 -> "🥉"
                    else -> "▫️"
                }
                sb.append("$medal ${student.studentName}: ${student.count} ذكر\n")
            }
        }

        sb.append("\nنسأل الله القبول والإخلاص في القول والعمل.")
        return sb.toString()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // Hero Summary Card
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = IslamicGreenDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, IslamicGoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(22.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(IslamicGreenMedium, IslamicGreenDark)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Assessment,
                                contentDescription = null,
                                tint = IslamicGoldBright,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "التقرير اليومي لحلقة الذكر",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGoldBright
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = todayFormatted,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = IslamicGoldLight.copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = String.format("%,d", todayTotal),
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 42.sp
                            )
                        )

                        Text(
                            text = "تسبيحة وذكر تم إحصاؤها اليوم",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = IslamicGoldLight
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Share / Copy buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("تقرير حلقة الذكر", generateReportText())
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "تم نسخ التقرير بنجاح!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("copy_report_button"),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "نسخ",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("نسخ التقرير", fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, generateReportText())
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "مشاركة تقرير حلقة الذكر"))
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("share_report_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = IslamicGoldPrimary,
                                    contentColor = Color(0xFF2B2100)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "مشاركة",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("مشاركة التقرير", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Dhikr Breakdown Section
        item {
            Text(
                text = "توزيع الأذكار اليوم:",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = IslamicGreenDark
                )
            )
        }

        // Items for each dhikr
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    DhikrType.entries.forEach { dhikr ->
                        val count = dhikrBreakdown.firstOrNull { it.dhikrId == dhikr.id }?.totalCount ?: 0
                        val fraction = if (todayTotal > 0) (count.toFloat() / todayTotal.toFloat()).coerceIn(0f, 1f) else 0f

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = dhikr.arabicText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGreenDark
                                    )
                                )

                                Text(
                                    text = "$count (${(fraction * 100).toInt()}%)",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGoldDark
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            LinearProgressIndicator(
                                progress = { fraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = IslamicGreenPrimary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Student Leaderboard Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = IslamicGoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ترتيب الطلاب الأكثر ذكراً اليوم:",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreenDark
                        )
                    )
                }
            }
        }

        if (leaderboard.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "لا توجد أذكار مسجلة بعد اليوم، ابدأوا بالتسبيح معاً!",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        } else {
            itemsIndexed(leaderboard) { index, student ->
                val medal = when (index) {
                    0 -> "🥇 المركز الأول"
                    1 -> "🥈 المركز الثاني"
                    2 -> "🥉 المركز الثالث"
                    else -> "المركز ${index + 1}"
                }

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = medal,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (index == 0) IslamicGoldDark else IslamicGreenDark
                                ),
                                modifier = Modifier.width(110.dp)
                            )

                            Text(
                                text = student.studentName,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }

                        Text(
                            text = "${student.count} ذكر",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicGreenPrimary
                            )
                        )
                    }
                }
            }
        }

        // Admin Reset Card at bottom
        item {
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "إدارة الحلقة وبدء جلسة جديدة",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicGreenDark
                            )
                        )
                        Text(
                            text = "تصفير عداد اليوم أو إعادة ضبط شاملة للمعلم والمشرف",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Button(
                        onClick = onOpenAdminReset,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD32F2F),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("admin_reset_report_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "إعادة ضبط",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("إعادة ضبط", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
