package com.example.registroprioridades.data.repository

import com.example.registroprioridades.data.remote.dto.TicketDto
import com.example.registroprioridades.data.remote.TicketApi
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val tickectApi: TicketApi
) {
    suspend fun saveTicket(ticket: TicketDto) = tickectApi.saveTicket(ticket)

    suspend fun getTicket(id: Int) = tickectApi.getTickets(id)

    suspend fun deleteTicket(id: Int) = tickectApi.deleteTicket(id)

    suspend fun getTickets() = tickectApi.getAllTicket()
}