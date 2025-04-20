package com.example.navigation

import com.example.navigation.SettingsDao
import com.example.navigation.SettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val settingsDao: SettingsDao) {

    val settings: Flow<SettingsEntity> = settingsDao.getSettings().map { it ?: SettingsEntity() }

    suspend fun updateSettings(settings: SettingsEntity) {
        if (settingsDao.hasSettings()) {
            settingsDao.updateSettings(settings)
        } else {
            settingsDao.insertSettings(settings)
        }
    }

    suspend fun updateUserName(name: String) {
        val currentSettings = settingsDao.getSettings().map { it ?: SettingsEntity() }
            .collect { settings ->
                val updatedSettings = settings.copy(name = name)
                updateSettings(updatedSettings)
            }
    }

    suspend fun updateUserEmail(email: String) {
        val currentSettings = settingsDao.getSettings().map { it ?: SettingsEntity() }
            .collect { settings ->
                val updatedSettings = settings.copy(email = email)
                updateSettings(updatedSettings)
            }
    }

    suspend fun updateAutoArm(enabled: Boolean) {
        val currentSettings = settingsDao.getSettings().map { it ?: SettingsEntity() }
            .collect { settings ->
                val updatedSettings = settings.copy(autoArm = enabled)
                updateSettings(updatedSettings)
            }
    }

    suspend fun updateAppNotifications(enabled: Boolean) {
        val currentSettings = settingsDao.getSettings().map { it ?: SettingsEntity() }
            .collect { settings ->
                val updatedSettings = settings.copy(notifications = enabled)
                updateSettings(updatedSettings)
            }
    }

    suspend fun updateAppColor(color: Long) {
        val currentSettings = settingsDao.getSettings().map { it ?: SettingsEntity() }
            .collect { settings ->
                val updatedSettings = settings.copy(appColor = color)
                updateSettings(updatedSettings)
            }
    }
}
