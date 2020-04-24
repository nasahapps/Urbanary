package com.nasahapps.urbanary.network

interface HttpClient {
    suspend fun <T> get(
        url: String,
        headers: List<Pair<String, String>> = emptyList(),
        responseClass: Class<T>
    ): T
}