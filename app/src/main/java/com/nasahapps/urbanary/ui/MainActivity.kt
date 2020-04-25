package com.nasahapps.urbanary.ui

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nasahapps.urbanary.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels {
        val repository = (application as MainApplication).appContainer.repository
        MainViewModelFactory(repository, this)
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
        setRecyclerViewAdapter()

        searchTextField?.editText?.setOnEditorActionListener { v, actionId, event ->
            viewModel.getDefinitions(searchTextField?.editText?.text?.toString())
            hideKeyboard()
            searchTextField?.clearFocus()
            true
        }
        searchTextField?.setEndIconOnClickListener {
            showSortChangeDialog()
        }

        appBar?.doOnLayout {
            // Add additional top padding to the RecyclerView so content can scroll under the search bar
            recyclerView?.updatePadding(top = it.height + it.marginTop)
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
                    setRecyclerViewAdapter()
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

            searchTextField?.isEndIconVisible = state == MainViewModel.ViewState.LIST
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

    fun showSortChangeDialog() {
        val sortChoices = MainViewModel.Sort
            .values()
            .map { it.readableName }
            .toTypedArray()
        val currentChoiceIndex = MainViewModel.Sort
            .values()
            .indexOf(viewModel.sort)

        var choice = currentChoiceIndex
        MaterialAlertDialogBuilder(this)
            .setTitle("Sort by...")
            .setSingleChoiceItems(sortChoices, currentChoiceIndex) { dialog, which ->
                choice = which
            }
            .setPositiveButton("Ok") { _, _ ->
                viewModel.sort = MainViewModel.Sort.values()[choice]
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun setRecyclerViewAdapter() {
        recyclerView?.adapter = DefinitionAdapter(viewModel.definitions)
    }

}
