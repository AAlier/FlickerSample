package com.flicksample.data.network

import com.flicksample.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HttpBuilder {
    private val DEFAULT_API_HOST = "https://api.flickr.com"
    private val TIMEOUT_INTERVAL = 1L

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }

    private fun initOkHttpClient(): OkHttpClient {
        val okhttp = OkHttpClient.Builder()
            .writeTimeout(TIMEOUT_INTERVAL, TimeUnit.MINUTES)
            .readTimeout(TIMEOUT_INTERVAL, TimeUnit.MINUTES)
            .connectTimeout(TIMEOUT_INTERVAL, TimeUnit.MINUTES)

        if (BuildConfig.DEBUG) {
            okhttp.addInterceptor(provideLoggingInterceptor())
        }
        return okhttp.build()
    }

    fun initRetrofit(baseUrl: String = DEFAULT_API_HOST): ForumService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(initOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ForumService::class.java)
    }
}
