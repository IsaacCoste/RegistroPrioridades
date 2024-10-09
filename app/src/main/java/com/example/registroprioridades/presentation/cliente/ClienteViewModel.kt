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
            if (validated()) {
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
                    errorNombre = null,
                    errorTelefono = null,
                    errorCelular = null,
                    errorRnc = null,
                    errorEmail = null,
                    errorDireccion = null
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
            val response = clienteRepository.deleteCliente(_uiState.value.clienteId!!)
            if (response.isSuccessful) {
                nuevo()
            }
            getClientes()
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

    private fun validated(): Boolean {
        var isValid = true

        if (_uiState.value.nombre.isBlank()) {
            _uiState.update {
                it.copy(errorNombre = "El nombre no puede estar vacío")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorNombre = null)
            }
        }

        if (_uiState.value.telefono.length > 10 || _uiState.value.telefono.isBlank()) {
            _uiState.update {
                it.copy(errorTelefono = "El teléfono no puede tener más de 10 dígitos")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorTelefono = null)
            }
        }

        if (_uiState.value.celular.length > 10 || _uiState.value.celular.isBlank()) {
            _uiState.update {
                it.copy(errorCelular = "El celular no puede tener más de 10 dígitos")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorCelular = null)
            }
        }

        if (_uiState.value.rnc.isBlank()) {
            _uiState.update {
                it.copy(errorRnc = "El RNC no puede estar vacío")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorRnc = null)
            }
        }

        if (_uiState.value.email.isBlank()) {
            _uiState.update {
                it.copy(errorEmail = "El email no puede estar vacío")
            }
            isValid = false
        } else if (!isValidEmail(_uiState.value.email)) {
            _uiState.update {
                it.copy(errorEmail = "El formato del email no es válido")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorEmail = null)
            }
        }

        if (_uiState.value.direccion.isBlank()) {
            _uiState.update {
                it.copy(errorDireccion = "La dirección no puede estar vacía")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorDireccion = null)
            }
        }

        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
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
    val errorNombre: String? = null,
    val errorTelefono: String? = null,
    val errorCelular: String? = null,
    val errorRnc: String? = null,
    val errorEmail: String? = null,
    val errorDireccion: String? = null,
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
