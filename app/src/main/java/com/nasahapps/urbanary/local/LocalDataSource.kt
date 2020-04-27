package com.nasahapps.urbanary.local

import com.nasahapps.urbanary.model.Definition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource {

    private val searchMap = mutableMapOf<String, List<Definition>>()

    suspend fun searchDefinitions(query: String?): List<Definition>? = withContext(Dispatchers.IO) {
        if (!query.isNullOrBlank()) {
            searchMap[query]
        } else {
            null
        }
    }

    suspend fun saveSearchDefinitions(query: String?,
                                      results: List<Definition>) = withContext(Dispatchers.IO) {
        if (!query.isNullOrBlank()) {
            searchMap[query] = results
        }
    }

}