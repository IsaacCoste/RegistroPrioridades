package com.example.registroprioridades.presentation.prioridad

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.registroprioridades.data.remote.dto.PrioridadDto

@Composable
fun PrioridadListScreen(
    viewModel: PrioridadViewModel = hiltViewModel(),
    createPrioridad: () -> Unit,
    onEditPrioridad: (Int) -> Unit,
    onDeletePrioridad: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PrioridadListBodyScreen(
        uiState = uiState,
        createPrioridad = createPrioridad,
        onEditPrioridad = onEditPrioridad,
        onDeletePrioridad = onDeletePrioridad
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadListBodyScreen(
    uiState: UiState,
    createPrioridad: () -> Unit,
    onEditPrioridad: (Int) -> Unit,
    onDeletePrioridad: (Int) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de Prioridades", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createPrioridad,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Prioridad"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(uiState.prioridades) { prioridad ->
                    ListRow(
                        prioridad = prioridad,
                        onEditPrioridad = onEditPrioridad,
                        onDeletePrioridad = onDeletePrioridad
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ListRow(
    prioridad: PrioridadDto,
    onEditPrioridad: (Int) -> Unit,
    onDeletePrioridad: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ID: ${prioridad.prioridadId}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            Text(
                text = "Descripción: ${prioridad.descripción}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
            Text(
                text = "Días de Compromiso: ${prioridad.diasCompromiso}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Editar", style = MaterialTheme.typography.bodyMedium) },
                onClick = {
                    expanded = false
                    onEditPrioridad(prioridad.prioridadId!!)
                }
            )
            DropdownMenuItem(
                text = { Text("Eliminar", style = MaterialTheme.typography.bodyMedium) },
                onClick = {
                    expanded = false
                    onDeletePrioridad(prioridad.prioridadId!!)
                }
            )
        }
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
}