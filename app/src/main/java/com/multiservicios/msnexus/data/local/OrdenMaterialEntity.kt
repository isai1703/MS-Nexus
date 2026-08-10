package com.multiservicios.msnexus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ordenes_materiales")
data class OrdenMaterialEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val ordenId: Long,
    val materialId: Long,
    val cantidad: Double,
    val observaciones: String = "",
    val inventarioDescontado: Boolean = false
)
