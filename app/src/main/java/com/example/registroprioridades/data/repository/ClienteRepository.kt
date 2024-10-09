package com.example.registroprioridades.data.repository

import com.example.registroprioridades.data.remote.ClienteApi
import com.example.registroprioridades.data.remote.dto.ClienteDto
import retrofit2.Response
import javax.inject.Inject

class ClienteRepository @Inject constructor(
    private val clienteApi: ClienteApi
) {
    suspend fun saveCliente(cliente: ClienteDto) = clienteApi.saveCliente(cliente)

    suspend fun getCliente(id: Int) = clienteApi.getCliente(id)

    suspend fun deleteCliente(id: Int): Response<Void?> = clienteApi.deleteCliente(id)

    suspend fun getClientes() = clienteApi.getAllCliente()

}