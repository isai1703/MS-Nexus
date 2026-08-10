package com.multiservicios.msnexus.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FolioDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(folio: FolioEntity): Long

    @Update
    suspend fun actualizar(folio: FolioEntity)

    @Query("SELECT * FROM folios WHERE tipo = :tipo LIMIT 1")
    suspend fun obtener(tipo: String): FolioEntity?

    @Query("UPDATE folios SET ultimoNumero = ultimoNumero + 1 WHERE tipo = :tipo")
    suspend fun incrementar(tipo: String)

    @Query("SELECT ultimoNumero FROM folios WHERE tipo = :tipo LIMIT 1")
    suspend fun obtenerUltimoNumero(tipo: String): Long?
}
