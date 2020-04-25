package com.nasahapps.urbanary.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nasahapps.urbanary.model.Definition
import com.nasahapps.urbanary.repository.Repository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class MainViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}

class MainViewModel(private val repository: Repository) : ViewModel() {

    enum class ViewState {
        INITIAL,
        LOADING,
        LIST,
        EMPTY,
        ERROR
    }

    enum class Sort(val readableName: String) {
        DEFAULT("Default"),
        THUMBS_UP("Most Thumbs Up"),
        THUMBS_DOWN("Most Thumbs Down")
    }

    val viewState = MutableLiveData(ViewState.INITIAL)

    var sort = Sort.DEFAULT
        set(value) {
            if (field != value) {
                field = value
                definitions = sortDefinitions()
                // Toggle a refresh of the view
                viewState.value = viewState.value
            }
        }
    var definitions = listOf<Definition>()
    private var originalDefinitions = listOf<Definition>()
    var searchQuery: String? = null

    fun getDefinitions(query: String?) {
        if (!query.isNullOrBlank()) {
            searchQuery = query
            viewState.value = ViewState.LOADING
            viewModelScope.launch {
                try {
                    originalDefinitions = repository.searchDefinitions(query)
                    definitions = sortDefinitions()
                    if (definitions.isNotEmpty()) {
                        viewState.value = ViewState.LIST
                    } else {
                        viewState.value = ViewState.EMPTY
                    }
                } catch (e: Throwable) {
                    if (e !is CancellationException) {
                        Log.e("Urbanary", "Error getting definitions for $searchQuery", e)
                        viewState.value = ViewState.ERROR
                    }
                }
            }
        }
    }

    private fun sortDefinitions(): List<Definition> {
        return when (sort) {
            Sort.THUMBS_UP -> originalDefinitions.sortedByDescending { it.thumbsUp }
            Sort.THUMBS_DOWN -> originalDefinitions.sortedByDescending { it.thumbsDown }
            Sort.DEFAULT -> originalDefinitions
        }
    }

}