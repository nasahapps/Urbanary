package com.nasahapps.urbanary.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.rule.ActivityTestRule
import com.nasahapps.urbanary.model.Definition
import com.nasahapps.urbanary.repository.Repository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

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
        viewModel = MainViewModelFactory(mockRepository, activityRule.activity)
            .create(MainViewModel::class.java)
    }

    @Test
    fun searchingForDefinitionsWithValidQuery__shouldReturnResults() {
        viewModel.getDefinitions("something")
        Assert.assertEquals(
            "ViewState was incorrect", MainViewModel.ViewState.LIST,
            viewModel.viewState.value
        )
    }

    @Test
    fun searchingForNothing__shouldDoNothing() {
        viewModel.getDefinitions(null)
        Assert.assertEquals(
            "ViewState was incorrect", MainViewModel.ViewState.INITIAL,
            viewModel.viewState.value
        )
    }

    @Test
    fun searchingForDefinitionsWithInvalidQuery__shouldReturnNoResults() {
        viewModel.getDefinitions("empty")
        Assert.assertEquals(
            "ViewState was incorrect", MainViewModel.ViewState.EMPTY,
            viewModel.viewState.value
        )
    }

    @Test
    fun errorDuringSearch__shouldHaveErrorViewState() {
        viewModel.getDefinitions("error")
        Assert.assertEquals(
            "ViewState was incorrect", MainViewModel.ViewState.ERROR,
            viewModel.viewState.value
        )
    }

}