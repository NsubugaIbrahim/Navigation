package com.example.navigation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1, // Single row for settings
    val name: String = "Kayiwa Rahim",
    val email: String = "kayiwa.rahim@students.mak.ac.ug",
    val autoArm: Boolean = true,
    val notifications: Boolean = false,
    val appColor: Long = 0xFFFFC107
)
