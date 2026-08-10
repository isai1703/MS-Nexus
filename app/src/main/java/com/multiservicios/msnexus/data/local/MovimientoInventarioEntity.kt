package com.multiservicios.msnexus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movimientos_inventario")
data class MovimientoInventarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val materialId: Long,
    val folioOrden: String = "",
    val tipo: String,
    val cantidad: Double,
    val existenciaAnterior: Double,
    val existenciaNueva: Double,
    val fecha: Long = System.currentTimeMillis(),
    val observaciones: String = ""
)
