package com.example.registroprioridades.data.repository

import com.example.registroprioridades.data.remote.dto.SistemaDto
import com.example.registroprioridades.data.remote.SistemaApi
import retrofit2.Response
import javax.inject.Inject

class SistemaRepository @Inject constructor(
    private val sistemaApi: SistemaApi
) {
    suspend fun saveSistema(sistema: SistemaDto) = sistemaApi.saveSistema(sistema)

    suspend fun getSistema(id: Int) = sistemaApi.getSistema(id)

    suspend fun deleteSistema(id: Int): Response<Void?> = sistemaApi.deleteSistema(id)

    suspend fun getSistemas() = sistemaApi.getAllSistema()
}