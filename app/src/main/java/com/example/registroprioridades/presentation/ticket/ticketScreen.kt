package com.example.registroprioridades.presentation.ticket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TicketScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    TicketId: Int,
    goBack: () -> Unit,
    isTicketDelete: Boolean
) {
    LaunchedEffect(TicketId) {
        if (isTicketDelete) {
            viewModel.selectedTicket(TicketId)
        } else {
            viewModel.selectedTicket(TicketId)
        }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TicketBodyScreen(
        uiState = uiState,
        onFechaChange = viewModel::onFechaChange,
        onClienteIdChange = viewModel::onClienteIdChange,
        onSistemaIdChange = viewModel::onSistemaIdChange,
        onPrioridadIdChange = viewModel::onPrioridadChange,
        onSolicitadoPorChange = viewModel::onSolicitadoPorChange,
        onAsuntoChange = viewModel::onAsuntoChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        saveTicket = viewModel::save,
        deleteTicket = viewModel::delete,
        goBack = goBack,
        isTicketDelete = isTicketDelete
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketBodyScreen(
    uiState: UiState,
    onFechaChange: (Date) -> Unit,
    onClienteIdChange: (Int) -> Unit,
    onSistemaIdChange: (Int) -> Unit,
    onPrioridadIdChange: (Int) -> Unit,
    onSolicitadoPorChange: (String) -> Unit,
    onAsuntoChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    saveTicket: () -> Unit,
    deleteTicket: () -> Unit,
    goBack: () -> Unit,
    isTicketDelete: Boolean
) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = uiState.fecha.let { dateFormatter.format(it) } ?: ""

    var showDatePicker by remember { mutableStateOf(false) }
    var clienteExpanded by remember { mutableStateOf(false) }
    var sistemaExpanded by remember { mutableStateOf(false) }
    var prioridadExpanded by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Text(
                text = "Registro de Tickets",
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
                label = { Text("Fecha") },
                value = formattedDate,
                readOnly = true,
                onValueChange = {},
                trailingIcon = {
                    Icon(Icons.Default.Add, contentDescription = null,
                        modifier = Modifier.clickable { showDatePicker = true })
                }
            )
            uiState.errorFecha?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
            if (showDatePicker) {
                DatePickerDialog(
                    onDateSelected = { date ->
                        onFechaChange(date)
                        showDatePicker = false
                    },
                    onDismissRequest = { showDatePicker = false }
                )
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { clienteExpanded = true },
                    label = { Text("Cliente") },
                    value = if (uiState.clienteId != 0) uiState.clientes.firstOrNull { it.clienteId == uiState.clienteId }?.nombre
                        ?: "" else "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.clickable { clienteExpanded = true }
                        )
                    }
                )
                DropdownMenu(
                    expanded = clienteExpanded,
                    onDismissRequest = { clienteExpanded = false }
                ) {
                    uiState.clientes.forEach { cliente ->
                        DropdownMenuItem(
                            text = { Text(cliente.nombre) },
                            onClick = {
                                onClienteIdChange(cliente.clienteId ?: 0)
                                clienteExpanded = false
                            }
                        )
                    }
                }
            }
            uiState.errorClienteId?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { sistemaExpanded = true },
                    label = { Text("Sistema") },
                    value = if (uiState.sistemaId != 0) uiState.sistemas.firstOrNull { it.sistemasId == uiState.sistemaId }?.sistemaNombre
                        ?: "" else "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.clickable { sistemaExpanded = true }
                        )
                    }
                )
                DropdownMenu(
                    expanded = sistemaExpanded,
                    onDismissRequest = { sistemaExpanded = false }
                ) {
                    uiState.sistemas.forEach { sistema ->
                        DropdownMenuItem(
                            text = { Text(sistema.sistemaNombre) },
                            onClick = {
                                onSistemaIdChange(sistema.sistemasId ?: 0)
                                sistemaExpanded = false
                            }
                        )
                    }
                }
            }
            uiState.errorSistemaId?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { prioridadExpanded = true },
                    label = { Text("Prioridad") },
                    value = uiState.prioridades.firstOrNull { it.prioridadId == uiState.prioridadId }?.descripción
                        ?: "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.clickable { prioridadExpanded = true }
                        )
                    }
                )
                DropdownMenu(
                    expanded = prioridadExpanded,
                    onDismissRequest = { prioridadExpanded = false }
                ) {
                    uiState.prioridades.forEach { prioridad ->
                        DropdownMenuItem(
                            text = { Text(prioridad.descripción) },
                            onClick = {
                                onPrioridadIdChange(prioridad.prioridadId ?: 0)
                                prioridadExpanded = false
                            }
                        )
                    }
                }
            }
            uiState.errorPrioridadId?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Solicitado por") },
                value = uiState.solicitadoPor,
                onValueChange = onSolicitadoPorChange
            )
            uiState.errorSolicitadoPor?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Asunto") },
                value = uiState.asunto,
                onValueChange = onAsuntoChange
            )
            uiState.errorAsunto?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Descripción") },
                value = uiState.descripcion,
                onValueChange = onDescripcionChange
            )
            uiState.errorDescripcion?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
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
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Volver")
                }
                if (!isTicketDelete) {
                    OutlinedButton(onClick = saveTicket) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Guardar"
                        )
                        Text("Guardar")
                    }
                } else {
                    OutlinedButton(onClick = deleteTicket) {
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

@Composable
fun DatePickerDialog(
    onDateSelected: (Date) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }
    datePickerDialog.setOnDismissListener { onDismissRequest() }
    datePickerDialog.show()
}