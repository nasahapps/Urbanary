package com.nasahapps.urbanary.ui

import android.app.Application

class MainApplication : Application() {

    val appContainer: AppContainer by lazy { AppContainer }

}