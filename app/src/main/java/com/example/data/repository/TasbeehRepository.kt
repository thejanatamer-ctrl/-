package com.example.data.repository

import com.example.data.dao.DhikrSummary
import com.example.data.dao.StudentLeaderboardItem
import com.example.data.dao.TasbeehDao
import com.example.data.entities.DhikrLogEntity
import com.example.data.entities.GroupStatsEntity
import com.example.data.entities.StudentEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TasbeehRepository(private val dao: TasbeehDao) {

    val allStudents: Flow<List<StudentEntity>> = dao.getAllStudentsFlow()
    val groupStats: Flow<GroupStatsEntity?> = dao.getGroupStatsFlow()

    private fun getTodayDateKey(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date())
    }

    fun getTodayLogs(): Flow<List<DhikrLogEntity>> {
        return dao.getLogsForDateFlow(getTodayDateKey())
    }

    fun getTodayDhikrBreakdown(): Flow<List<DhikrSummary>> {
        return dao.getDhikrBreakdownForDateFlow(getTodayDateKey())
    }

    fun getTodayStudentLeaderboard(): Flow<List<StudentLeaderboardItem>> {
        return dao.getStudentLeaderboardForDateFlow(getTodayDateKey())
    }

    suspend fun getOrCreateStudent(name: String, avatarColorHex: String): StudentEntity {
        val trimmed = name.trim().ifEmpty { "طالب الذكر" }
        val existing = dao.getStudentByName(trimmed)
        if (existing != null) {
            return existing
        }
        val newStudent = StudentEntity(
            name = trimmed,
            avatarColorHex = avatarColorHex,
            joinedAt = System.currentTimeMillis()
        )
        val newId = dao.insertStudent(newStudent)
        return dao.getStudentById(newId) ?: newStudent.copy(id = newId)
    }

    suspend fun recordDhikrTap(studentId: Long, studentName: String, dhikrId: String, delta: Int = 1) {
        val today = getTodayDateKey()
        val now = System.currentTimeMillis()

        // 1. Increment student personal count
        dao.incrementStudentCount(studentId, delta, now)

        // 2. Increment group total count
        dao.incrementGroupCount(delta.toLong())

        // 3. Insert individual log record
        dao.insertLog(
            DhikrLogEntity(
                studentId = studentId,
                studentName = studentName,
                dhikrId = dhikrId,
                count = delta,
                dateKey = today,
                timestamp = now
            )
        )
    }

    suspend fun adminReset(resetStudents: Boolean, adminName: String = "المشرف") {
        val now = System.currentTimeMillis()
        dao.resetGroupStats(now, adminName)
        if (resetStudents) {
            dao.resetAllStudentCounts()
            dao.clearAllLogs()
        }
    }
}
