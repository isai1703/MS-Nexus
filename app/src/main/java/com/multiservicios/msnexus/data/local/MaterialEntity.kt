package com.multiservicios.msnexus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materiales")
data class MaterialEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val codigo: String = "",
    val nombre: String,
    val categoria: String = "",
    val unidad: String,
    val existencia: Double = 0.0,
    val stockMinimo: Double = 0.0,
    val proveedor: String = "",
    val observaciones: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)
