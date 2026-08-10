package com.multiservicios.msnexus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.multiservicios.msnexus.data.local.AppDatabase
import com.multiservicios.msnexus.data.local.ClienteEntity
import com.multiservicios.msnexus.data.repository.ClienteRepository
import com.multiservicios.msnexus.data.repository.FolioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClienteViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)

    private val repository = ClienteRepository(
        database.clienteDao(),
        FolioRepository(database)
    )

    val clientes: StateFlow<List<ClienteEntity>> =
        repository.obtenerTodos()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun crearCliente(
        nombre: String,
        empresa: String,
        telefono: String,
        correo: String,
        direccion: String,
        onResultado: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (nombre.isBlank()) {
                    onResultado(false)
                    return@launch
                }

                repository.crear(
                    nombre = nombre.trim(),
                    empresa = empresa.trim(),
                    telefono = telefono.trim(),
                    correo = correo.trim(),
                    direccion = direccion.trim()
                )

                onResultado(true)

            } catch (e: Exception) {
                onResultado(false)
            }
        }
    }

    fun eliminarCliente(cliente: ClienteEntity) {
        viewModelScope.launch {
            repository.eliminar(cliente)
        }
    }
}
