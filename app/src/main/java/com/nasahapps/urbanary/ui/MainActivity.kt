package com.nasahapps.urbanary.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.nasahapps.urbanary.R

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels {
        (application as MainApplication).appContainer.mainViewModelFactory
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
