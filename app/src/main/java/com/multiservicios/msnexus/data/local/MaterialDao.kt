package com.multiservicios.msnexus.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {

    @Insert
    suspend fun insertar(material: MaterialEntity): Long

    @Update
    suspend fun actualizar(material: MaterialEntity)

    @Delete
    suspend fun eliminar(material: MaterialEntity)

    @Query("SELECT * FROM materiales ORDER BY nombre ASC")
    fun obtenerTodos(): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM materiales WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): MaterialEntity?

    @Query("""
        SELECT * FROM materiales
        WHERE nombre LIKE '%' || :texto || '%'
        OR codigo LIKE '%' || :texto || '%'
        OR categoria LIKE '%' || :texto || '%'
        ORDER BY nombre ASC
    """)
    fun buscar(texto: String): Flow<List<MaterialEntity>>

    @Query("UPDATE materiales SET existencia = :nuevaExistencia WHERE id = :materialId")
    suspend fun actualizarExistencia(
        materialId: Long,
        nuevaExistencia: Double
    )
}
