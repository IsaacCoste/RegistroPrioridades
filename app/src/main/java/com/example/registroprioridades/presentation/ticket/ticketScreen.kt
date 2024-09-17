package com.example.registroprioridades.presentation.ticket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
    goBack: () -> Unit
) {
    LaunchedEffect(TicketId) {
        viewModel.selectedTicket(TicketId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TicketBodyScreen(
        uiState = uiState,
        onFechaChange = viewModel::onFechaChange,
        onClienteChange = viewModel::onClienteChange,
        onAsuntoChange = viewModel::onAsuntoChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onPrioridadChange = viewModel::onPrioridadChange,
        saveTicket = viewModel::save,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketBodyScreen(
    uiState: UiState,
    onFechaChange: (Date) -> Unit,
    onClienteChange: (String) -> Unit,
    onAsuntoChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrioridadChange: (Int) -> Unit,
    saveTicket: () -> Unit,
    goBack: () -> Unit
) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = uiState.fecha?.let { dateFormatter.format(it) } ?: ""

    var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

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
            if (showDatePicker) {
                DatePickerDialog(
                    onDateSelected = { date ->
                        onFechaChange(date)
                        showDatePicker = false
                    },
                    onDismissRequest = { showDatePicker = false }
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Cliente") },
                value = uiState.cliente,
                onValueChange = onClienteChange
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Asunto") },
                value = uiState.asunto,
                onValueChange = onAsuntoChange
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Descripción") },
                value = uiState.descripcion,
                onValueChange = onDescripcionChange
            )

            Box(modifier = Modifier.fillMaxWidth()) {

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { expanded = true },
                        label = { Text("Prioridad") },
                        value = uiState.prioridades.firstOrNull { it.prioridadId == uiState.prioridadId }?.descripcion ?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.clickable { expanded = true }
                            )
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        uiState.prioridades.forEach { prioridad ->
                            DropdownMenuItem(
                                text = { Text(prioridad.descripcion) },
                                onClick = {
                                    onPrioridadChange(prioridad.prioridadId ?: 0)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

            }

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
                OutlinedButton(onClick = saveTicket) {
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
