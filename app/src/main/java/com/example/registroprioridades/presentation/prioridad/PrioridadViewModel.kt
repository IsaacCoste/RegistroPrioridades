package com.example.registroprioridades.presentation.prioridad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registroprioridades.data.local.entities.PrioridadEntity
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
            if (_uiState.value.descripcion.isNullOrBlank() || _uiState.value.diasCompromiso <= 0) {
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
    fun nuevo() {
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
                        prioridadId = prioridad?.prioridadId,
                        descripcion = prioridad?.descripcion ?: "",
                        diasCompromiso = prioridad?.diasCompromiso ?: 0
                    )
                }
            }
        }
    }
    fun delete() {
        viewModelScope.launch {
            prioridadRepository.deletePrioridad(_uiState.value.toEntity())
        }
    }
    private fun getPrioridades() {
        viewModelScope.launch {
            prioridadRepository.getPrioridades().collect { prioridad ->
                _uiState.update {
                    it.copy(prioridades = prioridad)
                }
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
    val prioridades: List<PrioridadEntity> = emptyList()
)

fun UiState.toEntity() = PrioridadEntity(
    prioridadId = prioridadId,
    descripcion = descripcion ?: "",
    diasCompromiso = diasCompromiso
)