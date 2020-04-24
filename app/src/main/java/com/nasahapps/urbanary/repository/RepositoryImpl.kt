package com.nasahapps.urbanary.repository

import com.nasahapps.urbanary.model.Definition
import com.nasahapps.urbanary.network.UrbanDictionaryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImpl(private val urbanDictionaryApi: UrbanDictionaryApi) : Repository {

    override suspend fun searchDefinitions(query: String?): List<Definition> =
        withContext(Dispatchers.IO) {
            urbanDictionaryApi.searchDefinitions(query)
        }

}