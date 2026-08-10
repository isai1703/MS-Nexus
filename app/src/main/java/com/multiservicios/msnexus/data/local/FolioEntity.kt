package com.multiservicios.msnexus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folios")
data class FolioEntity(
    @PrimaryKey
    val tipo: String,
    val ultimoNumero: Long = 0
)
