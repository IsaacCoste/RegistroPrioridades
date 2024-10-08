package com.example.registroprioridades.presentation.cliente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registroprioridades.data.remote.dto.ClienteDto
import com.example.registroprioridades.data.repository.ClienteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClienteViewModel @Inject constructor(
    private val clienteRepository: ClienteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getClientes()
    }

    fun save() {
        viewModelScope.launch {
            if (_uiState.value.nombre.isBlank()) {
                _uiState.update {
                    it.copy(errorMessage = "El nombre no puede estar vacio")
                }
            }
            else {
                clienteRepository.saveCliente(_uiState.value.toEntity())
                nuevo()
            }
        }
    }
    private fun nuevo() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    clienteId = null,
                    nombre = "",
                    telefono = "",
                    celular = "",
                    rnc = "",
                    email = "",
                    direccion = "",
                    errorMessage = null
                )
            }
        }
    }
    fun selectedCliente(clienteId: Int) {
        viewModelScope.launch {
            if (clienteId > 0) {
                val cliente = clienteRepository.getCliente(clienteId)
                _uiState.update {
                    it.copy(
                        clienteId = cliente.clienteId,
                        nombre = cliente.nombre,
                        telefono = cliente.telefono,
                        celular = cliente.celular,
                        rnc = cliente.rnc,
                        email = cliente.email,
                        direccion = cliente.direccion
                    )
                }
            }
        }
    }
    fun delete() {
        viewModelScope.launch {
            clienteRepository.deleteCliente(_uiState.value.clienteId!!)
            nuevo()
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

    fun onNombreChange(nombre: String) {
        _uiState.update {
            it.copy(nombre = nombre)
        }
    }
    fun onTelefonoChange(telefono: String) {
        _uiState.update {
            it.copy(telefono = telefono)
        }
    }
    fun onCelularChange(celular: String) {
        _uiState.update {
            it.copy(celular = celular)
        }
    }
    fun onRncChange(rnc: String) {
        _uiState.update {
            it.copy(rnc = rnc)
        }
    }
    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }
    fun onDireccionChange(direccion: String) {
        _uiState.update {
            it.copy(direccion = direccion)
        }
    }
    fun onClienteIdChange(clienteId: Int) {
        _uiState.update {
            it.copy(clienteId = clienteId)
        }
    }
}

data class UiState(
    val clienteId: Int? = null,
    val nombre: String = "",
    val telefono: String = "",
    val celular: String = "",
    val rnc: String = "",
    val email: String = "",
    val direccion: String = "",
    val errorMessage: String? = null,
    val clientes: List<ClienteDto> = emptyList()
)

fun UiState.toEntity() = ClienteDto(
    clienteId = clienteId,
    nombre = nombre,
    telefono = telefono,
    celular = celular,
    rnc = rnc,
    email = email,
    direccion = direccion
)