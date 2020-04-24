package com.nasahapps.urbanary.ui

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nasahapps.urbanary.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels {
        (application as MainApplication).appContainer.mainViewModelFactory
    }
    val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                hideKeyboard()
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView?.addOnScrollListener(onScrollListener)

        searchEditText?.setOnEditorActionListener { v, actionId, event ->
            viewModel.getDefinitions(searchEditText?.text?.toString())
            hideKeyboard()
            true
        }

        viewModel.viewState.observe(this, Observer { state ->
            val views =
                mutableListOf(initialLayout, progressBar, emptyText, errorText, recyclerView)

            val viewToMakeVisible: View? = when (state) {
                MainViewModel.ViewState.INITIAL -> {
                    initialLayout
                }
                MainViewModel.ViewState.LOADING -> {
                    progressBar
                }
                MainViewModel.ViewState.LIST -> {
                    recyclerView?.adapter = DefinitionAdapter(viewModel.definitions)
                    recyclerView
                }
                MainViewModel.ViewState.EMPTY -> {
                    emptyText?.text = "Sorry, we couldn't find: ${viewModel.searchQuery}"
                    emptyText
                }
                MainViewModel.ViewState.ERROR -> {
                    errorText
                }
                else -> {
                    null
                }
            }

            views.remove(viewToMakeVisible)
            views.forEach { it.isVisible = false }
            viewToMakeVisible?.isVisible = true
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView?.removeOnScrollListener(onScrollListener)
    }

    fun hideKeyboard() {
        findViewById<View>(Window.ID_ANDROID_CONTENT)?.let { view ->
            val imm = getSystemService<InputMethodManager>()
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
