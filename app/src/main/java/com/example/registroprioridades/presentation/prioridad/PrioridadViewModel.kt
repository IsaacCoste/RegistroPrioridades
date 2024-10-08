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
            if (_uiState.value.descripcion.isBlank() || _uiState.value.diasCompromiso <= 0) {
                _uiState.update {
                    it.copy(errorMessage = "La prioridad no puede estar vacia")
                }
            }
            else{
                prioridadRepository.savePrioridad(_uiState.value.toEntity())
                nuevo()
            }
        }
    }
    private fun nuevo() {
        _uiState.update {
            it.copy(
                prioridadId = null,
                descripcion = "",
                diasCompromiso = 0,
                errorMessage = null
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
                        descripcion = prioridad.descripción ?: "",
                        diasCompromiso = prioridad.diasCompromiso ?: 0
                    )
                }
            }
        }
    }
    fun delete() {
        viewModelScope.launch {
            prioridadRepository.deletePrioridad(_uiState.value.prioridadId!!)
            nuevo()
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
    val errorMessage: String? = null,
    val prioridades: List<PrioridadDto> = emptyList()
)

fun UiState.toEntity() = PrioridadDto(
    prioridadId = prioridadId,
    descripción = descripcion,
    diasCompromiso = diasCompromiso
)