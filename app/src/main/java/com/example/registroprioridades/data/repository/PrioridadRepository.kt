package com.example.registroprioridades.data.repository

import com.example.registroprioridades.data.local.dao.PrioridadDao
import com.example.registroprioridades.data.local.entities.PrioridadEntity
import javax.inject.Inject

class PrioridadRepository @Inject constructor(
    private val prioridadDao: PrioridadDao
) {
    suspend fun savePrioridad(prioridad: PrioridadEntity) = prioridadDao.save(prioridad)

    suspend fun getPrioridad(id: Int) = prioridadDao.find(id)

    suspend fun deletePrioridad(prioridad: PrioridadEntity) = prioridadDao.delete(prioridad)

    fun getPrioridades() = prioridadDao.getAll()
}