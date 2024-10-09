package com.example.registroprioridades.presentation.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registroprioridades.data.remote.dto.ClienteDto
import com.example.registroprioridades.data.remote.dto.PrioridadDto
import com.example.registroprioridades.data.remote.dto.SistemaDto
import com.example.registroprioridades.data.remote.dto.TicketDto
import com.example.registroprioridades.data.repository.ClienteRepository
import com.example.registroprioridades.data.repository.PrioridadRepository
import com.example.registroprioridades.data.repository.SistemaRepository
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
    private val prioridadRepository: PrioridadRepository,
    private val sistemaRepository: SistemaRepository,
    private val clienteRepository: ClienteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTickets()
        getPrioridades()
        getSistemas()
        getClientes()
    }

    fun save() {
        viewModelScope.launch {
            if (validated()) {
                ticketRepository.saveTicket(_uiState.value.toEntity())
                nuevo()
            }
        }
    }

    private fun validated(): Boolean {
        var isValid = true
        if (_uiState.value.fecha == null) {
            _uiState.update {
                it.copy(errorFecha = "La fecha no puede estar vacía")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorFecha = null)
            }
        }
        if (_uiState.value.clienteId <= 0) {
            _uiState.update {
                it.copy(errorClienteId = "El cliente no puede estar vacío")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorClienteId = null)
            }
        }
        if (_uiState.value.sistemaId <= 0) {
            _uiState.update {
                it.copy(errorSistemaId = "El sistema no puede estar vacío")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorSistemaId = null)
            }
        }
        if (_uiState.value.prioridadId <= 0) {
            _uiState.update {
                it.copy(errorPrioridadId = "La prioridad no puede estar vacía")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorPrioridadId = null)
            }
        }
        if (_uiState.value.solicitadoPor.isBlank()) {
            _uiState.update {
                it.copy(errorSolicitadoPor = "El solicitado por no puede estar vacío")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorSolicitadoPor = null)
            }
        }
        if (_uiState.value.asunto.isBlank()) {
            _uiState.update {
                it.copy(errorAsunto = "El asunto no puede estar vacío")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorAsunto = null)
            }
        }
        if (_uiState.value.descripcion.isBlank()) {
            _uiState.update {
                it.copy(errorDescripcion = "La descripción no puede estar vacía")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorDescripcion = null)
            }
        }
        return isValid
    }

    fun nuevo() {
        _uiState.update {
            it.copy(
                ticketId = null,
                fecha = Date(),
                clienteId = 0,
                sistemaId = 0,
                prioridadId = 0,
                solicitadoPor = "",
                asunto = "",
                descripcion = "",
                errorFecha = null
            )
        }
    }

    fun selectedTicket(ticketId: Int) {
        viewModelScope.launch {
            if (ticketId > 0) {
                val ticket = ticketRepository.getTicket(ticketId)
                _uiState.update {
                    it.copy(
                        ticketId = ticket.ticketId,
                        fecha = ticket.fecha,
                        clienteId = ticket.clienteId,
                        sistemaId = ticket.sistemaId,
                        prioridadId = ticket.prioridadId,
                        solicitadoPor = ticket.solicitadoPor,
                        asunto = ticket.asunto,
                        descripcion = ticket.descripcion
                    )
                }
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            val response = ticketRepository.deleteTicket(_uiState.value.ticketId!!)
            if (response.isSuccessful) {
                nuevo()
            }
        }
    }

    private fun getTickets() {
        viewModelScope.launch {
            val tickets = ticketRepository.getTickets()
            _uiState.update {
                it.copy(tickets = tickets)
            }
        }
    }

    private fun getPrioridades() {
        viewModelScope.launch {
            val prioridades = prioridadRepository.getPrioridades()
            _uiState.update {
                it.copy(prioridades = prioridades)
            }
        }
    }

    private fun getSistemas() {
        viewModelScope.launch {
            val sistemas = sistemaRepository.getSistemas()
            _uiState.update {
                it.copy(sistemas = sistemas)
            }
        }
    }

    private fun getClientes() {
        viewModelScope.launch {
            val clientes = clienteRepository.getClientes()
            _uiState.update {
                it.copy(clientes = clientes)
            }
        }
    }

    fun onTicketIdChange(ticketId: Int) {
        _uiState.update {
            it.copy(ticketId = ticketId)
        }
    }

    fun onClienteIdChange(clienteId: Int) {
        _uiState.update {
            it.copy(clienteId = clienteId)
        }
    }

    fun onAsuntoChange(asunto: String) {
        _uiState.update {
            it.copy(asunto = asunto)
        }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    fun onFechaChange(fecha: Date) {
        _uiState.update {
            it.copy(fecha = fecha)
        }
    }

    fun onPrioridadChange(prioridadId: Int) {
        _uiState.update {
            it.copy(prioridadId = prioridadId)
        }
    }

    fun onSolicitadoPorChange(solicitadoPor: String) {
        _uiState.update {
            it.copy(solicitadoPor = solicitadoPor)
        }
    }

    fun onSistemaIdChange(sistemaId: Int) {
        _uiState.update {
            it.copy(sistemaId = sistemaId)
        }
    }
}

data class UiState(
    val ticketId: Int? = null,
    val fecha: Date = Date(),
    val clienteId: Int = 0,
    val sistemaId: Int = 0,
    val prioridadId: Int = 0,
    val solicitadoPor: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val errorFecha: String? = null,
    val errorClienteId: String? = null,
    val errorSistemaId: String? = null,
    val errorPrioridadId: String? = null,
    val errorSolicitadoPor: String? = null,
    val errorAsunto: String? = null,
    val errorDescripcion: String? = null,
    val tickets: List<TicketDto> = emptyList(),
    val prioridades: List<PrioridadDto> = emptyList(),
    val sistemas: List<SistemaDto> = emptyList(),
    val clientes: List<ClienteDto> = emptyList()
)

fun UiState.toEntity() = TicketDto(
    ticketId = ticketId,
    fecha = fecha,
    clienteId = clienteId,
    sistemaId = sistemaId,
    prioridadId = prioridadId,
    solicitadoPor = solicitadoPor,
    asunto = asunto,
    descripcion = descripcion
)
