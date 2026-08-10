package com.multiservicios.msnexus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class ClienteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val numeroCliente: String,
    val nombre: String,
    val empresa: String = "",
    val rfc: String = "",
    val razonSocial: String = "",
    val regimenFiscal: String = "",
    val usoCfdi: String = "",
    val codigoPostalFiscal: String = "",
    val telefono: String = "",
    val correo: String = "",
    val direccion: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)
