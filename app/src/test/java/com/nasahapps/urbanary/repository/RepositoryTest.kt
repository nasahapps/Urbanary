package com.nasahapps.urbanary.repository

import com.nasahapps.urbanary.local.LocalDataSource
import com.nasahapps.urbanary.model.Definition
import com.nasahapps.urbanary.network.RemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    lateinit var localDataSource: LocalDataSource
    lateinit var remoteDataSource: RemoteDataSource
    lateinit var repository: Repository

    @Before
    fun setup() {
        localDataSource = LocalDataSource()
        remoteDataSource = mockk()
        repository = RepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun aRepeatedSearchWithRemoteFailure__shouldReturnCachedResultsRegardless() = runBlocking {
        val query = "query"
        val expectedResult = listOf(Definition(word = "word", definition = "definition"))

        // Upon making the first search query, it should return valid results
        coEvery { remoteDataSource.searchDefinitions(query) } returns expectedResult
        var actualResult = repository.searchDefinitions(query)
        Assert.assertEquals(expectedResult, actualResult)

        // Then upon making a repeated second call and forcing a remote error,
        // it should still return valid results from the cached LocalDataSource
        coEvery { remoteDataSource.searchDefinitions(query) } throws RuntimeException()
        actualResult = repository.searchDefinitions(query)
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun aNewSearchWithRemoteFailure__shouldFail() = runBlocking {
        val query = "query"
        // If the remote call fails and there's no cached data, the entire call should fail
        coEvery { remoteDataSource.searchDefinitions(query) } throws RuntimeException()
        var failure = false
        try {
            repository.searchDefinitions(query)
        } catch (e: Throwable) {
            failure = true
        }
        Assert.assertTrue("Search query should've failed", failure)
    }

}