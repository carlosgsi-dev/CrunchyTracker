package dev.carlosgsi.crunchytracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.carlosgsi.crunchytracker.model.Anime

@Dao
interface AnimeDao {

    // Obtiene todas las series guardadas localmente ordenadas alfabéticamente
    @Query("SELECT * FROM mis_series ORDER BY titulo ASC")
    suspend fun obtenerMisSeries(): List<Anime>

    // Busca una serie concreta por su ID de Kitsu (útil para saber cuántos capítulos llevas si reaparece en una búsqueda)
    @Query("SELECT * FROM mis_series WHERE id = :animeId LIMIT 1")
    suspend fun obtenerPorId(animeId: Int): Anime?

    // Inserta una serie nueva. Si el ID ya existe en la tabla (REPLACE), actualiza sus episodios vistos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarOActualizar(anime: Anime)

    // Elimina una serie de tu base de datos local
    @Delete
    suspend fun eliminar(anime: Anime)
}