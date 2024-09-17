package com.example.registroprioridades.presentation.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registroprioridades.data.local.entities.PrioridadEntity
import com.example.registroprioridades.data.local.entities.TicketEntity
import com.example.registroprioridades.data.repository.PrioridadRepository
import com.example.registroprioridades.data.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val prioridadRepository: PrioridadRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTickets()
        getPrioridades()
    }

    fun save() {
        viewModelScope.launch {
            val errorMessage = validate()
            if (errorMessage != null) {
                _uiState.update { it.copy(errorMessage = errorMessage) }
            } else {
                ticketRepository.saveTicket(_uiState.value.toEntity())
                nuevo()
            }
        }
    }

    private fun validate(): String? {
        return when {
            _uiState.value.cliente.isBlank() -> "El nombre del cliente no puede estar vacío"
            _uiState.value.asunto.isBlank() -> "El asunto no puede estar vacío"
            _uiState.value.descripcion.isBlank() -> "La descripción no puede estar vacía"
            _uiState.value.fecha == null -> "La fecha no puede estar vacía"
            else -> null
        }
    }

    fun nuevo() {
        _uiState.update {
            it.copy(
                ticketId = null,
                fecha = Date(),
                cliente = "",
                asunto = "",
                descripcion = "",
                prioridadId = 0,
                errorMessage = null
            )
        }
    }

    fun selectedTicket(ticketId: Int) {
        viewModelScope.launch {
            if (ticketId > 0) {
                val ticket = ticketRepository.getTicket(ticketId)
                _uiState.update {
                    it.copy(
                        ticketId = ticket?.ticketId,
                        fecha = ticket?.fecha,
                        prioridadId = ticket?.prioridadId ?: 0,
                        cliente = ticket?.cliente ?: "",
                        asunto = ticket?.asunto ?: "",
                        descripcion = ticket?.descripcion ?: ""
                    )
                }
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            ticketRepository.deleteTicket(_uiState.value.toEntity())
        }
    }

    private fun getTickets() {
        viewModelScope.launch {
            ticketRepository.getTickets().collect { tickets ->
                _uiState.update {
                    it.copy(tickets = tickets)
                }
            }
        }
    }
    private fun getPrioridades() {
        viewModelScope.launch {
            prioridadRepository.getPrioridades().collect { prioridades ->
                _uiState.update {
                    it.copy(prioridades = prioridades)
                }
            }
        }
    }

    fun onTicketIdChange(ticketId: Int) {
        _uiState.update {
            it.copy(ticketId = ticketId)
        }
    }
    fun onClienteChange(cliente: String) {
        _uiState.update {
            it.copy(cliente = cliente, errorMessage = null)
        }
    }

    fun onAsuntoChange(asunto: String) {
        _uiState.update {
            it.copy(asunto = asunto, errorMessage = null)
        }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion, errorMessage = null)
        }
    }

    fun onFechaChange(fecha: Date) {
        _uiState.update {
            it.copy(fecha = fecha, errorMessage = null)
        }
    }

    fun onPrioridadChange(prioridadId: Int) {
        _uiState.update {
            it.copy(prioridadId = prioridadId)
        }
    }
}

data class UiState(
    val ticketId: Int? = null,
    val fecha: Date? = null,
    val prioridadId: Int = 0,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val errorMessage: String? = null,
    val tickets: List<TicketEntity> = emptyList(),
    val prioridades: List<PrioridadEntity> = emptyList()
)

fun UiState.toEntity() = TicketEntity(
    ticketId = ticketId,
    fecha = fecha,
    prioridadId = prioridadId,
    cliente = cliente,
    asunto = asunto,
    descripcion = descripcion
)
