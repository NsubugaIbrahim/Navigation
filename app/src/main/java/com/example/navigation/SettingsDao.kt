package com.example.navigation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.navigation.SettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings WHERE id = 1")
    fun getSettings(): Flow<SettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: SettingsEntity)

    @Update
    suspend fun updateSettings(settings: SettingsEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM settings WHERE id = 1)")
    suspend fun hasSettings(): Boolean
}
