package com.bitkor.app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bitkor.app.R
import com.bitkor.app.data.model.PaymentMethod
import com.bitkor.app.state.AppController
import com.bitkor.app.ui.theme.BitKorShapes
import com.bitkor.app.ui.theme.BitkorTheme
import com.bitkor.app.ui.theme.BitkorTypography
import com.bitkor.app.ui.theme.CustomColor
import kotlinx.coroutines.launch

@Composable
fun MainScreen(controller: AppController) {
    val state by controller.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Text(
                        modifier = Modifier.weight(1f),
                        style = BitkorTypography.titleLarge,
                        text = "Добавленные реквизиты",
                        color = Color.Black
                    )
                    if (state.requisites.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.RemoveCircleOutline,
                            contentDescription = "Clear all",
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(bounded = false),
                                    onClick = {
                                        coroutineScope.launch {
                                            controller.showClearRequisitesConfirm()
                                        }
                                    },
                                )
                                .padding(20.dp, 0.dp)
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn(
                modifier = Modifier,
                content = {
                    items(items = state.requisites) { requisite ->
                        Spacer(modifier = Modifier.height(7.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(BitKorShapes.extraLarge)
                                .background(color = CustomColor().green_color_for_tint)
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            content = {
                                when (requisite.method) {
                                    PaymentMethod.SIM -> {
                                        Image(
                                            modifier = Modifier.size(36.dp),
                                            painter = painterResource(id = R.drawable.ic_method_sbp),
                                            contentDescription = null,
                                        )
                                    }

                                    PaymentMethod.CARD -> {
                                        Image(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .padding(6.dp),
                                            colorFilter = ColorFilter.tint(Color.Black),
                                            painter = painterResource(id = R.drawable.ic_method_card),
                                            contentDescription = null,
                                        )
                                    }
// Здесь исправить painter
                                    PaymentMethod.PHONE -> {
                                        Image(
                                            modifier = Modifier.size(36.dp),
                                            colorFilter = ColorFilter.tint(Color.Black),
                                            painter = painterResource(id = R.drawable.baseline_local_phone_24),
                                            contentDescription = null,
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = BitkorTypography.titleMedium,
                                    text = requisite.number,
                                )
                                Spacer(modifier = Modifier.width(30.dp))
                                Icon(
                                    imageVector = Icons.Outlined.Clear,
                                    contentDescription = "Remove",
                                    modifier = Modifier
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = rememberRipple(bounded = false),
                                            onClick = {
                                                coroutineScope.launch {
                                                    controller.removeRequisite(requisite)
                                                }
                                            },
                                        ),
                                )
                            }
                        )
                    }

                    if (state.requisites.isEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(BitKorShapes.extraLarge)
                                    .background(color = CustomColor().green_color_for_tint)
                                    .padding(horizontal = 30.dp, vertical = 12.dp),
                                content = {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        style = BitkorTypography.bodyMedium,
                                        color = Color.Black,
                                        text = "Для начала работы необходимо добавить реквизиты!",
                                    )
                                }
                            )
                        }
                    }
                },
            )
            if (state.clearRequisitesConfirmVisible) {
                AlertDialog(
                    onDismissRequest = {
                        coroutineScope.launch {
                            controller.hideClearRequisitesConfirm()
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    controller.hideClearRequisitesConfirm()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CustomColor().green_color_for_buttons
                            ),
                            shape = Shapes().medium,
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
                                    controller.clearRequisites()
                                }
                            },
                            shape = Shapes().medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CustomColor().green_color_for_buttons
                            ),
                            content = { Text(text = "Да",
                                color = Color.Black)},
                        )
                    },
                    title = { Text(text = "Подтверждение") },
                    text = { Text(text = "Хотите удалить все реквизиты?")}
                )
            }
        },
    )
}
@Preview
@Composable
fun MainScreenPreview() {
    BitkorTheme {
        MainScreen(controller = AppController())
    }
}
