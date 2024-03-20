package com.bitkor.app.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bitkor.app.R
import com.bitkor.app.service.BitkorService
import com.bitkor.app.state.AppController
import com.bitkor.app.ui.BitkorApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BitkorApp(AppController().apply { launch() })
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, BitkorService::class.java)
        startForegroundService(intent)
        ignoreBatteryOptimization()
    }

    @SuppressLint("BatteryLife")
    private fun ignoreBatteryOptimization() {
        val pm = getSystemService(POWER_SERVICE) as? PowerManager
        if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
            startActivity(Intent().apply {
                action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                data = Uri.parse("package:$packageName")
            })
        }
    }
}