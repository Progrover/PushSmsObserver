package com.bitkor.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bitkor.app.R
import com.bitkor.app.ui.activity.MainActivity

class BitkorService : Service() {
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val handler = Handler()
        val runnable = object: Runnable {
            override fun run() {
                handler.postDelayed(this, 5000)
                sendNotification()
            }
        }
        handler.postDelayed(runnable, 1000)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancel(DEFAULT_NOTIFICATION_ID)
        stopSelf()
    }

    override fun onBind(intent: Intent?) = null

    private fun sendNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val contentIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        createChannel()
        var info = "Отслеживание смс"
        if (!isOnline(applicationContext)) {info = "Отслеживание смc\n" + "Аларм! Отсутствует интернет!"}
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentIntent(contentIntent)
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(info)
            .build()
        startForeground(DEFAULT_NOTIFICATION_ID, notification)
    }

    private fun createChannel() {
        notificationManager.createNotificationChannel(NotificationChannel(
            CHANNEL_ID, "Bitkor Channel", NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Bitkor notification channel"
        })
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private companion object {
        private const val DEFAULT_NOTIFICATION_ID = 102
        private const val CHANNEL_ID = "CHANNEL_ID"
    }
}