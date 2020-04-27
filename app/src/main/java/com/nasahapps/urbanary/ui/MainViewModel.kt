package com.nasahapps.urbanary.ui

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.nasahapps.urbanary.model.Definition
import com.nasahapps.urbanary.repository.Repository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModelFactory(
        private val repository: Repository,
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(key: String,
                                         modelClass: Class<T>,
                                         handle: SavedStateHandle): T {
        return MainViewModel(repository, handle) as T
    }

}

class MainViewModel(private val repository: Repository,
                    private val state: SavedStateHandle) : ViewModel() {

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

    companion object {
        private val STATE_SEARCH_QUERY = "query"
        private val STATE_SORT = "sort"
        private val STATE_VIEW_STATE = "viewState"
        private val STATE_DEFINITIONS = "definitions"
    }

    val viewState = state.getLiveData(STATE_VIEW_STATE, ViewState.INITIAL)

    var sort: Sort = Sort.DEFAULT
        get() = state[STATE_SORT] ?: Sort.DEFAULT
        set(value) {
            if (field != value) {
                state[STATE_SORT] = value
                definitions = sortDefinitions()
                // Toggle a refresh of the view
                viewState.value = viewState.value
            }
        }
    var definitions = listOf<Definition>()
    // The original, unsorted results returned from the API. Kept as a reference if the user
    // wants to sort by DEFAULT
    private var originalDefinitions: List<Definition>
        get() = state[STATE_DEFINITIONS] ?: listOf()
        set(value) {
            state[STATE_DEFINITIONS] = value
            definitions = sortDefinitions()
        }
    var searchQuery: String?
        get() = state[STATE_SEARCH_QUERY]
        set(value) {
            state[STATE_SEARCH_QUERY] = value
        }
    private var job: Job? = null

    init {
        definitions = sortDefinitions()
    }

    fun getDefinitions(query: String?) {
        if (!query.isNullOrBlank()) {
            // Cancel the current search if one was already ongoing
            job?.cancel()

            searchQuery = query
            viewState.value = ViewState.LOADING
            job = viewModelScope.launch {
                try {
                    originalDefinitions = repository.searchDefinitions(query)
                    if (isActive) {
                        if (definitions.isNotEmpty()) {
                            viewState.value = ViewState.LIST
                        } else {
                            viewState.value = ViewState.EMPTY
                        }
                    }
                } catch (e: Throwable) {
                    if (e !is CancellationException && isActive) {
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