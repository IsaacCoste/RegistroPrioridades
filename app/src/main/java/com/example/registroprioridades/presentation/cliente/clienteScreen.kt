package com.example.registroprioridades.presentation.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ClienteScreen(
    viewModel: ClienteViewModel = hiltViewModel(),
    clienteId: Int,
    goBack: () -> Unit,
    isClienteDelete: Boolean
){
    LaunchedEffect(clienteId) {
        if (isClienteDelete) {
            viewModel.selectedCliente(clienteId)
        } else {
            viewModel.selectedCliente(clienteId)
        }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ClienteBodyScreen(
        uiState = uiState,
        onClienteIdChange = viewModel::onClienteIdChange,
        onNombreChange = viewModel::onNombreChange,
        onTelefonoChange = viewModel::onTelefonoChange,
        onCelularChange = viewModel::onCelularChange,
        onRncChange = viewModel::onRncChange,
        onEmailChange = viewModel::onEmailChange,
        onDireccionChange = viewModel::onDireccionChange,
        saveCliente = viewModel::save,
        deleteCliente = viewModel::delete,
        goBack = goBack,
        isClienteDelete = isClienteDelete
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteBodyScreen(
    uiState: UiState,
    onClienteIdChange: (Int) -> Unit,
    onNombreChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onCelularChange: (String) -> Unit,
    onRncChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onDireccionChange: (String) -> Unit,
    saveCliente: () -> Unit,
    deleteCliente: () -> Unit,
    goBack: () -> Unit,
    isClienteDelete: Boolean
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Text(
                text = "Registro de Clientes",
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
                label = { Text("Nombre") },
                value = uiState.nombre,
                onValueChange = onNombreChange
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Teléfono") },
                value = uiState.telefono,
                onValueChange = onTelefonoChange,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Celular") },
                value = uiState.celular,
                onValueChange = onCelularChange,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("RNC") },
                value = uiState.rnc,
                onValueChange = onRncChange
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Email") },
                value = uiState.email,
                onValueChange = onEmailChange
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Dirección") },
                value = uiState.direccion,
                onValueChange = onDireccionChange
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = goBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text("Volver")
                }
                if (!isClienteDelete) {
                    OutlinedButton(onClick = saveCliente) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Guardar"
                        )
                        Text("Guardar")
                    }
                } else {
                    OutlinedButton(onClick = {
                        deleteCliente()
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
