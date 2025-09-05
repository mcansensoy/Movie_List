package com.example.omdb.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// Hilt/Koin gibi DI yok; basit tutuyoruz. Öğrenme aşamasında yeterli.
object NetworkModule {

    private const val BASE_URL = "https://www.omdbapi.com/"

    private val moshi: Moshi = Moshi.Builder()
        // Kotlin data class desteği (reflection yolu ile)
        .add(KotlinJsonAdapterFactory())
        .build()

    private val logging = HttpLoggingInterceptor().apply {
        // DEBUG aşamasında gövde loglayalım
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttp 5.1.0 – standart builder API (interceptor + logging)  :contentReference[oaicite:17]{index=17}
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor())
        .addInterceptor(logging)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // Retrofit 3 + Moshi  :contentReference[oaicite:18]{index=18}
        .build()

    val omdbApi: OmdbApi = retrofit.create(OmdbApi::class.java)
}