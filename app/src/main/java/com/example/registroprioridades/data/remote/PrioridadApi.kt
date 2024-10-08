package com.example.registroprioridades.data.remote

import com.example.registroprioridades.data.remote.dto.PrioridadDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PrioridadApi {

    @GET("api/Prioridades/{id}")
    suspend fun getPrioridad(@Path("id") id: Int): PrioridadDto

    @GET("api/Prioridades")
    suspend fun getAllPrioridad(): List<PrioridadDto>

    @POST("api/Prioridades")
    suspend fun savePrioridad(@Body prioridadDto: PrioridadDto?): PrioridadDto

    @DELETE("api/Prioridades/{id}")
    suspend fun deletePrioridad(@Path("id") id: Int)
}