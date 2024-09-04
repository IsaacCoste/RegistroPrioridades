package com.example.registroprioridades.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import com.example.registroprioridades.data.entities.PrioridadEntity

@Dao
interface PrioridadDao {
    @Upsert()
    suspend fun save(prioridad: PrioridadEntity)
    @Query(
        """
            SELECT * FROM Prioridades 
            WHERE prioridadId = :id
            Limit 1
        """
    )
    suspend fun find(id: Int): PrioridadEntity?
    @Query(
        """
            SELECT * FROM Prioridades
            WHERE descripcion = :descripcion
            LIMIT 1
        """
    )
    suspend fun searchDescripcion(descripcion: String): PrioridadEntity?

    @Delete
    suspend fun delete(prioridad: PrioridadEntity)
    @Query("SELECT * FROM Prioridades")
    fun getAll(): Flow<List<PrioridadEntity>>
}