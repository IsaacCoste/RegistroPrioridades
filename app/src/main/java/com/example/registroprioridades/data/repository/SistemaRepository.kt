package com.example.registroprioridades.data.repository

import com.example.registroprioridades.data.remote.dto.SistemaDto
import com.example.registroprioridades.data.remote.SistemaApi
import javax.inject.Inject

class SistemaRepository @Inject constructor(
    private val sistemaApi: SistemaApi
) {
    suspend fun saveSistema(sistema: SistemaDto) = sistemaApi.saveSistema(sistema)

    suspend fun getSistema(id: Int) = sistemaApi.getSistema(id)

    suspend fun deleteSistema(id: Int) = sistemaApi.deleteSistema(id)

    suspend fun getSistemas() = sistemaApi.getAllSistema()
}