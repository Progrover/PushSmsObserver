package com.bitkor.app.data.provider

import com.bitkor.app.data.model.PaymentMethod
import com.bitkor.app.data.model.PaymentRequisites
import com.bitkor.app.utils.fromJson
import com.google.gson.Gson
import kotlinx.coroutines.sync.Mutex

object RequisitesProvider {
    private val gson = Gson()

    private val _requisites: MutableList<PaymentRequisites> = SharedPreferencesProvider
            .getString("requisites", "[]")
            .let(gson::fromJson)

    val requisites get() = _requisites

    val mappedRequisites: Map<PaymentMethod, List<String>>
        get() = _requisites.groupBy { it.method }.toList()
            .associate { it.first to it.second.map(PaymentRequisites::number) }

    fun putRequisite(paymentRequisites: PaymentRequisites) {
        _requisites.add(paymentRequisites)
        saveRequisites()
    }

    fun removeRequisite(paymentRequisites: PaymentRequisites) {
        _requisites.remove(paymentRequisites)
        saveRequisites()
    }

    fun clearRequisites() {
        _requisites.clear()
        saveRequisites()
    }

    private fun saveRequisites() {
        SharedPreferencesProvider.setString("requisites", gson.toJson(_requisites))
    }
}