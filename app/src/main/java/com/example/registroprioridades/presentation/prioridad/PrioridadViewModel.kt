package com.example.registroprioridades.presentation.prioridad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registroprioridades.data.remote.dto.PrioridadDto
import com.example.registroprioridades.data.repository.PrioridadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrioridadViewModel @Inject constructor(
    private val prioridadRepository: PrioridadRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPrioridades()
    }
    fun save() {
        viewModelScope.launch {
            if (validate()) {
                prioridadRepository.savePrioridad(_uiState.value.toEntity())
                nuevo()
            }
        }
    }
    private fun validate(): Boolean {
        var isValid = true

        if (_uiState.value.descripcion.isBlank()) {
            _uiState.update {
                it.copy(errorDescripcion = "La prioridad no puede estar vacía")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorDescripcion = null)
            }
        }
        if (_uiState.value.diasCompromiso <= 0) {
            _uiState.update {
                it.copy(errorDiasCompromiso = "Los días de compromiso deben ser mayores que cero")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorDiasCompromiso = null)
            }
        }
        return isValid
    }
    private fun nuevo() {
        _uiState.update {
            it.copy(
                prioridadId = null,
                descripcion = "",
                diasCompromiso = 0,
                errorDescripcion = null,
                errorDiasCompromiso = null
            )
        }
    }
    fun selectedPrioridad(prioridadId: Int) {
        viewModelScope.launch {
            if (prioridadId > 0) {
                val prioridad = prioridadRepository.getPrioridad(prioridadId)
                _uiState.update {
                    it.copy(
                        prioridadId = prioridad.prioridadId,
                        descripcion = prioridad.descripción,
                        diasCompromiso = prioridad.diasCompromiso
                    )
                }
            }
        }
    }
    fun delete() {
        viewModelScope.launch {
            val response = prioridadRepository.deletePrioridad(_uiState.value.prioridadId!!)
            if (response.isSuccessful) {
                nuevo()
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
    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }
    fun onDiasCompromisoChange(diasCompromiso: Int) {
        _uiState.update {
            it.copy(diasCompromiso = diasCompromiso)
        }
    }
}

data class UiState(
    val prioridadId: Int? = null,
    val descripcion: String = "",
    val diasCompromiso: Int = 0,
    val errorDescripcion: String? = null,
    val errorDiasCompromiso: String? = null,
    val prioridades: List<PrioridadDto> = emptyList()
)

fun UiState.toEntity() = PrioridadDto(
    prioridadId = prioridadId,
    descripción = descripcion,
    diasCompromiso = diasCompromiso
)
