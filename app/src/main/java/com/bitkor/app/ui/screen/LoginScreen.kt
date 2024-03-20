package com.bitkor.app.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPasteGo
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bitkor.app.state.AppController
import com.bitkor.app.ui.theme.BitKorShapes
import com.bitkor.app.ui.theme.BitkorTheme
import com.bitkor.app.ui.theme.BitkorTypography
import com.bitkor.app.ui.theme.CustomColor
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(controller: AppController) {
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    var tokenTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    Box(
        modifier = Modifier.fillMaxSize(),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.Center),
                content = {
                    Text(
                        style = BitkorTypography.titleLarge,
                        text = "Укажите токен для аккаунта"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = tokenTextFieldValue,
                        shape = BitKorShapes.extraLarge,
                        onValueChange = { value -> tokenTextFieldValue = value },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = CustomColor().green_color_for_buttons,
                        unfocusedContainerColor = CustomColor().green_color_for_tint,
                    disabledContainerColor = CustomColor().green_color_for_tint,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        maxLines = 1,
                        singleLine = true,
                        placeholder = {
                            Text(text = "8u24kjdfgletrefkgjer...")
                        },
                        trailingIcon = {
                            if (tokenTextFieldValue.text.isEmpty()) {
                                Icon(
                                    imageVector = Icons.Filled.ContentPasteGo,
                                    contentDescription = null,
                                    modifier = Modifier.clickable(
                                        role = Role.Image,
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple(bounded = false),
                                        onClick = {
                                            val text = clipboardManager.getText()?.text
                                                ?: tokenTextFieldValue.text
                                            tokenTextFieldValue = TextFieldValue(
                                                text = text,
                                                selection = TextRange(text.length),
                                            )
                                        },
                                    )
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = null,
                                    modifier = Modifier.clickable(
                                        role = Role.Image,
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple(bounded = false),
                                        onClick = {
                                            tokenTextFieldValue = TextFieldValue(text = "")
                                        },
                                    )
                                )
                            }
                        },
                    )
                    Spacer(modifier = Modifier.height(80.dp))
                    fun isValidToken(token: String): Boolean {
                        val regex = "^[A-Za-z0-9\\-_]{3,}$".toRegex()
                        return regex in token
                    }
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            coroutineScope.launch {
                                controller.login(tokenTextFieldValue.text)
                            }
                        },
                        enabled = isValidToken(tokenTextFieldValue.text),
                        content = { Text(text = "Продолжить", color = Color.Black) },
                        shape = BitKorShapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomColor().green_color_for_buttons
                        )
                    )
                },
            )
        },
    )
}

@Preview
@Composable
fun LoginScreenPreview() {
    BitkorTheme {
        LoginScreen(controller = AppController())
    }
}