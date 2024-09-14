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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.registroprioridades.data.database.PrioridadDb
import com.example.registroprioridades.data.entities.PrioridadEntity
import kotlinx.coroutines.launch

@Composable
fun PrioridadScreen(
    prioridadDb: PrioridadDb,
    goBack: () -> Unit
) {
    var descripcion by remember { mutableStateOf("") }
    var diasCompromiso by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

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
                value = descripcion,
                onValueChange = { descripcion = it }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Días Compromiso") },
                value = diasCompromiso,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                        diasCompromiso = newValue
                        errorMessage = null
                    } else {
                        errorMessage = "Días de Compromiso debe ser un número"
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            errorMessage?.let {
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
                    if (descripcion.isNotBlank() && diasCompromiso.isNotBlank()) {
                        scope.launch {
                            val existingPrioridad = verificarDescripcion(prioridadDb, descripcion)
                            when {
                                existingPrioridad != null -> errorMessage = "La prioridad ya existe"
                                descripcion.isBlank() -> errorMessage = "El campo de descripción no puede estar vacío"
                                diasCompromiso.toInt() <= 0 -> errorMessage = "El campo de días de compromiso debe ser mayor a 0"
                                else -> {
                                    guardarPrioridad(
                                        prioridadDb,
                                        PrioridadEntity(
                                            descripcion = descripcion,
                                            diasCompromiso = diasCompromiso.toInt()
                                        )
                                    )
                                    descripcion = ""
                                    diasCompromiso = ""
                                    errorMessage = null
                                }
                            }
                        }
                    } else {
                        errorMessage = "Todos los campos son requeridos"
                    }
                })
                {
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

private suspend fun guardarPrioridad(prioridadDb: PrioridadDb, prioridad: PrioridadEntity) {
    prioridadDb.prioridadDao().save(prioridad)
}

private suspend fun verificarDescripcion(prioridadDb: PrioridadDb, descripcion: String): PrioridadEntity? {
    return prioridadDb.prioridadDao().searchDescripcion(descripcion)
}