package com.bitkor.app.utils

import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

@Suppress("HasPlatformType")
val GSON = Converters.registerInstant(GsonBuilder()).create()

fun Request.Builder.postJson(obj: Any) =
    post(GSON.toJson(obj).toRequestBody("application/json; charset=utf-8".toMediaType()))

inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)
