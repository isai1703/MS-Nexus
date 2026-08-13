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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

        tipoElemento: String = "",
        alto: Double = 0.0,
        ancho: Double = 0.0,
        largo: Double = 0.0,
        cantidad: Int = 0,
        material: String = "",
        perfilCalibre: String = "",
        acabado: String = "",
        color: String = "",
        observacionesTecnicas: String = "",
        disenoUri: String = "",

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

                val folio =
                    folioRepository.siguienteOrden()

                val subtotalReal =
                    subtotal.coerceAtLeast(0.0)

                val descuentoReal =
                    descuento.coerceAtLeast(0.0)

                val base =
                    (subtotalReal - descuentoReal)
                        .coerceAtLeast(0.0)

                val ivaReal =
                    ivaPorcentaje.coerceAtLeast(0.0)

                val ivaImporte =
                    base * (ivaReal / 100.0)

                val total =
                    base + ivaImporte

                val orden = OrdenEntity(

                    folio = folio,

                    fecha =
                        SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                        ).format(Date()),

                    numeroCliente =
                        numeroCliente.trim(),

                    nombreCliente =
                        nombreCliente.trim(),

                    empresa =
                        empresa.trim(),

                    telefono =
                        telefono.trim(),

                    correo =
                        correo.trim(),

                    direccion =
                        direccion.trim(),

                    tipoTrabajo =
                        tipoTrabajo.trim(),

                    descripcionTrabajo =
                        descripcionTrabajo.trim(),

                    fechaProgramada =
                        fechaProgramada.trim(),

                    subtotal =
                        subtotalReal,

                    descuento =
                        descuentoReal,

                    ivaPorcentaje =
                        ivaReal,

                    ivaImporte =
                        ivaImporte,

                    total =
                        total,

                    tipoElemento =
                        tipoElemento.trim(),

                    alto =
                        alto.coerceAtLeast(0.0),

                    ancho =
                        ancho.coerceAtLeast(0.0),

                    largo =
                        largo.coerceAtLeast(0.0),

                    cantidad =
                        cantidad.coerceAtLeast(0),

                    material =
                        material.trim(),

                    perfilCalibre =
                        perfilCalibre.trim(),

                    acabado =
                        acabado.trim(),

                    color =
                        color.trim(),

                    observacionesTecnicas =
                        observacionesTecnicas.trim(),

                    disenoUri =
                        disenoUri.trim()
                )

                repository.insertar(orden)

                onResultado(true)

            } catch (_: Exception) {

                onResultado(false)
            }
        }
    }

    fun cambiarEstado(
        orden: OrdenEntity,
        nuevoEstado: String,
        onResultado: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {

                val ahora =
                    System.currentTimeMillis()

                val actualizada =
                    when (nuevoEstado) {

                        "Autorizada" ->
                            orden.copy(
                                estado = nuevoEstado,
                                fechaAutorizacion =
                                    orden.fechaAutorizacion
                                        ?: ahora
                            )

                        "En proceso" ->
                            orden.copy(
                                estado = nuevoEstado,
                                fechaInicio =
                                    orden.fechaInicio
                                        ?: ahora
                            )

                        "Finalizada" ->
                            orden.copy(
                                estado = nuevoEstado,
                                fechaFinalizacion =
                                    orden.fechaFinalizacion
                                        ?: ahora
                            )

                        else ->
                            orden.copy(
                                estado = nuevoEstado
                            )
                    }

                repository.actualizar(actualizada)

                onResultado(true)

            } catch (_: Exception) {

                onResultado(false)
            }
        }
    }

    fun marcarPdfGenerado(
        orden: OrdenEntity
    ) {

        viewModelScope.launch {

            try {

                repository.actualizar(
                    orden.copy(
                        pdfGenerado = true
                    )
                )

            } catch (_: Exception) {
            }
        }
    }

    fun eliminarOrden(
        orden: OrdenEntity
    ) {

        viewModelScope.launch {

            try {

                repository.eliminar(orden)

            } catch (_: Exception) {
            }
        }
    }
}
