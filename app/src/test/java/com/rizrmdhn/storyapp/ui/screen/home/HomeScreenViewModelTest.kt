package com.rizrmdhn.storyapp.ui.screen.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.rizrmdhn.core.domain.model.Story
import com.rizrmdhn.core.domain.usecase.StoryUseCase
import com.rizrmdhn.storyapp.utils.DummyData
import com.rizrmdhn.storyapp.utils.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
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
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeScreenViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var useCase: StoryUseCase
    private lateinit var viewModel: HomeScreenViewModel
    private val dummyStories = DummyData.generateDummyStory()

    @Before
    fun setUp() {
        viewModel = HomeScreenViewModel(useCase)
    }

    @Test
    fun `getStories should return empty list of stories`() = runTest {
        val expectedStories = MutableStateFlow<PagingData<Story>>(PagingData.empty())

        Mockito.`when`(useCase.getLocationSetting()).thenReturn(MutableStateFlow(1))
        Mockito.`when`(useCase.getStories(1, 1)).thenReturn(expectedStories)

        launch {
            viewModel.getLocationSetting()
            viewModel.getStories()
        }

        advanceUntilIdle()

        val differ = AsyncPagingDataDiffer(
            diffCallback = TestDiffCallback<Story>(),
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )


        val actualData = viewModel.state
        differ.submitData(actualData.value)

        Mockito.verify(useCase).getStories(1, 1)

        Assert.assertTrue(differ.snapshot().items.isEmpty())
    }

    @Test
    fun `getStories should return list of stories`() = runTest {
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStories)
        val expectedStories = MutableStateFlow<PagingData<Story>>(PagingData.empty())
        expectedStories.value = data

        Mockito.`when`(useCase.getLocationSetting()).thenReturn(MutableStateFlow(1))
        Mockito.`when`(useCase.getStories(1, 1)).thenReturn(expectedStories)

        launch {
            viewModel.getLocationSetting()
            viewModel.getStories()
        }

        advanceUntilIdle()

        val differ = AsyncPagingDataDiffer(
            diffCallback = TestDiffCallback<Story>(),
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )


        val actualData = viewModel.state
        differ.submitData(actualData.value)

        Mockito.verify(useCase).getStories(1, 1)

        Assert.assertEquals(dummyStories, differ.snapshot().items)
        Assert.assertEquals(dummyStories.size, differ.snapshot().items.size)
        Assert.assertEquals(dummyStories[0], differ.snapshot().items[0])
        Assert.assertTrue(differ.snapshot().items.isNotEmpty())
    }

    @Test
    fun `locationSwitched should return 1`() = runTest {
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStories)
        val expectedStories = MutableStateFlow<PagingData<Story>>(PagingData.empty())
        expectedStories.value = data

        Mockito.`when`(useCase.getLocationSetting()).thenReturn(MutableStateFlow(1))
        Mockito.`when`(useCase.getStories(1, 1)).thenReturn(expectedStories)
        Mockito.`when`(useCase.setLocationSetting(1)).thenReturn(Unit)

        launch {
            viewModel.locationSwitched()
            viewModel.getLocationSetting()
            viewModel.getStories()
        }

        advanceUntilIdle()

        Mockito.verify(useCase).setLocationSetting(1)
        Assert.assertEquals(1, viewModel.location.value)
    }

    @Test
    fun `locationSwitched should return 0`() = runTest {
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStories)
        val expectedStories = MutableStateFlow<PagingData<Story>>(PagingData.empty())
        expectedStories.value = data

        Mockito.`when`(useCase.getLocationSetting()).thenReturn(MutableStateFlow(0))
        Mockito.`when`(useCase.getStories(1, 0)).thenReturn(expectedStories)
        Mockito.`when`(useCase.setLocationSetting(1)).thenReturn(Unit)

        launch {
            viewModel.locationSwitched()
            viewModel.getLocationSetting()
            viewModel.getStories()
        }

        advanceUntilIdle()

        Mockito.verify(useCase).setLocationSetting(1)
        Assert.assertEquals(0, viewModel.location.value)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class TestDiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}

class StoryPagingSource : PagingSource<Int, Flow<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Flow<List<Story>>> {
        return LoadResult.Page(
            data = listOf(),
            prevKey = null,
            nextKey = null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Flow<List<Story>>>): Int? {
        return 0
    }

}