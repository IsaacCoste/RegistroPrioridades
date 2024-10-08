package com.example.registroprioridades.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object HomeScreen : Screen()
    @Serializable
    data object PrioridadList : Screen()
    @Serializable
    data class Prioridad(val prioridadId: Int, val isPrioridadDelete: Boolean) : Screen()
    @Serializable
    data object TicketList : Screen()
    @Serializable
    data class Ticket(val ticketId: Int, val isTicketDelete: Boolean) : Screen()
    @Serializable
    data object SistemaList : Screen()
    @Serializable
    data class Sistema(val sistemaId: Int, val isSistemaDelete: Boolean) : Screen()
    @Serializable
    data object ClienteList : Screen()
    @Serializable
    data class Cliente(val clienteId: Int, val isClienteDelete: Boolean) : Screen()
}