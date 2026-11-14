package dev.carlosgsi.crunchytracker.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Nueva URL base de Kitsu
    private const val BASE_URL = "https://kitsu.io/api/edge/"

    val apiService: KitsuApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KitsuApiService::class.java)
    }
}