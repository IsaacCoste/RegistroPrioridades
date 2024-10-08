package com.example.registroprioridades.data.di

import android.content.Context
import androidx.room.Room
import com.example.registroprioridades.data.local.database.PrioridadDb
import com.example.registroprioridades.data.remote.ClienteApi
import com.example.registroprioridades.data.remote.PrioridadApi
import com.example.registroprioridades.data.remote.SistemaApi
import com.example.registroprioridades.data.remote.TicketApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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


    const val BASE_URL = "https://prioridadesapi-gsghccathnd7fbf6.canadacentral-01.azurewebsites.net/"

    @Singleton
    @Provides
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(DateAdapter())
            .build()

    @Provides
    @Singleton
    fun provicesPrioridadApi(moshi: Moshi): PrioridadApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PrioridadApi::class.java)
    }
    @Provides
    @Singleton
    fun provicesClienteApi(moshi: Moshi): ClienteApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ClienteApi::class.java)
    }
    @Provides
    @Singleton
    fun provicesSistemaApi(moshi: Moshi): SistemaApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(SistemaApi::class.java)
    }
    @Provides
    @Singleton
    fun provicesTicketApi(moshi: Moshi): TicketApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TicketApi::class.java)
    }

    @Provides
    @Singleton
    fun providePrioridadDao(prioridadDb: PrioridadDb) = prioridadDb.prioridadDao()
    @Provides
    @Singleton
    fun provideTicketDao(prioridadDb: PrioridadDb) = prioridadDb.ticketDao()
}