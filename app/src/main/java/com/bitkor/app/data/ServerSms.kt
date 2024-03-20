package com.bitkor.app.data

import com.bitkor.app.data.model.PaymentMethod
import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.util.UUID

data class ServerSms(
    // "sms", "push" or "email"
    val type: String,
    val sender: String,
    val text: String,
    @SerializedName("send_message_date_time")
    val sendMessageDateTime: Instant? = null,
    @SerializedName("message_date_time")
    val messageDateTime: Instant,
    @SerializedName("external_id")
    val externalId: UUID,
    @SerializedName("device_id")
    val deviceId: String? = null,
    @SerializedName("app_version_id")
    val appVersionId: String? = null,
    @SerializedName("device_requisites")
    val requisites: Map<PaymentMethod, List<String>>,
)