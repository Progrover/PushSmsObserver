package com.bitkor.app.state

import com.bitkor.app.data.model.PaymentRequisites
import com.bitkor.app.data.provider.RequisitesProvider
import com.bitkor.app.data.provider.TokenProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppController {
    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()

    fun launch() {
        _state.value = _state.value.copy(
            token = TokenProvider.token.ifEmpty { null },
            requisites = RequisitesProvider.requisites,
        )
    }

    suspend fun login(newToken: String) {
        TokenProvider.token = newToken
        _state.emit(_state.value.copy(token = newToken))
    }

    suspend fun hideLogoutConfirm() {
        _state.emit(_state.value.copy(logoutConfirmVisible = false))
    }

    suspend fun showLogoutConfirm() {
        _state.emit(_state.value.copy(logoutConfirmVisible = true))
    }

    suspend fun showClearRequisitesConfirm() {
        _state.emit(_state.value.copy(clearRequisitesConfirmVisible = true))
    }

    suspend fun hideClearRequisitesConfirm() {
        _state.emit(_state.value.copy(clearRequisitesConfirmVisible = false))
    }

    suspend fun logout() {
        TokenProvider.token = ""
        _state.emit(_state.value.copy(token = null, logoutConfirmVisible = false))
    }

    suspend fun addRequisite(requisite: PaymentRequisites) {
        RequisitesProvider.putRequisite(requisite)
        _state.emit(
            _state.value.copy(
                requisites = RequisitesProvider.requisites.toList(),
                version = _state.value.version + 1
            )
        )
    }

    suspend fun removeRequisite(requisite: PaymentRequisites) {
        RequisitesProvider.removeRequisite(requisite)
        val requisites = mutableListOf<PaymentRequisites>()
        requisites.addAll(RequisitesProvider.requisites.toMutableList())
        _state.emit(_state.value.copy(requisites = requisites, version = _state.value.version + 1))
    }

    suspend fun clearRequisites() {
        RequisitesProvider.clearRequisites()
        _state.emit(_state.value.copy(
            requisites = emptyList(),
            version = _state.value.version + 1,
            clearRequisitesConfirmVisible = false,
        ))
    }
}