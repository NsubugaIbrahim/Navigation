package com.example.navigation

import com.example.navigation.RoutineDao
import com.example.navigation.RoutineEntity
import kotlinx.coroutines.flow.Flow

class RoutineRepository(private val routineDao: RoutineDao) {

    val allRoutines: Flow<List<RoutineEntity>> = routineDao.getAllRoutines()

    suspend fun insertRoutine(routine: RoutineEntity): Long {
        return routineDao.insertRoutine(routine)
    }

    suspend fun updateRoutine(routine: RoutineEntity) {
        routineDao.updateRoutine(routine)
    }

    suspend fun deleteRoutine(id: Int) {
        routineDao.deleteRoutine(id)
    }

    suspend fun getRoutineById(id: Int): RoutineEntity? {
        return routineDao.getRoutineById(id)
    }

    //duplicate Routine
    suspend fun duplicateRoutine(originalId: Int){
        val original = routineDao.getRoutineById(originalId)
        if (original != null){
            val duplicate = original.copy(id = 0, name = original.name + " (Copy) ")
            routineDao.insertRoutine(duplicate)
        }
    }
}
