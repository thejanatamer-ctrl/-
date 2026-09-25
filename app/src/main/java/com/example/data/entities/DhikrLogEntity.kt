package com.example.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dhikr_logs",
    indices = [
        Index(value = ["studentId"]),
        Index(value = ["dateKey"]),
        Index(value = ["dhikrId"])
    ]
)
data class DhikrLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val dhikrId: String,
    val count: Int,
    val dateKey: String, // Format: YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis()
)
