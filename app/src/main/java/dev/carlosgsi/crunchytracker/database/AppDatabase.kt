package dev.carlosgsi.crunchytracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.carlosgsi.crunchytracker.model.Anime

// Declaramos qué tablas (entities) forman la base de datos y su versión de esquema
@Database(entities = [Anime::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun animeDao(): AnimeDao

    companion object {
        // @Volatile garantiza que cualquier hilo lea siempre el estado más reciente de la instancia en memoria
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "crunchytracker_db" // Nombre del archivo físico SQLite dentro del almacenamiento interno
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}