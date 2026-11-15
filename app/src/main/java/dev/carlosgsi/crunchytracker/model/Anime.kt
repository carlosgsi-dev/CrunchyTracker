package dev.carlosgsi.crunchytracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity le dice a Room que esta clase será una tabla en la base de datos local
@Entity(tableName = "mis_series")
data class Anime(
    // Usaremos el ID real de Kitsu, no uno inventado
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val titulo: String,
    val urlImagen: String,
    var episodiosVistos: Int,
    val episodiosTotales: Int
) {
    fun calcularPorcentajeProgreso(): Int {
        if (episodiosTotales == 0) return 0
        return ((episodiosVistos.toDouble() / episodiosTotales) * 100).toInt()
    }
}