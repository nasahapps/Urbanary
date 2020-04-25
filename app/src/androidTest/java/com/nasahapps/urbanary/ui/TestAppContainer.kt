package com.nasahapps.urbanary.ui

import com.nasahapps.urbanary.model.Definition
import com.nasahapps.urbanary.repository.Repository

class TestAppContainer : AppContainer() {
    override val repository = object : Repository {
        override suspend fun searchDefinitions(query: String?): List<Definition> {
            return emptyList()
        }
    }
}