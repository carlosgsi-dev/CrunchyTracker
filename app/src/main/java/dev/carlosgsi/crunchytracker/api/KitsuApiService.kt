package dev.carlosgsi.crunchytracker.api

import retrofit2.Response
import retrofit2.http.GET

interface KitsuApiService {
    @GET("trending/anime")
    suspend fun getTrendingAnime(): Response<KitsuResponse>
}