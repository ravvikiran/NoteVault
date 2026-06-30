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
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        @ApplicationScope scope: CoroutineScope
    ): NoteVaultDatabase {
        return Room.databaseBuilder(
            context,
            NoteVaultDatabase::class.java,
            "notevault.db"
        )
            .addCallback(SeedDatabaseCallback(scope))
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: NoteVaultDatabase): NoteDao = database.noteDao()

    @Provides
    @Singleton
    fun provideFolderDao(database: NoteVaultDatabase): FolderDao = database.folderDao()
}

/**
 * Seeds default folders on first database creation.
 * Uses a separate callback class to avoid circular dependency issues.
 */
private class SeedDatabaseCallback(
    private val scope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Insert default folders directly via SQL to avoid circular dependency
        // with the DAO (which requires the database to be fully built).
        scope.launch {
            val folders = listOf(
                Triple("Inbox", "#8B7355", "inbox"),
                Triple("Ideas", "#D4A847", "lightbulb"),
                Triple("Diary", "#A67B7B", "book"),
                Triple("Journal", "#5E8B8B", "edit_note"),
                Triple("Shopping", "#BF8B3C", "shopping_cart"),
                Triple("To-Do", "#5B7B5C", "check_circle"),
                Triple("Work", "#6B7FA3", "work"),
                Triple("Study", "#7B5E7B", "school"),
                Triple("Scrap", "#5A5A5A", "note"),
            )
            folders.forEachIndexed { index, (name, color, icon) ->
                db.execSQL(
                    """INSERT INTO folders (name, colorHex, iconName, sortOrder, isDefault, createdAt) 
                       VALUES ('$name', '$color', '$icon', $index, 1, ${System.currentTimeMillis()})"""
                )
            }
        }
    }
}
