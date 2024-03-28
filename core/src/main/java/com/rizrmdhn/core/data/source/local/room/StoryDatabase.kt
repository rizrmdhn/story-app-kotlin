package com.rizrmdhn.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rizrmdhn.core.data.source.local.entity.RemoteKeys
import com.rizrmdhn.core.data.source.local.entity.StoryEntity

@Database(
    entities = [
        StoryEntity::class,
        RemoteKeys::class
    ],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}