package com.nasahapps.urbanary.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.nasahapps.urbanary.mock.MockRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    lateinit var viewModel: MainViewModel
    val mockRepository = MockRepository()

    @Before
    fun setup() {
        viewModel = MainViewModel(mockRepository, SavedStateHandle())
    }

    @Test
    fun searchingForDefinitionsWithValidQuery__shouldReturnResults() {
        viewModel.getDefinitions("something")
        Assert.assertEquals("ViewState was incorrect", MainViewModel.ViewState.LIST,
                viewModel.viewState.value)
    }

    @Test
    fun searchingForNothing__shouldDoNothing() {
        viewModel.getDefinitions(null)
        Assert.assertEquals("ViewState was incorrect", MainViewModel.ViewState.INITIAL,
                viewModel.viewState.value)
    }

    @Test
    fun searchingForDefinitionsWithInvalidQuery__shouldReturnNoResults() {
        viewModel.getDefinitions("empty")
        Assert.assertEquals("ViewState was incorrect", MainViewModel.ViewState.EMPTY,
                viewModel.viewState.value)
    }

    @Test
    fun errorDuringSearch__shouldHaveErrorViewState() {
        viewModel.getDefinitions("error")
        Assert.assertEquals("ViewState was incorrect", MainViewModel.ViewState.ERROR,
                viewModel.viewState.value)
    }

}