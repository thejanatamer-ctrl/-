package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.DhikrType
import com.example.ui.components.AdminResetDialog
import com.example.ui.components.CelebrationDialog
import com.example.ui.components.DhikrSelector
import com.example.ui.components.GroupCounterBanner
import com.example.ui.components.QrCodeDialog
import com.example.ui.components.StudentDialog
import com.example.ui.components.TasbeehButton
import com.example.ui.components.VirtueDialog
import com.example.ui.theme.IslamicGoldBright
import com.example.ui.theme.IslamicGoldDark
import com.example.ui.theme.IslamicGoldPrimary
import com.example.ui.theme.IslamicGreenDark
import com.example.ui.theme.IslamicGreenPrimary
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.TasbeehViewModel

@Composable
fun TasbeehApp(
    viewModel: TasbeehViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val allStudents by viewModel.allStudents.collectAsStateWithLifecycle()
    val groupStats by viewModel.groupStats.collectAsStateWithLifecycle()
    val dhikrBreakdown by viewModel.dhikrBreakdown.collectAsStateWithLifecycle()
    val studentLeaderboard by viewModel.studentLeaderboard.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("app_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = uiState.activeTab == AppTab.TASBEEH,
                    onClick = { viewModel.selectTab(AppTab.TASBEEH) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.activeTab == AppTab.TASBEEH) Icons.Filled.RadioButtonChecked else Icons.Outlined.RadioButtonUnchecked,
                            contentDescription = "المسبحة"
                        )
                    },
                    label = { Text("المسبحة", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = IslamicGreenPrimary,
                        selectedTextColor = IslamicGreenPrimary,
                        indicatorColor = IslamicGoldPrimary.copy(alpha = 0.25f)
                    ),
                    modifier = Modifier.testTag("nav_tasbeeh_tab")
                )

                NavigationBarItem(
                    selected = uiState.activeTab == AppTab.GROUP_CIRCLE,
                    onClick = { viewModel.selectTab(AppTab.GROUP_CIRCLE) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.activeTab == AppTab.GROUP_CIRCLE) Icons.Filled.Group else Icons.Outlined.Group,
                            contentDescription = "حلقة الطلاب"
                        )
                    },
                    label = { Text("حلقة الطلاب", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = IslamicGreenPrimary,
                        selectedTextColor = IslamicGreenPrimary,
                        indicatorColor = IslamicGoldPrimary.copy(alpha = 0.25f)
                    ),
                    modifier = Modifier.testTag("nav_circle_tab")
                )

                NavigationBarItem(
                    selected = uiState.activeTab == AppTab.DAILY_REPORT,
                    onClick = { viewModel.selectTab(AppTab.DAILY_REPORT) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.activeTab == AppTab.DAILY_REPORT) Icons.Filled.Assessment else Icons.Outlined.Assessment,
                            contentDescription = "تقرير اليوم"
                        )
                    },
                    label = { Text("تقرير اليوم", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = IslamicGreenPrimary,
                        selectedTextColor = IslamicGreenPrimary,
                        indicatorColor = IslamicGoldPrimary.copy(alpha = 0.25f)
                    ),
                    modifier = Modifier.testTag("nav_report_tab")
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Persistent Top Header & Group Counter Banner across all screens
            GroupCounterBanner(
                groupStats = groupStats,
                currentStudent = uiState.currentStudent,
                activeStudentsCount = allStudents.size,
                isVibrationEnabled = uiState.isVibrationEnabled,
                isSoundEnabled = uiState.isSoundEnabled,
                onStudentClick = { viewModel.setShowStudentDialog(true) },
                onQrClick = { viewModel.setShowQrDialog(true) },
                onAdminClick = { viewModel.setShowAdminResetDialog(true) },
                onToggleVibration = { viewModel.toggleVibration() },
                onToggleSound = { viewModel.toggleSound() }
            )

            // Screen content according to active tab
            when (uiState.activeTab) {
                AppTab.TASBEEH -> {
                    TasbeehContentScreen(
                        selectedDhikr = uiState.selectedDhikr,
                        currentDhikrCount = uiState.currentDhikrCount,
                        lapCount = uiState.lapCompletedCount,
                        recentActivities = uiState.recentActivities,
                        onDhikrSelected = { viewModel.selectDhikr(it) },
                        onTasbeehTap = { viewModel.onTasbeehTap() },
                        onResetLap = { viewModel.resetCurrentLapCounter() },
                        onShowVirtue = { viewModel.setShowVirtueDialog(true) },
                        modifier = Modifier.weight(1f)
                    )
                }

                AppTab.GROUP_CIRCLE -> {
                    GroupCircleScreen(
                        students = allStudents,
                        currentStudent = uiState.currentStudent,
                        isLiveSimulationActive = uiState.isLiveHalaqahActive,
                        recentActivities = uiState.recentActivities,
                        totalGroupCount = groupStats?.totalGroupCount ?: 0L,
                        onToggleLiveSimulation = { viewModel.toggleLiveHalaqah() },
                        onAddStudentClick = { viewModel.setShowStudentDialog(true) },
                        onOpenQrClick = { viewModel.setShowQrDialog(true) },
                        onSelectStudent = { student ->
                            viewModel.switchOrAddStudent(student.name, student.avatarColorHex)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                AppTab.DAILY_REPORT -> {
                    DailyReportScreen(
                        groupStats = groupStats,
                        dhikrBreakdown = dhikrBreakdown,
                        leaderboard = studentLeaderboard,
                        onOpenAdminReset = { viewModel.setShowAdminResetDialog(true) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    // Dialogs
    if (uiState.showStudentDialog) {
        StudentDialog(
            currentStudent = uiState.currentStudent,
            existingStudents = allStudents,
            onDismiss = { viewModel.setShowStudentDialog(false) },
            onStudentSelected = { name, colorHex ->
                viewModel.switchOrAddStudent(name, colorHex)
            }
        )
    }

    if (uiState.showQrDialog) {
        QrCodeDialog(
            onDismiss = { viewModel.setShowQrDialog(false) }
        )
    }

    if (uiState.showAdminResetDialog) {
        AdminResetDialog(
            onDismiss = { viewModel.setShowAdminResetDialog(false) },
            onConfirmReset = { resetAll, adminName ->
                viewModel.performAdminReset(resetAll, adminName)
            }
        )
    }

    if (uiState.showVirtueDialog) {
        VirtueDialog(
            dhikr = uiState.selectedDhikr,
            onDismiss = { viewModel.setShowVirtueDialog(false) }
        )
    }

    if (uiState.showTargetCelebration) {
        CelebrationDialog(
            message = uiState.celebrationMessage,
            onDismiss = { viewModel.dismissCelebration() }
        )
    }
}

@Composable
fun TasbeehContentScreen(
    selectedDhikr: DhikrType,
    currentDhikrCount: Int,
    lapCount: Int,
    recentActivities: List<String>,
    onDhikrSelected: (DhikrType) -> Unit,
    onTasbeehTap: () -> Unit,
    onResetLap: () -> Unit,
    onShowVirtue: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Dhikr selection chips
        DhikrSelector(
            selectedDhikr = selectedDhikr,
            onDhikrSelected = onDhikrSelected,
            onShowVirtue = onShowVirtue
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Centerpiece Large Tasbeeh Button
        TasbeehButton(
            dhikr = selectedDhikr,
            currentCount = currentDhikrCount,
            lapCount = lapCount,
            onTap = onTasbeehTap,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Lap controls & Reset lap
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(IslamicGoldPrimary)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "دورات مكتملة: $lapCount",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = IslamicGreenDark
                    )
                )
            }

            OutlinedButton(
                onClick = onResetLap,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("reset_lap_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "تصفير الدورة",
                    modifier = Modifier.size(16.dp),
                    tint = IslamicGoldDark
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "تصفير الدورة",
                    fontSize = 12.sp,
                    color = IslamicGoldDark
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Live Ticker Pill (shows recent student action)
        if (recentActivities.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(IslamicGreenPrimary.copy(alpha = 0.08f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✨ ${recentActivities.first()}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = IslamicGreenDark,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    ),
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
    }
}
