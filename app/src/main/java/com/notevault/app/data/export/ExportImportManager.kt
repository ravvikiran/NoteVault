package com.notevault.app.data.export

import android.content.Context
import com.notevault.app.data.local.dao.FolderDao
import com.notevault.app.data.local.dao.NoteDao
import com.notevault.app.data.local.entity.FolderEntity
import com.notevault.app.data.local.entity.NoteEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExportImportManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val noteDao: NoteDao,
    private val folderDao: FolderDao
) {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    suspend fun exportToJson(): String {
        val folders = folderDao.getAllFolders().first()
        val notes = noteDao.getAllActiveNotes().first()

        val export = NoteVaultExport(
            folders = folders.map { folder ->
                FolderExport(
                    id = folder.id,
                    name = folder.name,
                    colorHex = folder.colorHex,
                    iconName = folder.iconName,
                    sortOrder = folder.sortOrder,
                    isDefault = folder.isDefault
                )
            },
            notes = notes.map { note ->
                NoteExport(
                    id = note.id,
                    title = note.title,
                    content = note.content,
                    noteType = note.noteType,
                    folderId = note.folderId,
                    isPinned = note.isPinned,
                    isFavorite = note.isFavorite,
                    createdAt = note.createdAt,
                    modifiedAt = note.modifiedAt,
                    reminderAt = note.reminderAt,
                    moodEmoji = note.moodEmoji,
                    weatherTag = note.weatherTag,
                    colorHex = note.colorHex
                )
            }
        )

        return json.encodeToString(export)
    }

    suspend fun exportToFile(): File {
        val jsonString = exportToJson()
        val exportDir = File(context.getExternalFilesDir(null), "exports")
        exportDir.mkdirs()
        val exportFile = File(exportDir, "notevault_backup_${System.currentTimeMillis()}.json")
        exportFile.writeText(jsonString)
        return exportFile
    }

    suspend fun importFromJson(jsonString: String): Result<Int> {
        return try {
            val export = json.decodeFromString<NoteVaultExport>(jsonString)

            // Import folders
            export.folders.forEach { folderExport ->
                folderDao.insertFolder(
                    FolderEntity(
                        name = folderExport.name,
                        colorHex = folderExport.colorHex,
                        iconName = folderExport.iconName,
                        sortOrder = folderExport.sortOrder,
                        isDefault = folderExport.isDefault
                    )
                )
            }

            // Import notes
            export.notes.forEach { noteExport ->
                noteDao.insertNote(
                    NoteEntity(
                        title = noteExport.title,
                        content = noteExport.content,
                        noteType = noteExport.noteType,
                        folderId = noteExport.folderId,
                        isPinned = noteExport.isPinned,
                        isFavorite = noteExport.isFavorite,
                        createdAt = noteExport.createdAt,
                        modifiedAt = noteExport.modifiedAt,
                        reminderAt = noteExport.reminderAt,
                        moodEmoji = noteExport.moodEmoji,
                        weatherTag = noteExport.weatherTag,
                        colorHex = noteExport.colorHex
                    )
                )
            }

            Result.success(export.notes.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
