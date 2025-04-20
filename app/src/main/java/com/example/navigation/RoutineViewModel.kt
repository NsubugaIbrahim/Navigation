package com.example.navigation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.AppDatabase
import com.example.navigation.data.entity.RoutineEntity
import com.example.navigation.data.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RoutineRepository
    val allRoutines: Flow<List<RoutineEntity>>

    init {
        val routineDao = AppDatabase.getDatabase(application).routineDao()
        repository = RoutineRepository(routineDao)
        allRoutines = repository.allRoutines
    }

    fun insertRoutine(routine: RoutineEntity) = viewModelScope.launch {
        repository.insertRoutine(routine)
    }

    fun updateRoutine(routine: RoutineEntity) = viewModelScope.launch {
        repository.updateRoutine(routine)
    }

    fun deleteRoutine(id: Int) = viewModelScope.launch {
        repository.deleteRoutine(id)
    }
}
