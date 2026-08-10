package com.multiservicios.msnexus.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoInventarioDao {

    @Insert
    suspend fun insertar(movimiento: MovimientoInventarioEntity): Long

    @Query("""
        SELECT * FROM movimientos_inventario
        ORDER BY fecha DESC
    """)
    fun obtenerTodos(): Flow<List<MovimientoInventarioEntity>>

    @Query("""
        SELECT * FROM movimientos_inventario
        WHERE folioOrden = :folio
        ORDER BY fecha DESC
    """)
    fun obtenerPorFolio(folio: String): Flow<List<MovimientoInventarioEntity>>

    @Query("""
        SELECT * FROM movimientos_inventario
        WHERE materialId = :materialId
        ORDER BY fecha DESC
    """)
    fun obtenerPorMaterial(materialId: Long): Flow<List<MovimientoInventarioEntity>>
}
