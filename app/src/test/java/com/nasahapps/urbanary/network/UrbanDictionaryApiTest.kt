package com.nasahapps.urbanary.network

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UrbanDictionaryApiTest {

    lateinit var api: UrbanDictionaryApi

    @Before
    fun setup() {
        val client = MockHttpClient()
        api = UrbanDictionaryApi(client)
    }

    @Test
    fun searchingWithQuery__shouldReturnResults() = runBlocking {
        val results = api.searchDefinitions("wat")
        Assert.assertTrue("Search results were empty", results.isNotEmpty())
    }

    @Test
    fun searchingWithNoQuery__shouldFail() = runBlocking {
        var failure = false
        try {
            api.searchDefinitions(null)
        } catch (e: Throwable) {
            failure = true
        }
        Assert.assertTrue("Search query should've failed", failure)
    }

}