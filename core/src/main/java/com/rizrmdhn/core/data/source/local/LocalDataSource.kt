package com.rizrmdhn.core.data.source.local

import com.rizrmdhn.core.data.source.local.entity.RemoteKeys
import com.rizrmdhn.core.data.source.local.entity.StoryEntity
import com.rizrmdhn.core.data.source.local.preferences.SettingPreferences
import com.rizrmdhn.core.data.source.local.room.RemoteKeysDao
import com.rizrmdhn.core.data.source.local.room.StoryDao

class LocalDataSource(
    private val storyDao: StoryDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val settingPreferences: SettingPreferences,
) {
    fun getAllStories() = storyDao.getAllStories()

    suspend fun insertStories(storyList: List<StoryEntity>) = storyDao.insertStories(storyList)

    suspend fun deleteAllStories() = storyDao.deleteAllStories()

    suspend fun getRemoteKeysId(id: String) = remoteKeysDao.getRemoteKeysId(id)

    suspend fun insertAll(remoteKey: List<RemoteKeys>) = remoteKeysDao.insertAll(remoteKey)

    suspend fun deleteRemoteKeys() = remoteKeysDao.deleteRemoteKeys()

    fun getThemeSetting() = settingPreferences.getThemeSetting()

    suspend fun saveThemeSetting(isDarkMode: Boolean) = settingPreferences.saveThemeSetting(isDarkMode)

    fun getAccessToken() = settingPreferences.getAuthToken()

    suspend fun saveAccessToken(token: String) = settingPreferences.saveAuthToken(token)

    suspend fun removeAccessToken() = settingPreferences.removeAuthToken()

    fun getLocaleSetting() = settingPreferences.getLocaleSetting()

    suspend fun saveLocaleSetting(locale: String) = settingPreferences.saveLocaleSetting(locale)

    fun getLocationSetting() = settingPreferences.getLocationSetting()

    suspend fun saveLocationSetting(location: Int) = settingPreferences.saveLocationSetting(location)
}