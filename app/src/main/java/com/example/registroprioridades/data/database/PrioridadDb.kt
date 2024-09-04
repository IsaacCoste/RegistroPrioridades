package com.example.registroprioridades.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.registroprioridades.data.dao.PrioridadDao
import com.example.registroprioridades.data.entities.PrioridadEntity

@Database(
    entities = [
        PrioridadEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PrioridadDb : RoomDatabase(){
    abstract fun prioridadDao(): PrioridadDao
}