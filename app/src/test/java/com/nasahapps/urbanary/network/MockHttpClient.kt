package com.nasahapps.urbanary.network

import com.google.gson.Gson
import com.nasahapps.urbanary.model.MOCK_SEARCH_RESPONSE
import com.nasahapps.urbanary.model.SearchResponse

class MockHttpClient : HttpClient {

    override suspend fun <T> get(url: String, headers: List<Pair<String, String>>): T {
        if (url.startsWith("https://mashape-community-urban-dictionary.p.rapidapi.com/define")) {
            return Gson().fromJson(MOCK_SEARCH_RESPONSE, SearchResponse::class.java) as T
        } else {
            throw RuntimeException("Unsupported url")
        }
    }

}