package com.multiservicios.msnexus.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdenDao {

    @Insert
    suspend fun insertar(orden: OrdenEntity): Long

    @Update
    suspend fun actualizar(orden: OrdenEntity)

    @Delete
    suspend fun eliminar(orden: OrdenEntity)

    @Query("SELECT * FROM ordenes ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<OrdenEntity>>

    @Query("SELECT * FROM ordenes WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): OrdenEntity?

    @Query("SELECT * FROM ordenes WHERE id = :id LIMIT 1")
    fun observarPorId(id: Long): Flow<OrdenEntity?>

    @Query("SELECT * FROM ordenes WHERE nombreCliente LIKE '%' || :texto || '%' OR empresa LIKE '%' || :texto || '%' OR numeroCliente LIKE '%' || :texto || '%' ORDER BY id DESC")
    fun buscar(texto: String): Flow<List<OrdenEntity>>
}
