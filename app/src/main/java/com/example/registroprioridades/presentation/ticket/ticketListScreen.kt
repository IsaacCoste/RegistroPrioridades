package com.example.registroprioridades.presentation.ticket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.registroprioridades.data.local.entities.PrioridadEntity
import com.example.registroprioridades.data.local.entities.TicketEntity
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TicketListScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    createTicket: () -> Unit,
    onEditTicket: (Int) -> Unit,
    onDeleteTicket: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TicketListBodyScreen(
        uiState = uiState,
        createTicket = createTicket,
        onEditTicket = onEditTicket,
        onDeleteTicket = onDeleteTicket
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListBodyScreen(
    uiState: UiState,
    createTicket: () -> Unit,
    onEditTicket: (Int) -> Unit,
    onDeleteTicket: (Int) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de Tickets") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createTicket,
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Ticket"
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
                items(uiState.tickets) { ticket ->
                    TicketRow(
                        ticket = ticket,
                        prioridades = uiState.prioridades,
                        onEditTicket = onEditTicket,
                        onDeleteTicket = onDeleteTicket
                    )
                }
            }
        }
    }
}

@Composable
fun TicketRow(
    ticket: TicketEntity,
    prioridades: List<PrioridadEntity>,
    onEditTicket: (Int) -> Unit,
    onDeleteTicket: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val descripcionPrioridad = prioridades.find { prioridad ->
        prioridad.prioridadId == ticket.prioridadId
    }?.descripcion ?: "Sin Prioridad"

    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable { expanded = true },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "TicketID: ${ticket.ticketId}",
                textAlign = TextAlign.Center
            )
            Text(
                text = "Fecha: ${ticket.fecha?.let { dateFormat.format(it) }}",
                textAlign = TextAlign.Center
            )
            Text(
                text = "Cliente: ${ticket.cliente}",
                textAlign = TextAlign.Center
            )
            Text(
                text = "Asunto: ${ticket.asunto}",
                textAlign = TextAlign.Center
            )
            Text(
                text = "Descripción: ${ticket.descripcion}",
                textAlign = TextAlign.Center
            )
            Text(
                text = "Prioridad: $descripcionPrioridad",
                textAlign = TextAlign.Center
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Editar") },
                onClick = {
                    expanded = false
                    onEditTicket(ticket.ticketId!!)
                }
            )
            DropdownMenuItem(
                text = { Text("Eliminar") },
                onClick = {
                    expanded = false
                    onDeleteTicket(ticket.ticketId!!)
                }
            )
        }
    }
    Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
}
