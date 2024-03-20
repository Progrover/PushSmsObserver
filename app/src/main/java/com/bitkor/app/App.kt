package com.bitkor.app

import android.app.Application
import com.bitkor.app.data.provider.SharedPreferencesProvider
import com.bitkor.app.data.provider.TokenProvider
import okhttp3.OkHttpClient
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class App : Application() {
    lateinit var executor: ExecutorService
        private set

    lateinit var client: OkHttpClient
        private set

    override fun onCreate() {
        app = this

        executor = Executors.newCachedThreadPool()

        client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        super.onCreate()

        SharedPreferencesProvider.init(this)

        if (TokenProvider.deviceId.isBlank()) {
            TokenProvider.deviceId = UUID.randomUUID().toString()
        }
    }

    companion object {
        lateinit var app: App
            private set
    }
}