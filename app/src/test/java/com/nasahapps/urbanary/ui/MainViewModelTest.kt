package com.nasahapps.urbanary.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nasahapps.urbanary.model.Definition
import com.nasahapps.urbanary.repository.Repository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Config.OLDEST_SDK])
class MainViewModelTest {

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    lateinit var viewModel: MainViewModel
    val mockRepository = object : Repository {
        override suspend fun searchDefinitions(query: String?): List<Definition> {
            return when (query) {
                "empty" -> emptyList()
                "error" -> throw RuntimeException()
                else -> listOf(Definition())
            }
        }
    }

    @Before
    fun setup() {
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun searchingForDefinitionsWithValidQuery__shouldReturnResults() {
        viewModel.getDefinitions("something")
        Assert.assertEquals(
            "ViewState was not LIST", MainViewModel.ViewState.LIST,
            viewModel.viewState.value
        )
    }

    @Test
    fun searchingForNothing__shouldDoNothing() {
        viewModel.getDefinitions(null)
        Assert.assertEquals(
            "ViewState was not INITIAL", MainViewModel.ViewState.INITIAL,
            viewModel.viewState.value
        )
    }

    @Test
    fun searchingForDefinitionsWithInvalidQuery__shouldReturnNoResults() {
        viewModel.getDefinitions("empty")
        Assert.assertEquals(
            "ViewState was not EMPTY", MainViewModel.ViewState.EMPTY,
            viewModel.viewState.value
        )
    }

    @Test
    fun errorDuringSearch__shouldHaveErrorViewState() {
        viewModel.getDefinitions("error")
        Assert.assertEquals(
            "ViewState was not ERROR", MainViewModel.ViewState.ERROR,
            viewModel.viewState.value
        )
    }

}