package com.example.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "students",
    indices = [Index(value = ["name"], unique = true)]
)
data class StudentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val avatarColorHex: String = "#114B3A",
    val joinedAt: Long = System.currentTimeMillis(),
    val totalCount: Int = 0,
    val lastActive: Long = System.currentTimeMillis()
)
