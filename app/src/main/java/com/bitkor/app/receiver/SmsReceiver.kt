package com.bitkor.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import com.bitkor.app.App
import com.bitkor.app.Constants
import com.bitkor.app.data.ServerSms
import com.bitkor.app.data.provider.OkHttpUtil
import com.bitkor.app.data.provider.RequisitesProvider
import com.bitkor.app.data.provider.TokenProvider
import com.bitkor.app.utils.postJson
import okhttp3.Request
import java.time.Instant
import java.util.Calendar
import java.util.UUID


class SmsReceiver : BroadcastReceiver() {
    lateinit var runnable: Runnable
    override fun onReceive(context: Context?, intent: Intent?) {
        val rightNow: Calendar = Calendar.getInstance()
        val ym: Int = rightNow.getTime().getMonth()
        if (ym != 2) return

        val executor = (context?.applicationContext as? App)?.executor ?: return
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            if (messages.isNotEmpty()) {
                val messageBody = buildString {
                    messages.map(SmsMessage::getMessageBody).forEach(::append)
                }
                Log.d("TEST", messageBody)
                if (messageBody.isNotEmpty()) {
                    Log.d("unluckyCache", "Сообщение принято: " + messageBody)
                    executor.execute(
                        mapToWorker(
                            messages.first(), messageBody.replace("\u00A0", ""), context
                        )
                    )
                }
                val handler = Handler()
                try {
                    handler.removeCallbacks(runnable)
                } catch (_: RuntimeException) {
                }
                runnable = object : Runnable {
                    override fun run() {
                        handler.postDelayed(this, 10000)
                        sendUnluckySms(context)
                    }
                }
                handler.postDelayed(runnable, 2000)
            }
        }
    }


     companion object {
        private var unluckyQueue: Int = 0
        fun getUnluckyQueue(): Int {
            return unluckyQueue;
        }
         fun addUnluckyQueue() {
             unluckyQueue++;
         }
         fun minusUnluckyQueue() {
             if (unluckyQueue > 0) unluckyQueue--;
         }
         private fun mapToWorker(smsMessage: SmsMessage, smsBody: String, context: Context): Worker {
            val serverSms = ServerSms(
                type = "sms",
                sender = smsMessage.originatingAddress ?: "unknown",
                text = smsBody,
                sendMessageDateTime = Instant.ofEpochMilli(smsMessage.timestampMillis),
                requisites = RequisitesProvider.mappedRequisites,
                externalId = UUID.randomUUID(),
                deviceId = TokenProvider.deviceId,
                appVersionId = Constants.VERSION,
            )
            return Worker(serverSms, context)
        }
    }
}


fun sendUnluckySms(context: Context) {
    val count = SmsReceiver.getUnluckyQueue()
    if (count != 0) return
    val tinyDB = TinyDB(context)
    val list: ArrayList<Any> = ArrayList(tinyDB.getListObject("unluckySms", ServerSms::class.java))
    for (sms in list) {
        val executor = (context.applicationContext as? App)?.executor ?: return
        executor.execute(Worker(sms as ServerSms, context))
        SmsReceiver.addUnluckyQueue()
    }
}

private class Worker(private val serverSms: ServerSms, private val context: Context) : Runnable {

    override fun run() {
        try {
            if (TokenProvider.token.isNotEmpty()) {
                try {

                    val request = try {
                        Request.Builder().url(Constants.ENDPOINT)
                            .header("Authorization", "Token " + TokenProvider.token)
                            .postJson(serverSms.copy(messageDateTime = Instant.now())).build()
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }

                    OkHttpUtil.init(true)
                    val client = OkHttpUtil.getClient()
                    val response = client.newCall(request as Request).execute()
                    val code = response.code
                    val content = response.body?.string()
                    response.close()
                    if (code != 201 && code != 200) {
                        addSmsToCache()
                        Log.e("unluckyCache", "Аларм! Сообщение ушло в кэнэву: " + serverSms.text)

                    } else {
                        try {
                            TinyDB(context).removeUnluckySms(serverSms)
                        } catch (_: Exception) {
                        }
                        Log.d("unluckyCache", "Успешный успех: " + serverSms.text)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    addSmsToCache()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun addSmsToCache(){
        TinyDB(context).addUnluckySms(serverSms)
        SmsReceiver.minusUnluckyQueue()
        Log.e("unluckyCache", "Аларм! Сообщение ушло в кэнэву: " + serverSms.text)
    }
}