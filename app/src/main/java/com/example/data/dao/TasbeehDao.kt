package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entities.DhikrLogEntity
import com.example.data.entities.GroupStatsEntity
import com.example.data.entities.StudentEntity
import kotlinx.coroutines.flow.Flow

data class DhikrSummary(
    val dhikrId: String,
    val totalCount: Int
)

data class StudentLeaderboardItem(
    val studentId: Long,
    val studentName: String,
    val avatarColorHex: String,
    val count: Int
)

@Dao
interface TasbeehDao {

    // Students
    @Query("SELECT * FROM students ORDER BY totalCount DESC, lastActive DESC")
    fun getAllStudentsFlow(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    suspend fun getStudentById(id: Long): StudentEntity?

    @Query("SELECT * FROM students WHERE name = :name LIMIT 1")
    suspend fun getStudentByName(name: String): StudentEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(student: StudentEntity): Long

    @Update
    suspend fun updateStudent(student: StudentEntity)

    @Query("UPDATE students SET totalCount = totalCount + :delta, lastActive = :now WHERE id = :studentId")
    suspend fun incrementStudentCount(studentId: Long, delta: Int, now: Long = System.currentTimeMillis())

    @Query("UPDATE students SET totalCount = 0")
    suspend fun resetAllStudentCounts()

    @Query("DELETE FROM students WHERE id = :id")
    suspend fun deleteStudent(id: Long)

    // Dhikr Logs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: DhikrLogEntity)

    @Query("SELECT * FROM dhikr_logs WHERE dateKey = :dateKey ORDER BY timestamp DESC")
    fun getLogsForDateFlow(dateKey: String): Flow<List<DhikrLogEntity>>

    @Query("SELECT dhikrId, SUM(count) as totalCount FROM dhikr_logs WHERE dateKey = :dateKey GROUP BY dhikrId")
    fun getDhikrBreakdownForDateFlow(dateKey: String): Flow<List<DhikrSummary>>

    @Query("SELECT studentId, studentName, '' as avatarColorHex, SUM(count) as count FROM dhikr_logs WHERE dateKey = :dateKey GROUP BY studentId, studentName ORDER BY count DESC")
    fun getStudentLeaderboardForDateFlow(dateKey: String): Flow<List<StudentLeaderboardItem>>

    @Query("SELECT SUM(count) FROM dhikr_logs WHERE dateKey = :dateKey")
    fun getTodayTotalCountFlow(dateKey: String): Flow<Long?>

    @Query("DELETE FROM dhikr_logs")
    suspend fun clearAllLogs()

    @Query("DELETE FROM dhikr_logs WHERE dateKey = :dateKey")
    suspend fun clearLogsForDate(dateKey: String)

    // Group Stats
    @Query("SELECT * FROM group_stats WHERE id = 1 LIMIT 1")
    fun getGroupStatsFlow(): Flow<GroupStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateGroupStats(stats: GroupStatsEntity)

    @Query("UPDATE group_stats SET totalGroupCount = totalGroupCount + :delta, todayGroupCount = todayGroupCount + :delta WHERE id = 1")
    suspend fun incrementGroupCount(delta: Long)

    @Query("UPDATE group_stats SET totalGroupCount = 0, todayGroupCount = 0, lastResetTimestamp = :now, lastResetBy = :adminName WHERE id = 1")
    suspend fun resetGroupStats(now: Long, adminName: String)

    @Query("UPDATE group_stats SET todayGroupCount = 0, todayDateKey = :newDateKey WHERE id = 1")
    suspend fun resetTodayCountOnly(newDateKey: String)
}
