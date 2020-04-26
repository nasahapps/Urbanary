package com.nasahapps.urbanary.ui

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class TestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?,
                                className: String?,
                                context: Context?): Application {
        println("Test application: $className")
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }
}