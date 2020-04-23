package com.nasahapps.urbanary.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class HttpClientImpl : HttpClient {

    private val client = OkHttpClient()

    override suspend fun <T> get(url: String, headers: List<Pair<String, String>>) =
        withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url(url)
                .apply {
                    headers.forEach {
                        addHeader(it.first, it.second)
                    }
                }
                .build()
            val response = client.newCall(request).execute()
            val json = response.body?.string()
            val type = object : TypeToken<T>() {}.type
            Gson().fromJson<T>(json, type)
        }

}