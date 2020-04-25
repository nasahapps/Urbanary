package com.nasahapps.urbanary.local

import com.nasahapps.urbanary.model.Definition
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LocalDataSourceTest {

    lateinit var localDataSource: LocalDataSource

    @Before
    fun setup() {
        localDataSource = LocalDataSource()
    }

    @Test
    fun searchingForSavedDefinitions__shouldReturnResults() = runBlocking {
        val query = "query"
        val definitions = listOf(Definition(word = "word", definition = "definition"))
        localDataSource.saveSearchDefinitions(query, definitions)

        val cachedDefinitions = localDataSource.searchDefinitions(query)
        Assert.assertEquals(definitions, cachedDefinitions)
    }

    @Test
    fun searchingForUnsavedDefinitions__shouldReturnNothing() = runBlocking {
        val cachedDefinitions = localDataSource.searchDefinitions("query")
        Assert.assertNull(cachedDefinitions)
    }

}