package com.example.registroprioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.registroprioridades.presentation.navigation.PrioridadNavHost
import com.example.registroprioridades.ui.theme.RegistroPrioridadesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RegistroPrioridadesTheme {
                val navHost = rememberNavController()
                PrioridadNavHost(navHost)
            }
        }
    }
}