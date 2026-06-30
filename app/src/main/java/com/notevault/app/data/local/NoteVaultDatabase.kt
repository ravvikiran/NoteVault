package com.notevault.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.notevault.app.data.local.dao.FolderDao
import com.notevault.app.data.local.dao.NoteDao
import com.notevault.app.data.local.entity.FolderEntity
import com.notevault.app.data.local.entity.NoteEntity
import com.notevault.app.data.local.entity.NoteTagCrossRef
import com.notevault.app.data.local.entity.TagEntity

@Database(
    entities = [
        NoteEntity::class,
        FolderEntity::class,
        TagEntity::class,
        NoteTagCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NoteVaultDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun folderDao(): FolderDao
}
