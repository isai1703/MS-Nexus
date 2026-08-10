package com.multiservicios.msnexus.data.repository

import androidx.room.withTransaction
import com.multiservicios.msnexus.data.local.AppDatabase
import com.multiservicios.msnexus.data.local.FolioEntity

class FolioRepository(
    private val database: AppDatabase
) {

    suspend fun siguienteCliente(): String {
        return siguiente("CLIENTE", "C")
    }

    suspend fun siguienteOrden(): String {
        return siguiente("ORDEN", "OT")
    }

    private suspend fun siguiente(
        tipo: String,
        prefijo: String
    ): String {

        val numero = database.withTransaction {
            val dao = database.folioDao()
            val folioActual = dao.obtener(tipo)

            if (folioActual == null) {
                dao.insertar(
                    FolioEntity(
                        tipo = tipo,
                        ultimoNumero = 1
                    )
                )
                1L
            } else {
                dao.incrementar(tipo)
                folioActual.ultimoNumero + 1
            }
        }

        return "$prefijo-${"%06d".format(numero)}"
    }
}
