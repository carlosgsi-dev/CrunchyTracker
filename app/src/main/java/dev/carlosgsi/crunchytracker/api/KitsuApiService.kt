package dev.carlosgsi.crunchytracker.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KitsuApiService {
    @GET("trending/anime")
    suspend fun getTrendingAnime(): Response<KitsuResponse>

    // Nuevo endpoint para buscar por nombre
    // Retrofit convertirá esto en: https://kitsu.io/api/edge/anime?filter[text]=tu_busqueda
    @GET("anime")
    suspend fun buscarAnime(@Query("filter[text]") query: String): Response<KitsuResponse>
}