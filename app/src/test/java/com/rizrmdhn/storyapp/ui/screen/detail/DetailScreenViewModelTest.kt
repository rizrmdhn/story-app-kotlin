package com.rizrmdhn.storyapp.ui.screen.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rizrmdhn.core.data.Resource
import com.rizrmdhn.core.domain.model.StoryDetails
import com.rizrmdhn.core.domain.usecase.StoryUseCase
import com.rizrmdhn.core.utils.DataMapper
import com.rizrmdhn.storyapp.utils.DummyData
import com.rizrmdhn.storyapp.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DetailScreenViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()


    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var useCase: StoryUseCase
    private lateinit var viewModel: DetailScreenViewModel


    @Before
    fun setUp() {
        viewModel = DetailScreenViewModel(useCase)
    }

    @Test
    fun `getStoryDetail should return story details`() = runTest {
        val expectedDetails =
            DataMapper.mapStoryToStoryDetails(DummyData.generateDummyStoryDetails())
        val expectedFlow =
            MutableStateFlow<Resource<StoryDetails>>(Resource.Success(expectedDetails))

        // Mock use case behavior
        `when`(useCase.getLocationSetting()).thenReturn(MutableStateFlow(0))
        `when`(useCase.getAccessToken()).thenReturn(MutableStateFlow("token"))
        `when`(useCase.getStoryDetail("1", "Bearer token")).thenReturn(expectedFlow)

        launch {
            viewModel.getLocationSetting()
            viewModel.getAccessToken()
            viewModel.getStoryDetail("1")
        }

        advanceUntilIdle()

        val actualFlow = viewModel.state

        Mockito.verify(useCase).getStoryDetail("1", "Bearer token")

        Assert.assertTrue(actualFlow.value is Resource.Success)
        Assert.assertEquals(expectedDetails, actualFlow.value.data)
        Assert.assertEquals(expectedDetails.id, actualFlow.value.data?.id)
        Assert.assertEquals(expectedDetails.name, actualFlow.value.data?.name)
    }
}