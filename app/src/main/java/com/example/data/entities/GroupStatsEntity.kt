package com.example.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group_stats")
data class GroupStatsEntity(
    @PrimaryKey val id: Int = 1,
    val totalGroupCount: Long = 0L,
    val todayGroupCount: Long = 0L,
    val todayDateKey: String = "",
    val lastResetTimestamp: Long = 0L,
    val lastResetBy: String = "المشرف"
)
