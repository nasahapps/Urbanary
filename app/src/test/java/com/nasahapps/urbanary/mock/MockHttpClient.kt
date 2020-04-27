package com.nasahapps.urbanary.mock

import com.google.gson.Gson
import com.nasahapps.urbanary.network.HttpClient

class MockHttpClient : HttpClient {

    override suspend fun <T> get(url: String,
                                 headers: List<Pair<String, String>>,
                                 responseClass: Class<T>): T {
        if (url.startsWith("https://mashape-community-urban-dictionary.p.rapidapi.com/define")) {
            val queryParams = url.substring(url.indexOf("?") + 1)
                .split("&")
                .associate {
                    val pair = it.split("=")
                    Pair(pair[0], pair[1])
                }
            if (queryParams["term"].isNullOrBlank()) {
                throw RuntimeException("search term was null")
            }
            return Gson().fromJson(MOCK_SEARCH_RESPONSE, responseClass)
        } else {
            throw RuntimeException("Unsupported url")
        }
    }

}