package com.bitkor.app.ui.screen

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bitkor.app.ui.theme.BitkorTypography
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsScreen(permissions: MultiplePermissionsState) {
    val permission = permissions.permissions.first { !it.status.isGranted }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            val text = when (permission.permission) {
                Manifest.permission.READ_SMS -> {
                    "Для работы приложение требуется разрешение на чтение входящих сообщений."
                }

                Manifest.permission.RECEIVE_SMS -> {
                    "Для работы приложение требуется разрешение на получение входящих сообщений."
                }

                Manifest.permission.POST_NOTIFICATIONS -> {
                    "Для работы приложения требуется разрешение на показ уведомлений."
                }

                else -> ""
            }
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(72.dp),
                imageVector = Icons.Filled.WarningAmber,
                contentDescription = text,
                tint = Color(0xFFFFCC00),
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = BitkorTypography.titleMedium,
                textAlign = TextAlign.Center,
                text = text,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { permission.launchPermissionRequest() },
                content = {
                    Text("Выдать разрешение")
                },
            )
        }
    }
}