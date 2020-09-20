package com.example.demoapplication.networking

import android.text.TextUtils
import com.example.demoapplication.localDatabase.staticValue.StaticValue.Companion.DEBUG
import com.example.demoapplication.localDatabase.staticValue.StaticValue.Companion.REQUEST_TIMEOUT_DURATION
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object RetrofitNetworking {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit? {
        Timber.e(baseUrl)
        if (retrofit == null || !TextUtils.isEmpty(baseUrl)) {
            val gson = GsonBuilder()
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create()
            retrofit = Retrofit.Builder()
                .client(createRequestInterceptorClient())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))//GsonConverter
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava2
                .build()
        } else {
            Timber.e("Retrofit is not null || BaseUrl is null")
        }


        return retrofit

    }
    /*Client*/
    private fun createRequestInterceptorClient(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        //TODO While release in Google Play Change the Level to NONE
        return if (DEBUG) {
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .build()
        } else {
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .build()
        }
    }
}