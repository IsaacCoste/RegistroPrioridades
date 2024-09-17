package com.example.registroprioridades.presentation.prioridad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PrioridadScreen(
    viewModel: PrioridadViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PrioridadBodyScreen(
        uiState = uiState,
        onDescripcionChange = viewModel::onDescripcionChange,
        onDiasCompromisoChange = viewModel::onDiasCompromisoChange,
        savePrioridad = viewModel::save,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadBodyScreen(
    uiState: UiState,
    onDescripcionChange: (String) -> Unit,
    onDiasCompromisoChange: (Int) -> Unit,
    savePrioridad: () -> Unit,
    goBack: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Text(
                text = "Registro de Prioridades",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Descripción") },
                value = uiState.descripcion,
                onValueChange = onDescripcionChange
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Días Compromiso") },
                value = uiState.diasCompromiso.toString(),
                onValueChange = { newValue ->
                    val diasCompromiso = newValue.toIntOrNull() ?: 0
                    onDiasCompromisoChange(diasCompromiso)
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            uiState.errorMessage?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = it,
                        color = Color.Red
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = goBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Volver")
                }
                OutlinedButton(onClick = {
                    savePrioridad()
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Guardar"
                    )
                    Text("Guardar")
                }
            }
        }
    }
}
