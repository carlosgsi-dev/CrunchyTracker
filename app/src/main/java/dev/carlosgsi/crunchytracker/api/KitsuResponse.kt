package dev.carlosgsi.crunchytracker.api

import com.google.gson.annotations.SerializedName

data class KitsuResponse(
    @SerializedName("data") val data: List<AnimeDto>
)

data class AnimeDto(
    @SerializedName("id") val id: String,
    @SerializedName("attributes") val attributes: AnimeAttributes
) {
    fun toAnimeLocal(): dev.carlosgsi.crunchytracker.model.Anime {
        return dev.carlosgsi.crunchytracker.model.Anime(
            id = this.id.toIntOrNull() ?: 0,
            titulo = this.attributes.titulo ?: "Sin título",
            urlImagen = this.attributes.posterImage?.original ?: "",
            episodiosVistos = 0,
            episodiosTotales = this.attributes.episodiosTotales ?: 0
        )
    }
}

data class AnimeAttributes(
    @SerializedName("canonicalTitle") val titulo: String?,
    @SerializedName("episodeCount") val episodiosTotales: Int?,
    @SerializedName("posterImage") val posterImage: PosterImage?
)

data class PosterImage(
    @SerializedName("original") val original: String
)