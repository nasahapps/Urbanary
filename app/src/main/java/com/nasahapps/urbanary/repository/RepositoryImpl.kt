package com.nasahapps.urbanary.repository

import com.nasahapps.urbanary.local.LocalDataSource
import com.nasahapps.urbanary.model.Definition
import com.nasahapps.urbanary.network.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImpl(private val remoteDataSource: RemoteDataSource,
                     private val localDataSource: LocalDataSource) : Repository {

    override suspend fun searchDefinitions(query: String?): List<Definition> =
        withContext(Dispatchers.IO) {
            var results = localDataSource.searchDefinitions(query)
            if (results == null) {
                results = remoteDataSource.searchDefinitions(query)
                localDataSource.saveSearchDefinitions(query, results.orEmpty())
            }

            results.orEmpty()
        }

}