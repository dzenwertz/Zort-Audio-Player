package com.aurastream.mobile.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    var currentBaseUrl: String = "http://10.0.2.2:8080/"
        private set

    private var _apiService: AuraApiService? = null

    fun setBaseUrl(url: String) {
        currentBaseUrl = if (url.endsWith("/")) url else "$url/"
        _apiService = buildApiService()
    }

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private fun buildApiService(): AuraApiService {
        return Retrofit.Builder()
            .baseUrl(currentBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuraApiService::class.java)
    }

    val apiService: AuraApiService
        get() {
            if (_apiService == null) {
                _apiService = buildApiService()
            }
            return _apiService!!
        }
}
