package com.bitkor.app.ui

import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person4
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.glance.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bitkor.app.data.model.PaymentMethod
import com.bitkor.app.data.model.PaymentRequisites
import com.bitkor.app.state.AppController
import com.bitkor.app.ui.screen.LoginScreen
import com.bitkor.app.ui.screen.MainScreen
import com.bitkor.app.ui.screen.PermissionsScreen
import com.bitkor.app.ui.theme.BitkorTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import com.bitkor.app.ui.theme.CustomColor

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BitkorApp(controller: AppController) {
    BitkorTheme {
        val permissions = rememberMultiplePermissionsState(
            permissions = mutableListOf(
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
            ).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        )

        var newRequisiteDialogVisible by remember { mutableStateOf(false) }
        val state by controller.state.collectAsStateWithLifecycle()
        val coroutineScope = rememberCoroutineScope()


        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (null != state.token) {
                    TopAppBar(
                        navigationIcon = {
                            Icon(
                                modifier = Modifier.size(40.dp),
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                tint = CustomColor().green_color_for_buttons,
                            )
                        },
                        title = {
                            Column(
                                modifier = Modifier,
                                content = {
                                    Text(
                                        style = MaterialTheme.typography.titleLarge,
                                        text = "Профиль",
                                        color = Color.Black
                                    )
                                    Text(
                                        style = MaterialTheme.typography.titleSmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        text = state.token ?: "",
                                       color = Color.Black
                                    )
                                }
                            )
                        },
                        actions = {
                            IconButton(onClick = { coroutineScope.launch { controller.showLogoutConfirm() } }) {
                                Icon(
                                    tint = CustomColor().green_color_for_buttons,
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = null,
                                )
                            }
                        }
                    )
                }
            },
            content = { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues), content = {
                    if (!permissions.permissions.all { it.status.isGranted }) {
                        PermissionsScreen(permissions = permissions)
                    } else if (null == state.token) {
                        LoginScreen(controller)
                    } else {
                        MainScreen(controller)
                    }
                    var requisitePaymentMethodLabel by remember(newRequisiteDialogVisible) {
                        mutableStateOf("Банковская карта")
                    }
                    var requisitePaymentMethodValue by remember(newRequisiteDialogVisible) {
                        mutableStateOf(PaymentMethod.CARD)
                    }
                    var requisitePaymentMethodExpanded by remember(newRequisiteDialogVisible) {
                        mutableStateOf(false)
                    }
                    var requisiteNumberValue by remember(newRequisiteDialogVisible) { mutableStateOf("") }
                    if (newRequisiteDialogVisible) {
                        AlertDialog(
                            onDismissRequest = { newRequisiteDialogVisible = false },
                            dismissButton = {
                                Button(onClick = { newRequisiteDialogVisible = false },
                                    shape = Shapes().large,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CustomColor().green_color_for_buttons
                                    )) {
                                    Text(text = "Закрыть",
                                        color = Color.Black)


                                }
                            },
                            confirmButton = {
                                Button(
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CustomColor().green_color_for_buttons,
                                        disabledContainerColor = CustomColor().green_color_for_tint

                                    ),
                                    onClick = {
                                        val requisite = PaymentRequisites(
                                            method = requisitePaymentMethodValue,
                                            number = requisiteNumberValue,
                                        )
                                        coroutineScope.launch {
                                            controller.addRequisite(requisite)
                                        }
                                        newRequisiteDialogVisible = false
                                    },
                                    shape = Shapes().large,
                                    enabled = when (requisitePaymentMethodValue) {
                                        PaymentMethod.CARD -> {
                                            val regex = "^\\d{15,20}$".toRegex()
                                            regex in requisiteNumberValue
                                        }

                                        PaymentMethod.SIM -> {
                                            val regex = "^[+]7\\d{10}$".toRegex()
                                            regex in requisiteNumberValue
                                        }
                                        PaymentMethod.PHONE -> {
                                            val regex = "^7\\d{10}$".toRegex()
                                            regex in requisiteNumberValue
                                        }
                                    },
                                    content = {
                                        Text(text = "Добавить",
                                            color = Color.Black)
                                    },
                                )
                            },
                            title = { Text(text = "Новые реквизиты") },
                            text = {
                                Column(
                                    modifier = Modifier,
                                    content = {
                                        Text(
                                            modifier = Modifier.padding(0.dp, 5.dp),
                                            style = MaterialTheme.typography.labelLarge,
                                            text = "Выберите тип",
                                            color = Color.Black
                                        )
                                        ExposedDropdownMenuBox(
                                            expanded = requisitePaymentMethodExpanded,
                                            onExpandedChange = {
                                                requisitePaymentMethodExpanded =
                                                    !requisitePaymentMethodExpanded
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            content = {
                                                TextField(
                                                    modifier = Modifier.menuAnchor(),
                                                    readOnly = true,
                                                    value = requisitePaymentMethodLabel,
                                                    onValueChange = { },
                                                    trailingIcon = {
                                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                                            expanded = requisitePaymentMethodExpanded
                                                        )
                                                    },
                                                    singleLine = true,
                                                    maxLines = 1,
                                                    shape = MaterialTheme.shapes.extraLarge,
                                                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                                                        focusedContainerColor = CustomColor().green_color_for_tint,
                                                        unfocusedContainerColor = CustomColor().green_color_for_tint,
                                                        focusedIndicatorColor = Color.Transparent,
                                                        disabledIndicatorColor = Color.Transparent,
                                                        errorIndicatorColor = Color.Transparent,
                                                        unfocusedIndicatorColor = Color.Transparent,
                                                    ),
                                                )
                                                ExposedDropdownMenu(
                                                    expanded = requisitePaymentMethodExpanded,
                                                    onDismissRequest = {
                                                        requisitePaymentMethodExpanded = false
                                                    },
                                                    content = {
                                                        DropdownMenuItem(
                                                            text = { Text(text = "Банковская карта") },
                                                            enabled = requisitePaymentMethodValue != PaymentMethod.CARD,
                                                            onClick = {
                                                                requisitePaymentMethodLabel =
                                                                    "Банковская карта"
                                                                requisitePaymentMethodValue =
                                                                    PaymentMethod.CARD
                                                                requisitePaymentMethodExpanded =
                                                                    false
                                                            },
                                                        )
                                                        DropdownMenuItem(
                                                            text = { Text(text = "Система Быстрых Платежей") },
                                                            enabled = requisitePaymentMethodValue != PaymentMethod.SIM,
                                                            onClick = {
                                                                requisitePaymentMethodLabel =
                                                                    "Система Быстрых Платежей"
                                                                requisitePaymentMethodValue =
                                                                    PaymentMethod.SIM
                                                                requisitePaymentMethodExpanded =
                                                                    false
                                                            },
                                                        )
                                                        DropdownMenuItem(
                                                            text = { Text(text = "Номер телефона") },
                                                            enabled = requisitePaymentMethodValue != PaymentMethod.PHONE,
                                                            onClick = {
                                                                requisitePaymentMethodLabel =
                                                                    "Номер телефона"
                                                                requisitePaymentMethodValue =
                                                                    PaymentMethod.PHONE
                                                                requisitePaymentMethodExpanded =
                                                                    false
                                                            },
                                                        )
                                                    },
                                                )
                                            },
                                        )
                                        Spacer(modifier = Modifier.height(20.dp))
                                        Text(
                                            modifier = Modifier.padding(0.dp, 5.dp),
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Color.Black,
                                            text = when (requisitePaymentMethodValue) {
                                                PaymentMethod.SIM -> "Введите номер телефона"
                                                PaymentMethod.CARD -> "Введите номер карты"
                                                PaymentMethod.PHONE -> "Введите номер телефона"
                                            },
                                        )
                                        TextField(
                                            value = requisiteNumberValue,
                                            onValueChange = { requisiteNumberValue = it },
                                            shape = MaterialTheme.shapes.extraLarge,
                                            placeholder = {
                                                when (requisitePaymentMethodValue) {
                                                    PaymentMethod.SIM -> {
                                                        Text(text = "+71234567890")
                                                    }
                                                    PaymentMethod.CARD -> {
                                                        Text(text = "1234123412341234")
                                                    }
                                                    PaymentMethod.PHONE -> {
                                                        Text(text = "71234567890")
                                                    }
                                                }
                                            },
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = CustomColor().green_color_for_tint,
                                                unfocusedContainerColor = CustomColor().green_color_for_tint,
                                                unfocusedIndicatorColor = Color.Transparent,
                                                errorIndicatorColor = Color.Transparent,
                                                disabledIndicatorColor = Color.Transparent,
                                                focusedIndicatorColor = Color.Transparent,
                                            ),
                                        )
                                    },
                                )
                            },
                        )
                    }
                    if (state.logoutConfirmVisible) {
                        AlertDialog(
                            onDismissRequest = {
                                coroutineScope.launch {
                                    controller.hideLogoutConfirm()
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            controller.hideLogoutConfirm()
                                        }
                                    },
                                    shape = Shapes().large,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CustomColor().green_color_for_buttons
                                    ),
                                    content = {
                                        Text(text = "Нет",
                                            color = Color.Black)
                                    },
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            controller.logout()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CustomColor().green_color_for_buttons
                                    ),
                                    shape = Shapes().large,
                                    content = { Text(text = "Да",
                                        color = Color.Black) },
                                )
                            },
                            title = { Text(text = "Подтверждение") },
                            text = { Text(text = "Хотите выйти из аккаунта?") }
                        )
                    }
                })
            },
            floatingActionButton = {
                if (null != state.token) {
                    FloatingActionButton(
                        shape = CircleShape,
                        containerColor = CustomColor().green_color_for_buttons,
                        onClick = { newRequisiteDialogVisible = true },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                            )
                        },
                    )
                }
            },
        )
    }
}

@Preview
@Composable
fun BitkorAppPreview() {
    BitkorTheme {
        BitkorApp(controller = AppController())
    }
}