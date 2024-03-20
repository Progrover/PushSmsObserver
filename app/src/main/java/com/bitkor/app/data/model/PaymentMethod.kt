package com.bitkor.app.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.bitkor.app.R

enum class PaymentMethod(
    @StringRes val nameRes: Int,
    @DrawableRes val iconRes: Int
) {
    CARD(R.string.payment_method_card, R.drawable.ic_method_card),
    PHONE(R.string.payment_method_phone, R.drawable.ic_method_card),
    SIM(R.string.payment_method_sbp, R.drawable.ic_method_sbp)
}