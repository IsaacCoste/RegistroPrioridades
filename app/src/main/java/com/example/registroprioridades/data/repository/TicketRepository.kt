package com.example.registroprioridades.data.repository

import com.example.registroprioridades.data.local.dao.TicketDao
import com.example.registroprioridades.data.local.entities.TicketEntity
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val tickectDao: TicketDao
) {
    suspend fun saveTicket(ticket: TicketEntity) = tickectDao.save(ticket)

    suspend fun getTicket(id: Int) = tickectDao.find(id)

    suspend fun searchCliente(cliente: String) = tickectDao.searchCliente(cliente)

    suspend fun searchAsunto(asunto: String) = tickectDao.searchAsunto(asunto)

    suspend fun searchDescripcion(descripcion: String) = tickectDao.searchDescripcion(descripcion)

    suspend fun deleteTicket(ticket: TicketEntity) = tickectDao.delete(ticket)

    fun getTickets() = tickectDao.getAll()
}