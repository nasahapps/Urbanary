package com.nasahapps.urbanary.repository

import com.nasahapps.urbanary.model.Definition
import com.nasahapps.urbanary.network.UrbanDictionaryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val urbanDictionaryApi: UrbanDictionaryApi) {

    suspend fun searchDefinitions(query: String?): List<Definition> = withContext(Dispatchers.IO) {
        urbanDictionaryApi.searchDefinitions(query)
    }

}