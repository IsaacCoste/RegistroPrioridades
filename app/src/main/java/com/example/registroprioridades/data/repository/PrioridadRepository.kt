package com.example.registroprioridades.data.repository

import com.example.registroprioridades.data.remote.dto.PrioridadDto
import com.example.registroprioridades.data.remote.PrioridadApi
import retrofit2.Response
import javax.inject.Inject

class PrioridadRepository @Inject constructor(
    private val prioridadApi: PrioridadApi
) {
    suspend fun savePrioridad(prioridad: PrioridadDto) = prioridadApi.savePrioridad(prioridad)

    suspend fun getPrioridad(id: Int) = prioridadApi.getPrioridad(id)

    suspend fun deletePrioridad(id: Int): Response<Void?> = prioridadApi.deletePrioridad(id)

    suspend fun getPrioridades() = prioridadApi.getAllPrioridad()
}