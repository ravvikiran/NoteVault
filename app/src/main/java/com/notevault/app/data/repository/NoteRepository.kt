package com.notevault.app.data.repository

import com.notevault.app.data.local.dao.NoteDao
import com.notevault.app.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getAllActiveNotes(): Flow<List<NoteEntity>> =
        noteDao.getAllActiveNotes()

    fun getNotesByFolder(folderId: Long): Flow<List<NoteEntity>> =
        noteDao.getNotesByFolder(folderId)

    fun getUnfiledNotes(): Flow<List<NoteEntity>> =
        noteDao.getUnfiledNotes()

    suspend fun getNoteById(noteId: Long): NoteEntity? =
        noteDao.getNoteById(noteId)

    fun observeNoteById(noteId: Long): Flow<NoteEntity?> =
        noteDao.observeNoteById(noteId)

    fun getFavoriteNotes(): Flow<List<NoteEntity>> =
        noteDao.getFavoriteNotes()

    fun getDeletedNotes(): Flow<List<NoteEntity>> =
        noteDao.getDeletedNotes()

    fun searchNotes(query: String): Flow<List<NoteEntity>> =
        noteDao.searchNotes(query)

    fun getNotesForDate(startOfDay: Long, endOfDay: Long): Flow<List<NoteEntity>> =
        noteDao.getNotesForDate(startOfDay, endOfDay)

    fun getTodayTodos(startOfDay: Long): Flow<List<NoteEntity>> =
        noteDao.getTodayTodos(startOfDay)

    suspend fun insertNote(note: NoteEntity): Long =
        noteDao.insertNote(note)

    suspend fun updateNote(note: NoteEntity) =
        noteDao.updateNote(note)

    suspend fun softDeleteNote(noteId: Long) =
        noteDao.softDeleteNote(noteId)

    suspend fun restoreNote(noteId: Long) =
        noteDao.restoreNote(noteId)

    suspend fun permanentlyDeleteOldNotes(threshold: Long) =
        noteDao.permanentlyDeleteOldNotes(threshold)

    suspend fun togglePin(noteId: Long, isPinned: Boolean) =
        noteDao.togglePin(noteId, isPinned)

    suspend fun toggleFavorite(noteId: Long, isFavorite: Boolean) =
        noteDao.toggleFavorite(noteId, isFavorite)

    fun getTotalNoteCount(): Flow<Int> =
        noteDao.getTotalNoteCount()
}
