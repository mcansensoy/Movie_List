package com.example.omdb.data.remote

import okhttp3.Interceptor
import okhttp3.Response
//import com.example.omdb.BuildConfig

// Bu interceptor her isteğin URL’ine ?apikey=... ekler.
// Böylece Retrofit arayüzünde tekrar tekrar @Query("apikey") yazmayız.
class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val urlWithKey = original.url.newBuilder()
            .addQueryParameter("apikey", "b215de35") // OMDb için zorunlu parametre.
            .build()

        val newRequest = original.newBuilder()
            .url(urlWithKey)
            .build()

        return chain.proceed(newRequest)
    }
}