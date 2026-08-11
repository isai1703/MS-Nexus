package com.multiservicios.msnexus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ordenes")
data class OrdenEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val folio: String,

    val fecha: String,

    val numeroCliente: String,

    val nombreCliente: String,

    val empresa: String,

    val telefono: String,

    val correo: String,

    val direccion: String,

    val tipoTrabajo: String,

    val descripcionTrabajo: String,

    val fechaProgramada: String = "",

    val subtotal: Double,

    val descuento: Double,

    val ivaPorcentaje: Double,

    val ivaImporte: Double,

    val total: Double,

    val estado: String = "Pendiente",

    val pdfGenerado: Boolean = false,

    val fechaAutorizacion: Long? = null,

    val fechaInicio: Long? = null,

    val fechaFinalizacion: Long? = null,

    val fechaCreacion: Long = System.currentTimeMillis(),

    val disenoAprobado: String? = null
)
