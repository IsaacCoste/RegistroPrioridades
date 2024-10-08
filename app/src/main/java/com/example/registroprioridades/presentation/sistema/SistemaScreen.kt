package com.example.registroprioridades.presentation.sistema

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SistemaScreen(
    viewModel: SistemasViewModel = hiltViewModel(),
    sistemaId: Int,
    goBack: () -> Unit,
    isSistemaDelete: Boolean
) {
    LaunchedEffect(sistemaId) {
        viewModel.selectedSistema(sistemaId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SistemaBodyScreen(
        uiState = uiState,
        onSistemaNombreChange = viewModel::onSistemaNombreChange,
        saveSistema = viewModel::save,
        deleteSistema = viewModel::delete,
        goBack = goBack,
        isSistemaDelete = isSistemaDelete
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SistemaBodyScreen(
    uiState: UiState,
    onSistemaNombreChange: (String) -> Unit,
    saveSistema: () -> Unit,
    deleteSistema: () -> Unit,
    goBack: () -> Unit,
    isSistemaDelete: Boolean
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Text(
                text = "Registro de Sistemas",
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
                label = { Text("Nombre del Sistema") },
                value = uiState.sistemaNombre,
                onValueChange = onSistemaNombreChange
            )
            Spacer(modifier = Modifier.padding(8.dp))
            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = goBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Volver")
                }
                if (!isSistemaDelete) {
                    OutlinedButton(onClick = {
                        saveSistema()
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Guardar"
                        )
                        Text("Guardar")
                    }
                } else {
                    OutlinedButton(onClick = {
                        deleteSistema()
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            tint = Color.Red,
                            contentDescription = "Eliminar"
                        )
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}