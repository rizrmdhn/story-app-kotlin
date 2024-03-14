package com.rizrmdhn.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rizrmdhn.core.data.source.local.LocalDataSource
import com.rizrmdhn.core.data.source.local.entity.RemoteKeys
import com.rizrmdhn.core.data.source.remote.RemoteDataSource
import com.rizrmdhn.core.domain.model.Story
import com.rizrmdhn.core.utils.DataMapper


@OptIn(ExperimentalPagingApi::class)
class StoryPagingSource(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val location: Int,
    private val token: String
) : RemoteMediator<Int, Story>() {
    private companion object {
        const val INITIAL_PAGE = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
//        return try {
//            val page = params.key ?: INITIAL_PAGE
//            Log.d("StoryPagingSource", "load: ${params.loadSize}")
//            val response = remoteDataSource.getStories(page, params.loadSize, location, token)
//            val mappedDataToEntities = DataMapper.mapResponseToEntities(response)
//            val mappedDataToModel = DataMapper.mapEntitiesToDomain(mappedDataToEntities)
//            LoadResult.Page(
//                data = mappedDataToModel,
//                prevKey = if (page == INITIAL_PAGE) null else page - 1,
//                nextKey = if (response.isEmpty()) null else page + 1
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        return try {
            val response = remoteDataSource.getStories(page, state.config.pageSize, location, token)
            val isEndOfList = response.isEmpty()

            if (loadType == LoadType.REFRESH) {
                localDataSource.deleteRemoteKeys()
                localDataSource.deleteAllStories()
            }

            val prevKey = if (page == INITIAL_PAGE) null else page - 1
            val nextKey = if (isEndOfList) null else page + 1
            val remoteKeys = response.map {
                RemoteKeys(it.id, prevKey, nextKey)
            }
            localDataSource.insertAll(remoteKeys)
            localDataSource.insertStories(DataMapper.mapResponseToEntities(response))

            MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Story>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            localDataSource.getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Story>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            localDataSource.getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Story>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                localDataSource.getRemoteKeysId(id)
            }
        }
    }
}