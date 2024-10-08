package com.example.registroprioridades.data.remote

import com.example.registroprioridades.data.remote.dto.SistemaDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SistemaApi {

    @GET("api/Sistemas/{id}")
    suspend fun getSistema(@Path("id") id: Int): SistemaDto

    @GET("api/Sistemas")
    suspend fun getAllSistema(): List<SistemaDto>

    @POST("api/Sistemas")
    suspend fun saveSistema(@Body sistemaDto: SistemaDto?): SistemaDto

    @DELETE("api/Sistemas/{id}")
    suspend fun deleteSistema(@Path("id") id: Int)
}