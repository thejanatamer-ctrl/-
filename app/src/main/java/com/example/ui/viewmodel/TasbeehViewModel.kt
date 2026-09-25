package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.dao.DhikrSummary
import com.example.data.dao.StudentLeaderboardItem
import com.example.data.entities.DhikrLogEntity
import com.example.data.entities.GroupStatsEntity
import com.example.data.entities.StudentEntity
import com.example.data.repository.TasbeehRepository
import com.example.model.DhikrType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class AppTab {
    TASBEEH,
    GROUP_CIRCLE,
    DAILY_REPORT
}

data class TasbeehUiState(
    val currentStudent: StudentEntity? = null,
    val selectedDhikr: DhikrType = DhikrType.SUBHANALLAH,
    val currentDhikrCount: Int = 0,
    val lapCompletedCount: Int = 0,
    val isVibrationEnabled: Boolean = true,
    val isSoundEnabled: Boolean = true,
    val isLiveHalaqahActive: Boolean = true,
    val activeTab: AppTab = AppTab.TASBEEH,
    val recentActivities: List<String> = emptyList(),
    val showStudentDialog: Boolean = false,
    val showQrDialog: Boolean = false,
    val showAdminResetDialog: Boolean = false,
    val showVirtueDialog: Boolean = false,
    val showTargetCelebration: Boolean = false,
    val celebrationMessage: String = ""
)

class TasbeehViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TasbeehRepository
    private var simulationJob: Job? = null

    private val _uiState = MutableStateFlow(TasbeehUiState())
    val uiState: StateFlow<TasbeehUiState> = _uiState.asStateFlow()

    val allStudents: StateFlow<List<StudentEntity>>
    val groupStats: StateFlow<GroupStatsEntity?>
    val todayLogs: StateFlow<List<DhikrLogEntity>>
    val dhikrBreakdown: StateFlow<List<DhikrSummary>>
    val studentLeaderboard: StateFlow<List<StudentLeaderboardItem>>

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            application.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = TasbeehRepository(db.tasbeehDao())

        allStudents = repository.allStudents.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        groupStats = repository.groupStats.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        todayLogs = repository.getTodayLogs().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        dhikrBreakdown = repository.getTodayDhikrBreakdown().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        studentLeaderboard = repository.getTodayStudentLeaderboard().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        // Observe students to initialize active student
        viewModelScope.launch {
            allStudents.collect { students ->
                if (students.isNotEmpty() && _uiState.value.currentStudent == null) {
                    _uiState.value = _uiState.value.copy(currentStudent = students.first())
                }
            }
        }

        // Start live halaqah peer simulation
        startLiveHalaqahSimulation()
    }

    fun selectTab(tab: AppTab) {
        _uiState.value = _uiState.value.copy(activeTab = tab)
    }

    fun selectDhikr(dhikr: DhikrType) {
        _uiState.value = _uiState.value.copy(
            selectedDhikr = dhikr,
            currentDhikrCount = 0,
            lapCompletedCount = 0
        )
    }

    fun onTasbeehTap() {
        val student = _uiState.value.currentStudent ?: return
        val currentDhikr = _uiState.value.selectedDhikr

        val newCount = _uiState.value.currentDhikrCount + 1
        var newLapCount = _uiState.value.lapCompletedCount
        var showCelebration = false
        var celebrationMsg = ""

        if (newCount >= currentDhikr.target) {
            newLapCount += 1
            showCelebration = true
            celebrationMsg = "تقبل الله طاعتكم! أتممتم ${currentDhikr.target} من ${currentDhikr.arabicText}"
            triggerCelebrationHaptic()
        } else {
            triggerStandardHaptic()
        }

        _uiState.value = _uiState.value.copy(
            currentDhikrCount = if (newCount >= currentDhikr.target) 0 else newCount,
            lapCompletedCount = newLapCount,
            showTargetCelebration = showCelebration,
            celebrationMessage = celebrationMsg
        )

        // Asynchronously persist tap to Room database
        viewModelScope.launch {
            repository.recordDhikrTap(
                studentId = student.id,
                studentName = student.name,
                dhikrId = currentDhikr.id,
                delta = 1
            )
            // Update local student reference
            _uiState.value = _uiState.value.copy(
                currentStudent = student.copy(totalCount = student.totalCount + 1)
            )
        }
    }

    fun dismissCelebration() {
        _uiState.value = _uiState.value.copy(showTargetCelebration = false)
    }

    fun resetCurrentLapCounter() {
        _uiState.value = _uiState.value.copy(
            currentDhikrCount = 0,
            lapCompletedCount = 0
        )
    }

    fun switchOrAddStudent(name: String, avatarColorHex: String = "#114B3A") {
        viewModelScope.launch {
            val student = repository.getOrCreateStudent(name, avatarColorHex)
            _uiState.value = _uiState.value.copy(
                currentStudent = student,
                showStudentDialog = false
            )
            addActivity("انضم ${student.name} إلى حلقة الذكر")
        }
    }

    fun toggleVibration() {
        _uiState.value = _uiState.value.copy(isVibrationEnabled = !_uiState.value.isVibrationEnabled)
    }

    fun toggleSound() {
        _uiState.value = _uiState.value.copy(isSoundEnabled = !_uiState.value.isSoundEnabled)
    }

    fun toggleLiveHalaqah() {
        val newState = !_uiState.value.isLiveHalaqahActive
        _uiState.value = _uiState.value.copy(isLiveHalaqahActive = newState)
        if (newState) {
            startLiveHalaqahSimulation()
        } else {
            simulationJob?.cancel()
        }
    }

    fun setShowStudentDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showStudentDialog = show)
    }

    fun setShowQrDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showQrDialog = show)
    }

    fun setShowAdminResetDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showAdminResetDialog = show)
    }

    fun setShowVirtueDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showVirtueDialog = show)
    }

    fun performAdminReset(resetAllStudents: Boolean, adminName: String) {
        viewModelScope.launch {
            repository.adminReset(resetAllStudents, adminName)
            _uiState.value = _uiState.value.copy(
                currentDhikrCount = 0,
                lapCompletedCount = 0,
                showAdminResetDialog = false
            )
            addActivity("قام $adminName بإعادة ضبط عداد الحلقة")
        }
    }

    private fun addActivity(message: String) {
        val current = _uiState.value.recentActivities.toMutableList()
        current.add(0, message)
        if (current.size > 8) {
            current.removeAt(current.size - 1)
        }
        _uiState.value = _uiState.value.copy(recentActivities = current)
    }

    private fun triggerStandardHaptic() {
        if (!_uiState.value.isVibrationEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(35)
            }
        } catch (_: Exception) {}
    }

    private fun triggerCelebrationHaptic() {
        if (!_uiState.value.isVibrationEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 60, 50, 80, 50, 120)
                val amplitudes = intArrayOf(0, 180, 0, 220, 0, 255)
                vibrator?.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 60, 50, 80, 50, 120), -1)
            }
        } catch (_: Exception) {}
    }

    private fun startLiveHalaqahSimulation() {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            while (isActive) {
                // Natural random intervals between 4 and 8 seconds
                delay(Random.nextLong(4000, 8000))
                val students = allStudents.value
                val current = _uiState.value.currentStudent
                val peers = students.filter { it.id != current?.id }
                if (peers.isNotEmpty()) {
                    val peer = peers.random()
                    val randomDhikr = DhikrType.entries.random()
                    val delta = Random.nextInt(1, 4)
                    repository.recordDhikrTap(peer.id, peer.name, randomDhikr.id, delta)
                    addActivity("${peer.name} سبّح: ${randomDhikr.arabicText} (+$delta)")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        simulationJob?.cancel()
    }
}
