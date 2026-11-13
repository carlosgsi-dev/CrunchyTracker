package dev.carlosgsi.crunchytracker.model

data class Anime(
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