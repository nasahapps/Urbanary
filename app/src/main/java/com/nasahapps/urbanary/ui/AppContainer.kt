package com.nasahapps.urbanary.ui

import com.nasahapps.urbanary.network.HttpClient
import com.nasahapps.urbanary.network.HttpClientImpl
import com.nasahapps.urbanary.network.UrbanDictionaryApi
import com.nasahapps.urbanary.repository.Repository
import com.nasahapps.urbanary.repository.RepositoryImpl

object AppContainer {

    private val httpClient: HttpClient = HttpClientImpl()
    private val urbanDictionaryApi: UrbanDictionaryApi = UrbanDictionaryApi(httpClient)
    private val repository: Repository = RepositoryImpl(urbanDictionaryApi)

    val mainViewModelFactory = MainViewModelFactory(repository)

}