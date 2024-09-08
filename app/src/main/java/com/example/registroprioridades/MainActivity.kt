package com.example.registroprioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.registroprioridades.data.database.PrioridadDb
import com.example.registroprioridades.presentation.navigation.PrioridadNavHost
import com.example.registroprioridades.ui.theme.RegistroPrioridadesTheme

class MainActivity : ComponentActivity() {
    private lateinit var prioridadDb: PrioridadDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        prioridadDb = Room.databaseBuilder(
            applicationContext,
            PrioridadDb::class.java,
            "Prioridad.db"
        ).fallbackToDestructiveMigration()
            .build()

        setContent {
            RegistroPrioridadesTheme {
                val navHost = rememberNavController()
                PrioridadNavHost(prioridadDb, navHost)
            }
        }
    }
}