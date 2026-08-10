package com.multiservicios.msnexus.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    @Insert
    suspend fun insertar(cliente: ClienteEntity): Long

    @Update
    suspend fun actualizar(cliente: ClienteEntity)

    @Delete
    suspend fun eliminar(cliente: ClienteEntity)

    @Query("SELECT * FROM clientes ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<ClienteEntity>>

    @Query("SELECT * FROM clientes WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): ClienteEntity?

    @Query("""
        SELECT * FROM clientes
        WHERE nombre LIKE '%' || :texto || '%'
        OR empresa LIKE '%' || :texto || '%'
        OR numeroCliente LIKE '%' || :texto || '%'
        ORDER BY id DESC
    """)
    fun buscar(texto: String): Flow<List<ClienteEntity>>

    @Query("SELECT * FROM clientes ORDER BY id DESC LIMIT 1")
    suspend fun obtenerUltimo(): ClienteEntity?

    @Query("SELECT COUNT(*) FROM clientes")
    suspend fun contar(): Int
}
