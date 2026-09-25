package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.TasbeehDao
import com.example.data.entities.DhikrLogEntity
import com.example.data.entities.GroupStatsEntity
import com.example.data.entities.StudentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        StudentEntity::class,
        DhikrLogEntity::class,
        GroupStatsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tasbeehDao(): TasbeehDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tasbeeh_community_db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.tasbeehDao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: TasbeehDao) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val todayStr = dateFormat.format(Date())

                // Initialize singleton group stats
                dao.insertOrUpdateGroupStats(
                    GroupStatsEntity(
                        id = 1,
                        totalGroupCount = 1420L,
                        todayGroupCount = 385L,
                        todayDateKey = todayStr,
                        lastResetTimestamp = 0L,
                        lastResetBy = "المشرف"
                    )
                )

                // Populate active default classmates to simulate a vibrant class halaqah
                val initialStudents = listOf(
                    StudentEntity(name = "أحمد محمد", avatarColorHex = "#114B3A", totalCount = 450),
                    StudentEntity(name = "فاطمة الزهراء", avatarColorHex = "#D4AF37", totalCount = 380),
                    StudentEntity(name = "عبد الله عمر", avatarColorHex = "#1E6B53", totalCount = 320),
                    StudentEntity(name = "مريم خالد", avatarColorHex = "#8C6B1C", totalCount = 270)
                )

                initialStudents.forEach { student ->
                    val studentId = dao.insertStudent(student)
                    val idToUse = if (studentId > 0) studentId else 1L
                    // add initial dhikr log for today
                    dao.insertLog(
                        DhikrLogEntity(
                            studentId = idToUse,
                            studentName = student.name,
                            dhikrId = "subhanallah",
                            count = (student.totalCount * 0.4).toInt(),
                            dateKey = todayStr
                        )
                    )
                    dao.insertLog(
                        DhikrLogEntity(
                            studentId = idToUse,
                            studentName = student.name,
                            dhikrId = "salawat",
                            count = (student.totalCount * 0.6).toInt(),
                            dateKey = todayStr
                        )
                    )
                }
            }
        }
    }
}
