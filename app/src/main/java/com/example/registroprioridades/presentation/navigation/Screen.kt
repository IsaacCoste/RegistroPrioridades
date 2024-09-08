package com.example.registroprioridades.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object PrioridadList : Screen()
    @Serializable
    data class Prioridad(val prioridadId: Int) : Screen()
    @Serializable
    data class PrioridadEdit(val prioridadId: Int) : Screen()
    @Serializable
    data class PrioridadDelete(val prioridadId: Int) : Screen()

}