package com.bitkor.app.data.provider

object TokenProvider {
    var token: String = SharedPreferencesProvider.getString("token", "")
        set(value) {
            SharedPreferencesProvider.setString("token", value)
            field = value
        }

    var deviceId: String = SharedPreferencesProvider.getString("deviceId", "")
        set(value) {
            SharedPreferencesProvider.setString("deviceId", value)
            field = value
        }

    var parsingStatus: Boolean = true
        get() = SharedPreferencesProvider.getBoolean("parsingStatus")
        set(value) {
            SharedPreferencesProvider.setBoolean("parsingStatus", value)
            field = value
        }

}