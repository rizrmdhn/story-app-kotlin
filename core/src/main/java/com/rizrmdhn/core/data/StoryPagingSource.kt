package com.rizrmdhn.core.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rizrmdhn.core.data.source.remote.RemoteDataSource
import com.rizrmdhn.core.data.source.remote.response.ListStoryItem
import com.rizrmdhn.core.domain.model.Story
import com.rizrmdhn.core.utils.DataMapper


class StoryPagingSource(
    private val remoteDataSource: RemoteDataSource,
    private val location: Int,
    private val token: String
) : PagingSource<Int, Story>() {
    private companion object {
        const val INITIAL_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            Log.d("StoryPagingSource", "load: ${params.loadSize}")
            val response = remoteDataSource.getStories(page, params.loadSize, location, token)
            val mappedDataToEntities = DataMapper.mapResponseToEntities(response)
            val mappedDataToModel = DataMapper.mapEntitiesToDomain(mappedDataToEntities)
            LoadResult.Page(
                data = mappedDataToModel,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}