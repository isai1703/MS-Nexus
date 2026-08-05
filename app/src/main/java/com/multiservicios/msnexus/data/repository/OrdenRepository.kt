package com.multiservicios.msnexus.data.repository

import com.multiservicios.msnexus.data.local.OrdenDao
import com.multiservicios.msnexus.data.local.OrdenEntity

class OrdenRepository(
    private val ordenDao: OrdenDao
) {

    fun obtenerTodas() = ordenDao.obtenerTodas()

    fun observarPorId(id: Long) = ordenDao.observarPorId(id)

    fun buscar(texto: String) = ordenDao.buscar(texto)

    suspend fun obtenerPorId(id: Long): OrdenEntity? {
        return ordenDao.obtenerPorId(id)
    }

    suspend fun insertar(orden: OrdenEntity): Long {
        return ordenDao.insertar(orden)
    }

    suspend fun actualizar(orden: OrdenEntity) {
        ordenDao.actualizar(orden)
    }

    suspend fun eliminar(orden: OrdenEntity) {
        ordenDao.eliminar(orden)
    }
}
