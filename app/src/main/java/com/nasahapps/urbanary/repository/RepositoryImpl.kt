package com.nasahapps.urbanary.repository

import com.nasahapps.urbanary.model.Definition
import com.nasahapps.urbanary.network.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImpl(private val remoteDataSource: RemoteDataSource) : Repository {

    override suspend fun searchDefinitions(query: String?): List<Definition> =
        withContext(Dispatchers.IO) {
            remoteDataSource.searchDefinitions(query)
        }

}