package com.example.registroprioridades.presentation.sistema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registroprioridades.data.remote.dto.SistemaDto
import com.example.registroprioridades.data.repository.SistemaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SistemasViewModel @Inject constructor(
    private val sistemasRepository: SistemaRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getSistemas()
    }
    fun save() {
        viewModelScope.launch {
            if (_uiState.value.sistemaNombre.isBlank()) {
                _uiState.update {
                    it.copy(errorMessage = "El sistema no puede estar vacio")
                }
            } else {
                sistemasRepository.saveSistema(_uiState.value.toEntity())
                nuevo()
            }
        }
    }
    private fun nuevo() {
        _uiState.update {
            it.copy(
                sistemaId = 0,
                sistemaNombre = "",
                errorMessage = null
            )
        }
    }
    fun selectedSistema(sistemaId: Int) {
        viewModelScope.launch {
            if (sistemaId > 0) {
                val sistema = sistemasRepository.getSistema(sistemaId)
                _uiState.update {
                    it.copy(
                        sistemaId = sistema.sistemasId,
                        sistemaNombre = sistema.sistemaNombre
                    )
                }
            }
        }
    }
    fun delete() {
        viewModelScope.launch {
            val response = sistemasRepository.deleteSistema(_uiState.value.sistemaId!!)
            if (response.isSuccessful) {
                nuevo()
            }
            getSistemas()
        }
    }
    private fun getSistemas() {
        viewModelScope.launch {
            val sistemas = sistemasRepository.getSistemas()
            _uiState.update {
                it.copy(sistemas = sistemas)
            }
        }
    }
    fun onSistemaNombreChange(sistemaNombre: String) {
        _uiState.update {
            it.copy(sistemaNombre = sistemaNombre)
        }
    }
    fun onSistemaIdChange(sistemaId: Int) {
        _uiState.update {
            it.copy(sistemaId = sistemaId)
        }
    }
}

data class UiState(
    val sistemaId: Int? = null,
    val sistemaNombre: String = "",
    val errorMessage: String? = null,
    val sistemas: List<SistemaDto> = emptyList()
)

fun UiState.toEntity() = SistemaDto(
    sistemasId = sistemaId,
    sistemaNombre = sistemaNombre
)