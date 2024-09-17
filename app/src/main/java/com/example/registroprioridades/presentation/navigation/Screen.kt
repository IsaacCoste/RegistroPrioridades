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
    @Serializable
    data object TicketList : Screen()
    @Serializable
    data class Ticket(val ticketId: Int) : Screen()
    @Serializable
    data class TicketEdit(val ticketId: Int) : Screen()
    @Serializable
    data class TicketDelete(val ticketId: Int) : Screen()
    @Serializable
    data object HomeScreen : Screen()


}