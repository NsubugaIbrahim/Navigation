package com.example.navigation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.AppDatabase
import com.example.navigation.SettingsEntity
import com.example.navigation.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SettingsRepository
    val settings: Flow<SettingsEntity>

    init {
        val settingsDao = AppDatabase.getDatabase(application).settingsDao()
        repository = SettingsRepository(settingsDao)
        settings = repository.settings
    }

    fun updateSettings(settings: SettingsEntity) = viewModelScope.launch {
        repository.updateSettings(settings)
    }

    fun updateUserName(name: String) = viewModelScope.launch {
        repository.updateUserName(name)
    }

    fun updateUserEmail(email: String) = viewModelScope.launch {
        repository.updateUserEmail(email)
    }

    fun updateAutoArm(enabled: Boolean) = viewModelScope.launch {
        repository.updateAutoArm(enabled)
    }

    fun updateAppNotifications(enabled: Boolean) = viewModelScope.launch {
        repository.updateAppNotifications(enabled)
    }

    fun updateAppColor(color: Long) = viewModelScope.launch {
        repository.updateAppColor(color)
    }
}
