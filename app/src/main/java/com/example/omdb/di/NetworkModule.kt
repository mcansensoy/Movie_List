package com.example.omdb.di

import com.example.omdb.api.OmdbApiService
import com.example.omdb.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class) // application scope
object NetworkModule {

    private const val BASE_URL = "https://www.omdbapi.com/"

    // ApiKey interceptor: her isteğe ?apikey=... ekler
    @Provides
    fun provideApiKeyInterceptor(): Interceptor = Interceptor { chain ->
        val original = chain.request()
        val originalUrl: HttpUrl = original.url
        val url = originalUrl.newBuilder()
            .addQueryParameter("apikey", BuildConfig.OMDB_API_KEY) // BuildConfig içinden alıyoruz
            .build()
        val request = original.newBuilder().url(url).build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(apiKeyInterceptor: Interceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOmdbApi(retrofit: Retrofit): OmdbApiService =
        retrofit.create(OmdbApiService::class.java)
}