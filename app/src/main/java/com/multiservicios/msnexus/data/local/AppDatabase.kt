package com.multiservicios.msnexus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        OrdenEntity::class,
        ClienteEntity::class,
        MaterialEntity::class,
        OrdenMaterialEntity::class,
        MovimientoInventarioEntity::class,
        FolioEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ordenDao(): OrdenDao
    abstract fun clienteDao(): ClienteDao
    abstract fun materialDao(): MaterialDao
    abstract fun ordenMaterialDao(): OrdenMaterialDao
    abstract fun movimientoInventarioDao(): MovimientoInventarioDao
    abstract fun folioDao(): FolioDao

    companion object {

        private val MIGRATION_3_4 = object : Migration(3, 4) {

            override fun migrate(
                database: SupportSQLiteDatabase
            ) {

                database.execSQL(
                    "ALTER TABLE ordenes ADD COLUMN tipoElemento TEXT NOT NULL DEFAULT ''"
                )

                database.execSQL(
                    "ALTER TABLE ordenes ADD COLUMN alto REAL NOT NULL DEFAULT 0.0"
                )

                database.execSQL(
                    "ALTER TABLE ordenes ADD COLUMN ancho REAL NOT NULL DEFAULT 0.0"
                )

                database.execSQL(
                    "ALTER TABLE ordenes ADD COLUMN largo REAL NOT NULL DEFAULT 0.0"
                )

                database.execSQL(
                    "ALTER TABLE ordenes ADD COLUMN cantidad INTEGER NOT NULL DEFAULT 0"
                )

                database.execSQL(
                    "ALTER TABLE ordenes ADD COLUMN material TEXT NOT NULL DEFAULT ''"
                )

                database.execSQL(
                    "ALTER TABLE ordenes ADD COLUMN perfilCalibre TEXT NOT NULL DEFAULT ''"
                )

                database.execSQL(
                    "ALTER TABLE ordenes ADD COLUMN acabado TEXT NOT NULL DEFAULT ''"
                )

                database.execSQL(
                    "ALTER TABLE ordenes ADD COLUMN color TEXT NOT NULL DEFAULT ''"
                )

                database.execSQL(
                    "ALTER TABLE ordenes ADD COLUMN observacionesTecnicas TEXT NOT NULL DEFAULT ''"
                )

                database.execSQL(
                    "ALTER TABLE ordenes ADD COLUMN disenoUri TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ms_nexus_database"
                )
                    .addMigrations(MIGRATION_3_4)
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}
