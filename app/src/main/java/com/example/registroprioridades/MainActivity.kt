package com.example.registroprioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import com.example.registroprioridades.data.database.PrioridadDb
import com.example.registroprioridades.data.entities.PrioridadEntity
import com.example.registroprioridades.ui.theme.RegistroPrioridadesTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var prioridadDb: PrioridadDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        prioridadDb = Room.databaseBuilder(
            applicationContext,
            PrioridadDb::class.java,
            "Prioridad.db"
        ).fallbackToDestructiveMigration()
            .build()

        setContent {
            RegistroPrioridadesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        PrioridadScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun PrioridadScreen(modifier: Modifier = Modifier) {
        var descripcion by remember { mutableStateOf("") }
        var diasCompromiso by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        val scope = rememberCoroutineScope()

        Scaffold { innerPadding ->
            Column(
                modifier = modifier
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
                    OutlinedButton(onClick = {
                        scope.launch {
                            when {
                                descripcion.isBlank() -> {
                                    errorMessage = "El campo de descripción no puede estar vacío"
                                }
                                diasCompromiso.isBlank() -> {
                                    errorMessage = "Todos los campos son requeridos"
                                }
                                (diasCompromiso.toIntOrNull() ?: 0) <= 0 -> {
                                    errorMessage = "El campo de días compromiso debe ser mayor a 0"
                                }
                                verificarDescripcion(descripcion) != null -> {
                                    errorMessage = "La prioridad ya existe"
                                }
                                else -> {
                                    guardarPrioridad(
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
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Guardar"
                        )
                        Text("Guardar")
                    }
                }

                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = "Lista de Prioridades",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                val lifecycleOwner = LocalLifecycleOwner.current
                val prioridadList by prioridadDb.prioridadDao().getAll()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        lifecycleOwner = lifecycleOwner,
                        minActiveState = Lifecycle.State.STARTED
                    )
                PrioridadListScreen(prioridadList)
            }
        }
    }

    private suspend fun guardarPrioridad(prioridad: PrioridadEntity) {
        prioridadDb.prioridadDao().save(prioridad)
    }

    private suspend fun verificarDescripcion(descripcion: String): PrioridadEntity? {
        return prioridadDb.prioridadDao().searchDescripcion(descripcion)
    }

    @Composable
    fun PrioridadListScreen(prioridadList: List<PrioridadEntity>) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
            ) {
                Text(
                    text = "Descripción",
                    modifier = Modifier.weight(2f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Días de Compromiso",
                    modifier = Modifier.weight(2f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(prioridadList) { prioridad ->
                    ListRow(prioridad)
                }
            }
        }
    }

    @Composable
    fun ListRow(prioridad: PrioridadEntity) {
        if (prioridad.prioridadId == 1)
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = prioridad.descripcion,
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.Center
            )
            Text(
                text = prioridad.diasCompromiso.toString(),
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.Center
            )
        }
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
    }

    @Preview(showBackground = true)
    @Composable
    fun PrioridadScreenPreview() {
        RegistroPrioridadesTheme {
            PrioridadScreen()
        }
    }
}