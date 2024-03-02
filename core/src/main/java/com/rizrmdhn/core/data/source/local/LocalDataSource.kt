package com.rizrmdhn.core.data.source.local

import com.rizrmdhn.core.data.source.local.entity.StoryEntity
import com.rizrmdhn.core.data.source.local.preferences.SettingPreferences
import com.rizrmdhn.core.data.source.local.room.StoryDao

class LocalDataSource(
    private val storyDao: StoryDao,
    private val settingPreferences: SettingPreferences,
) {
    fun getAllStories() = storyDao.getAllStories()

    fun getFavoriteStories() = storyDao.getFavoriteStories()

    suspend fun insertStories(storyList: List<StoryEntity>) = storyDao.insertStories(storyList)

    fun setFavoriteStory(story: StoryEntity, newState: Boolean) {
        story.isFavorite = newState
        storyDao.updateFavoriteStory(story)
    }

    fun isFavoriteStory(id: String) = storyDao.isFavoriteStory(id)

    fun searchStory(query: String) = storyDao.searchStory(query)

    fun searchFavoriteStory(query: String) = storyDao.searchFavoriteStory(query)

    fun getThemeSetting() = settingPreferences.getThemeSetting()

    suspend fun saveThemeSetting(isDarkMode: Boolean) = settingPreferences.saveThemeSetting(isDarkMode)

    fun getAccessToken() = settingPreferences.getAuthToken()

    suspend fun saveAccessToken(token: String) = settingPreferences.saveAuthToken(token)

    suspend fun removeAccessToken() = settingPreferences.removeAuthToken()

    fun getLocaleSetting() = settingPreferences.getLocaleSetting()

    suspend fun saveLocaleSetting(locale: String) = settingPreferences.saveLocaleSetting(locale)
}