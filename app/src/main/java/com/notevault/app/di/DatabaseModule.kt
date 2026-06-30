package com.notevault.app.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.notevault.app.data.local.NoteVaultDatabase
import com.notevault.app.data.local.dao.FolderDao
import com.notevault.app.data.local.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
            .addCallback(SeedDatabaseCallback())
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
 * Runs synchronously inside onCreate to guarantee the db reference is valid.
 */
private class SeedDatabaseCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
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
        val timestamp = System.currentTimeMillis()
        db.beginTransaction()
        try {
            folders.forEachIndexed { index, (name, color, icon) ->
                db.execSQL(
                    "INSERT INTO folders (name, colorHex, iconName, sortOrder, isDefault, createdAt) VALUES (?, ?, ?, ?, 1, ?)",
                    arrayOf(name, color, icon, index, timestamp)
                )
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}
