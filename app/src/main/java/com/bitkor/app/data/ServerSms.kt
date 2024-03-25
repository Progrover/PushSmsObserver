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
    val sendMessageDateTime: Instant,
    @SerializedName("message_date_time")
    val messageDateTime: Instant ? = null,
    @SerializedName("external_id")
    val externalId: UUID,
    @SerializedName("device_id")
    val deviceId: String? = null,
    @SerializedName("app_version_id")
    val appVersionId: String? = null,
    @SerializedName("device_requisites")
    val requisites: Map<PaymentMethod, List<String>>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServerSms

        if (type != other.type) return false
        if (sender != other.sender) return false
        if (text != other.text) return false
        if (sendMessageDateTime != other.sendMessageDateTime) return false
        if (messageDateTime != other.messageDateTime) return false
        if (externalId != other.externalId) return false
        if (deviceId != other.deviceId) return false
        if (appVersionId != other.appVersionId) return false
        if (requisites != other.requisites) return false

        return true
    }
}
