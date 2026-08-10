package com.multiservicios.msnexus.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdenMaterialDao {

    @Insert
    suspend fun insertar(detalle: OrdenMaterialEntity): Long

    @Update
    suspend fun actualizar(detalle: OrdenMaterialEntity)

    @Query("""
        SELECT * FROM ordenes_materiales
        WHERE ordenId = :ordenId
        ORDER BY id ASC
    """)
    fun obtenerPorOrden(ordenId: Long): Flow<List<OrdenMaterialEntity>>

    @Query("""
        SELECT * FROM ordenes_materiales
        WHERE ordenId = :ordenId
    """)
    suspend fun obtenerPorOrdenDirecto(ordenId: Long): List<OrdenMaterialEntity>

    @Query("""
        UPDATE ordenes_materiales
        SET inventarioDescontado = 1
        WHERE id = :detalleId
    """)
    suspend fun marcarInventarioDescontado(detalleId: Long)
}
