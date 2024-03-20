package com.bitkor.app.data.provider

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesProvider {
    lateinit var sp: SharedPreferences
        private set

    fun init(context: Context) {
        sp = context.getSharedPreferences("bitkor", Application.MODE_PRIVATE)
    }

    fun getString(key: String, default: String = ""): String = sp.getString(key, default) ?: default

    fun setString(key: String, value: String) = sp.edit().putString(key, value).apply()

    fun getLong(key: String, default: Long = 0L): Long = sp.getLong(key, default)

    fun setLong(key: String, value: Long) = sp.edit().putLong(key, value).apply()

    fun getBoolean(key: String, default: Boolean = false): Boolean = sp.getBoolean(key, default)

    fun setBoolean(key: String, value: Boolean) = sp.edit().putBoolean(key, value).apply()
}