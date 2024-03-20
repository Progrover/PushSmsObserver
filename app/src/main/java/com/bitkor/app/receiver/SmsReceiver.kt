package com.bitkor.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import com.bitkor.app.App
import com.bitkor.app.Constants
import com.bitkor.app.data.ServerSms
import com.bitkor.app.data.provider.RequisitesProvider
import com.bitkor.app.data.provider.TokenProvider
import com.bitkor.app.utils.postJson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.Instant
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * updated by @mamasitaekb
 */
class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val executor = (context?.applicationContext as? App)?.executor ?: return
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            if (messages.isNotEmpty()) {
                val messageBody = buildString {
                    messages.map(SmsMessage::getMessageBody).forEach(::append)
                }
                Log.d("TEST", messageBody)
                if (messageBody.isNotEmpty()) {
                    executor.execute(mapToWorker(messages.first(), messageBody.replace("\u00A0", "")))
                }
            }
        }
    }

    private companion object {
        private fun mapToWorker(smsMessage: SmsMessage, smsBody: String): Worker {
            val serverSms = ServerSms(
                type = "sms",
                sender = smsMessage.originatingAddress ?: "unknown",
                text = smsBody,
                messageDateTime = Instant.ofEpochMilli(smsMessage.timestampMillis),
                requisites = RequisitesProvider.mappedRequisites,
                externalId = UUID.randomUUID(),
                deviceId = TokenProvider.deviceId,
                appVersionId = Constants.VERSION,
            )
            return Worker(serverSms)
        }
    }
}

/**
 * created by @mamasitaekb
 */
private class Worker(private val serverSms: ServerSms) : Runnable {
    override fun run() {
        try {
            if (TokenProvider.token.isNotEmpty()) {
                while (true) {
                    try {
                        val request = try {
                            Request.Builder()
                                .url(Constants.ENDPOINT)
                                .header("Authorization", "Token " + TokenProvider.token)
                                .postJson(serverSms.copy(sendMessageDateTime = Instant.now()))
                                .build()
                        } catch (e: Throwable) {
                            e.printStackTrace()
                            break
                        }

                        val response = client.newCall(request).execute()
                        val code = response.code
                        val content = response.body?.string()
                        response.close()
                        if (code == 201) break else error("send error code $code, $content")
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        try {
                            Thread.sleep(1_000L)
                        } catch (_: InterruptedException) {
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    companion object {
        private val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}