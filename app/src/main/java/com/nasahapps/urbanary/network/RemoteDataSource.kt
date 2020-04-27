package com.nasahapps.urbanary.network

import com.nasahapps.urbanary.model.Definition
import com.nasahapps.urbanary.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataSource(private val httpClient: HttpClient) {

    private val apiHost = "mashape-community-urban-dictionary.p.rapidapi.com"
    private val baseUrl = "https://$apiHost/define"
    private val apiKey = "b314e1eff2msh2bf82b59ac63fc3p1b6ccejsn3e8de8eec9c4"

    suspend fun searchDefinitions(query: String?): List<Definition> = withContext(Dispatchers.IO) {
        val searchQuery = if (!query.isNullOrBlank()) query else ""
        val url = "$baseUrl?term=$searchQuery"
        val headers = listOf(
            Pair("x-rapidapi-host", apiHost),
            Pair("x-rapidapi-key", apiKey)
        )
        val response = httpClient.get(url, headers, SearchResponse::class.java)
        response.list
    }

}