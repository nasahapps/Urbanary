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
    fun testSearchingDefinitions() = runBlocking {
        val results = api.searchDefinitions("wat")
        Assert.assertTrue("Search results were empty", results.isNotEmpty())
    }

}