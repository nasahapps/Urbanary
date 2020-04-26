package com.nasahapps.urbanary.ui

import com.nasahapps.urbanary.mock.MockRepository
import com.nasahapps.urbanary.repository.Repository

class TestAppContainer : AppContainer() {

    override val repository: Repository = MockRepository()

}