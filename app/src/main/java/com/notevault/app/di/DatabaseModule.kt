package com.notevault.app.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.notevault.app.data.local.NoteVaultDatabase
import com.notevault.app.data.local.dao.FolderDao
import com.notevault.app.data.local.dao.NoteDao
import com.notevault.app.data.local.entity.FolderEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteVaultDatabase {
        return Room.databaseBuilder(
            context,
            NoteVaultDatabase::class.java,
            "notevault.db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                        val folderDao = provideDatabase(context).folderDao()
                        seedDefaultFolders(folderDao)
                    }
                }
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: NoteVaultDatabase): NoteDao = database.noteDao()

    @Provides
    @Singleton
    fun provideFolderDao(database: NoteVaultDatabase): FolderDao = database.folderDao()

    private suspend fun seedDefaultFolders(folderDao: FolderDao) {
        val defaultFolders = listOf(
            FolderEntity(name = "Inbox", colorHex = "#8B7355", iconName = "inbox", sortOrder = 0, isDefault = true),
            FolderEntity(name = "Ideas", colorHex = "#D4A847", iconName = "lightbulb", sortOrder = 1, isDefault = true),
            FolderEntity(name = "Diary", colorHex = "#A67B7B", iconName = "book", sortOrder = 2, isDefault = true),
            FolderEntity(name = "Journal", colorHex = "#5E8B8B", iconName = "edit_note", sortOrder = 3, isDefault = true),
            FolderEntity(name = "Shopping", colorHex = "#BF8B3C", iconName = "shopping_cart", sortOrder = 4, isDefault = true),
            FolderEntity(name = "To-Do", colorHex = "#5B7B5C", iconName = "check_circle", sortOrder = 5, isDefault = true),
            FolderEntity(name = "Work", colorHex = "#6B7FA3", iconName = "work", sortOrder = 6, isDefault = true),
            FolderEntity(name = "Study", colorHex = "#7B5E7B", iconName = "school", sortOrder = 7, isDefault = true),
            FolderEntity(name = "Scrap", colorHex = "#5A5A5A", iconName = "note", sortOrder = 8, isDefault = true),
        )
        folderDao.insertFolders(defaultFolders)
    }
}
