package com.nasahapps.urbanary.ui

import com.nasahapps.urbanary.network.HttpClient
import com.nasahapps.urbanary.network.HttpClientImpl
import com.nasahapps.urbanary.network.RemoteDataSource
import com.nasahapps.urbanary.repository.Repository
import com.nasahapps.urbanary.repository.RepositoryImpl

open class AppContainer {

    protected open val httpClient: HttpClient = HttpClientImpl()
    protected open val remoteDataSource: RemoteDataSource = RemoteDataSource(httpClient)
    open val repository: Repository = RepositoryImpl(remoteDataSource)

//    open val mainViewModelFactory = MainViewModelFactory(repository)

}