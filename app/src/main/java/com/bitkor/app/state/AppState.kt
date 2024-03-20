package com.bitkor.app.state

import com.bitkor.app.data.model.PaymentRequisites

data class AppState(
    val token: String? = null,
    val requisites: List<PaymentRequisites> = emptyList(),
    val logoutConfirmVisible: Boolean = false,
    val clearRequisitesConfirmVisible: Boolean = false,
    val version: Long = 0,
)
