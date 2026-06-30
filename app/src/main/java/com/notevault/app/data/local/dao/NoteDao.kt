package com.notevault.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.notevault.app.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes WHERE isDeleted = 0 ORDER BY isPinned DESC, modifiedAt DESC")
    fun getAllActiveNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE folderId = :folderId AND isDeleted = 0 ORDER BY isPinned DESC, modifiedAt DESC")
    fun getNotesByFolder(folderId: Long): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE folderId IS NULL AND isDeleted = 0 ORDER BY isPinned DESC, modifiedAt DESC")
    fun getUnfiledNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): NoteEntity?

    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun observeNoteById(noteId: Long): Flow<NoteEntity?>

    @Query("SELECT * FROM notes WHERE isFavorite = 1 AND isDeleted = 0 ORDER BY modifiedAt DESC")
    fun getFavoriteNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE isDeleted = 1 ORDER BY deletedAt DESC")
    fun getDeletedNotes(): Flow<List<NoteEntity>>

    @Query("""
        SELECT * FROM notes 
        WHERE isDeleted = 0 
        AND (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%')
        ORDER BY modifiedAt DESC
    """)
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Query("""
        SELECT * FROM notes 
        WHERE isDeleted = 0 
        AND createdAt >= :startOfDay 
        AND createdAt < :endOfDay
        ORDER BY createdAt DESC
    """)
    fun getNotesForDate(startOfDay: Long, endOfDay: Long): Flow<List<NoteEntity>>

    @Query("""
        SELECT * FROM notes 
        WHERE isDeleted = 0 
        AND noteType = 'checklist'
        AND modifiedAt >= :startOfDay
        ORDER BY modifiedAt DESC
    """)
    fun getTodayTodos(startOfDay: Long): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("UPDATE notes SET isDeleted = 1, deletedAt = :deletedAt WHERE id = :noteId")
    suspend fun softDeleteNote(noteId: Long, deletedAt: Long)

    @Query("UPDATE notes SET isDeleted = 0, deletedAt = NULL WHERE id = :noteId")
    suspend fun restoreNote(noteId: Long)

    @Query("DELETE FROM notes WHERE isDeleted = 1 AND deletedAt < :threshold")
    suspend fun permanentlyDeleteOldNotes(threshold: Long)

    @Query("UPDATE notes SET isPinned = :isPinned WHERE id = :noteId")
    suspend fun togglePin(noteId: Long, isPinned: Boolean)

    @Query("UPDATE notes SET isFavorite = :isFavorite WHERE id = :noteId")
    suspend fun toggleFavorite(noteId: Long, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM notes WHERE isDeleted = 0")
    fun getTotalNoteCount(): Flow<Int>
}
