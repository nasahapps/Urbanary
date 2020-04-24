package com.nasahapps.urbanary.ui

import android.app.Application

open class MainApplication : Application() {

    open val appContainer: AppContainer by lazy { AppContainer() }

}