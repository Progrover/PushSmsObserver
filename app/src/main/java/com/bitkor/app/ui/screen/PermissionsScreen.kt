package com.bitkor.app.ui.screen

import android.Manifest
import android.graphics.Paint.Style
import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bitkor.app.state.AppController
import com.bitkor.app.ui.theme.BitkorTheme
import com.bitkor.app.ui.theme.BitkorTypography
import com.bitkor.app.ui.theme.CustomColor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.MultiplePermissionsState as MultiplePermissionsState

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
                    "Необходимо выдать разрешение на чтение входящих сообщений."
                }

                Manifest.permission.RECEIVE_SMS -> {
                    "Необходимо выдать разрешение на получение входящих сообщений."
                }

                Manifest.permission.POST_NOTIFICATIONS -> {
                    "Необходимо выдать разрешение на показ уведомлений."
                }

                else -> ""
            }
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(100.dp),
                imageVector = Icons.Filled.WarningAmber,
                contentDescription = text,
                tint = CustomColor().green_color_for_buttons,
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(40.dp, 0.dp),
                style = BitkorTypography.titleMedium,
                textAlign = TextAlign.Justify,
                text = text,
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { permission.launchPermissionRequest() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomColor().green_color_for_tint
                ),
                shape = Shapes().large,
                content = {
                    Text("Выдать разрешение",
                        style = BitkorTypography.bodyLarge,
                        color = Color.Black
                        )
                },
            )
        }
    }
}
