package com.bitkor.app.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import com.bitkor.app.R

enum class PaymentMethod(
    @StringRes val nameRes: Int,
    @DrawableRes val iconRes: Int
) {
    CARD(R.string.payment_method_card, R.drawable.ic_method_card),
    PHONE(R.string.payment_method_phone, R.drawable.baseline_local_phone_24),
    SIM(R.string.payment_method_sbp, R.drawable.ic_method_sbp)
}