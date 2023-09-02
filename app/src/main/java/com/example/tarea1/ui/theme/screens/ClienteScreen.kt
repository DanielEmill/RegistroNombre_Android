package com.example.tarea1.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.tarea1.data.ClienteDb
import com.example.tarea1.data.models.cliente
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ClienteScreen(viewModel: ClienteViewModel = hiltViewModel()) {
    val clientes by viewModel.clientes.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.isMessageShownFlow.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(
                    message = "Cliente guardado",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Registro de Clientes") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Cliente: ", style =
                MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = viewModel.Nombre,
                    onValueChange = { viewModel.Nombre = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Nombre") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                val keyboardController =
                    LocalSoftwareKeyboardController.current
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)){
                    OutlinedButton(onClick = {
                        keyboardController?.hide()
                        viewModel.saveCliente()
                        viewModel.setMessageShown()
                    },modifier = Modifier.fillMaxWidth()
                    )
                    {
                        Icon(imageVector = Icons.Default.AddCircle,
                            contentDescription = "Guardar")
                        Text(text = "Guardar")
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Gray)
                            .padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Lista (${clientes.size} registros):",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(clientes) { cliente ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "ID: ${cliente.clienteId}", fontWeight = FontWeight.Bold)
                                Text(text = cliente.nombre)
                                OutlinedButton(
                                    onClick = {
                                        viewModel.deleteCliente(cliente)
                                    }
                                ) {
                                    Text(text = "Eliminar")
                                }
                            }
                        }
                    }

                }
            }
        }

    }
}


@HiltViewModel
class ClienteViewModel @Inject constructor(
    private val clienteDb: ClienteDb
) : ViewModel() {
    var Nombre by mutableStateOf("")

    private val _isMessageShown = MutableSharedFlow<Boolean>()
    val isMessageShownFlow = _isMessageShown.asSharedFlow()
    fun setMessageShown() {
        viewModelScope.launch {
            _isMessageShown.emit(true)
        }
    }

    val clientes: StateFlow<List<cliente>> = clienteDb.ClienteDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    fun saveCliente() {
        viewModelScope.launch {
            val client = cliente(
                nombre = Nombre
            )
            clienteDb.ClienteDao.save(client)
            limpiar()
        }
    }
    fun deleteCliente(cliente: cliente) {
        viewModelScope.launch {
            clienteDb.ClienteDao.delete(cliente)
            limpiar()
        }
    }

    private fun limpiar() {
        Nombre = ""
    }
}