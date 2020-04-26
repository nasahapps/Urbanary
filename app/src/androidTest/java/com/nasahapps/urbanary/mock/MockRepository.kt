package com.nasahapps.urbanary.mock

import com.google.gson.Gson
import com.nasahapps.urbanary.model.Definition
import com.nasahapps.urbanary.model.SearchResponse
import com.nasahapps.urbanary.repository.Repository

class MockRepository : Repository {

    override suspend fun searchDefinitions(query: String?): List<Definition> {
        val response = Gson().fromJson(MOCK_SEARCH_RESPONSE, SearchResponse::class.java)
        return response.list
    }

}