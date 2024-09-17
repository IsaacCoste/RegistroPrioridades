package com.example.registroprioridades.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.registroprioridades.data.local.dao.PrioridadDao
import com.example.registroprioridades.data.local.dao.TicketDao
import com.example.registroprioridades.data.local.entities.PrioridadEntity
import com.example.registroprioridades.data.local.entities.TicketEntity

@Database(
    entities = [
        PrioridadEntity::class,
        TicketEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PrioridadDb : RoomDatabase(){
    abstract fun prioridadDao(): PrioridadDao
    abstract fun ticketDao(): TicketDao
}