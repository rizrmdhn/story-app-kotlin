package com.rizrmdhn.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rizrmdhn.core.data.source.local.entity.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
    @Query("SELECT * FROM story")
    fun getAllStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM story where isFavorite = 1")
    fun getFavoriteStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM story WHERE name LIKE '%' || :query || '%'")
    fun searchStory(query: String): Flow<List<StoryEntity>>

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)

    @Update
    fun updateFavoriteStory(story: StoryEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM story WHERE id=:id AND isFavorite = 1)")
    fun isFavoriteStory(id: String): Flow<Boolean>

    @Query("SELECT * FROM story WHERE name LIKE '%' || :query || '%' AND isFavorite = 1")
    fun searchFavoriteStory(query: String): Flow<List<StoryEntity>>

}