package com.nasahapps.urbanary.repository

import com.nasahapps.urbanary.model.Definition

interface Repository {
    suspend fun searchDefinitions(query: String?): List<Definition>
}