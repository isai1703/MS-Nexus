package com.multiservicios.msnexus.data.repository

import com.multiservicios.msnexus.data.local.ClienteDao
import com.multiservicios.msnexus.data.local.ClienteEntity

class ClienteRepository(
    private val clienteDao: ClienteDao,
    private val folioRepository: FolioRepository
) {

    fun obtenerTodos() = clienteDao.obtenerTodos()

    fun buscar(texto: String) = clienteDao.buscar(texto)

    suspend fun obtenerPorId(id: Long): ClienteEntity? {
        return clienteDao.obtenerPorId(id)
    }

    suspend fun crear(
        nombre: String,
        empresa: String,
        rfc: String,
        razonSocial: String,
        regimenFiscal: String,
        usoCfdi: String,
        codigoPostalFiscal: String,
        telefono: String,
        correo: String,
        direccion: String
    ): Long {

        val numeroCliente = folioRepository.siguienteCliente()

        return clienteDao.insertar(
            ClienteEntity(
                numeroCliente = numeroCliente,
                nombre = nombre,
                empresa = empresa,
                rfc = rfc,
                razonSocial = razonSocial,
                regimenFiscal = regimenFiscal,
                usoCfdi = usoCfdi,
                codigoPostalFiscal = codigoPostalFiscal,
                telefono = telefono,
                correo = correo,
                direccion = direccion
            )
        )
    }

    suspend fun actualizar(cliente: ClienteEntity) {
        clienteDao.actualizar(cliente)
    }

    suspend fun eliminar(cliente: ClienteEntity) {
        clienteDao.eliminar(cliente)
    }
}
