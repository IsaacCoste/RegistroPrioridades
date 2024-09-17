package com.example.registroprioridades.data.di

import android.content.Context
import androidx.room.Room
import com.example.registroprioridades.data.local.database.PrioridadDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun providePrioridadDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            PrioridadDb::class.java,
            "Prioridad.db"
        ).fallbackToDestructiveMigration()
            .build()
    @Provides
    @Singleton
    fun providePrioridadDao(prioridadDb: PrioridadDb) = prioridadDb.prioridadDao()
    @Provides
    @Singleton
    fun provideTicketDao(prioridadDb: PrioridadDb) = prioridadDb.ticketDao()
}