package com.nasahapps.urbanary.ui

class TestApplication : MainApplication() {

    override val appContainer: AppContainer by lazy { TestAppContainer() }

}