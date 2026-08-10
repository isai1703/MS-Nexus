package com.multiservicios.msnexus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.multiservicios.msnexus.data.local.AppDatabase
import com.multiservicios.msnexus.data.local.OrdenEntity
import com.multiservicios.msnexus.data.repository.FolioRepository
import com.multiservicios.msnexus.data.repository.OrdenRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrdenViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)

    private val repository = OrdenRepository(
        database.ordenDao()
    )

    private val folioRepository = FolioRepository(database)

    val ordenes: StateFlow<List<OrdenEntity>> =
        repository.obtenerTodas()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun crearOrden(
        numeroCliente: String,
        nombreCliente: String,
        empresa: String,
        telefono: String,
        correo: String,
        direccion: String,
        tipoTrabajo: String,
        descripcionTrabajo: String,
        fechaProgramada: String,
        subtotal: Double,
        descuento: Double,
        ivaPorcentaje: Double,
        onResultado: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (nombreCliente.isBlank()) {
                    onResultado(false)
                    return@launch
                }

                if (tipoTrabajo.isBlank()) {
                    onResultado(false)
                    return@launch
                }

                val folio = folioRepository.siguienteOrden()

                val subtotalReal = subtotal.coerceAtLeast(0.0)
                val descuentoReal = descuento.coerceAtLeast(0.0)
                val base = (subtotalReal - descuentoReal).coerceAtLeast(0.0)

                val ivaReal = ivaPorcentaje.coerceAtLeast(0.0)
                val ivaImporte = base * (ivaReal / 100.0)
                val total = base + ivaImporte

                val orden = OrdenEntity(
                    folio = folio,
                    fecha = java.text.SimpleDateFormat(
                        "dd/MM/yyyy",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date()),
                    numeroCliente = numeroCliente.trim(),
                    nombreCliente = nombreCliente.trim(),
                    empresa = empresa.trim(),
                    telefono = telefono.trim(),
                    correo = correo.trim(),
                    direccion = direccion.trim(),
                    tipoTrabajo = tipoTrabajo.trim(),
                    descripcionTrabajo = descripcionTrabajo.trim(),
                    fechaProgramada = fechaProgramada.trim(),
                    subtotal = subtotalReal,
                    descuento = descuentoReal,
                    ivaPorcentaje = ivaReal,
                    ivaImporte = ivaImporte,
                    total = total
                )

                repository.insertar(orden)

                onResultado(true)

            } catch (_: Exception) {
                onResultado(false)
            }
        }
    }

    fun eliminarOrden(orden: OrdenEntity) {
        viewModelScope.launch {
            try {
                repository.eliminar(orden)
            } catch (_: Exception) {
            }
        }
    }
}
