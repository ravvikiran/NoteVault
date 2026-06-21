package com.notevault.app.ui.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notevault.app.data.local.entity.FolderEntity
import com.notevault.app.data.local.entity.NoteEntity
import com.notevault.app.data.repository.FolderRepository
import com.notevault.app.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {

    private val _selectedFolderId = MutableStateFlow<Long?>(null)
    val selectedFolderId: StateFlow<Long?> = _selectedFolderId.asStateFlow()

    val folders: StateFlow<List<FolderEntity>> = folderRepository.getAllFolders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes: StateFlow<List<NoteEntity>> = _notes.asStateFlow()

    init {
        loadNotes()
    }

    fun selectFolder(folderId: Long?) {
        _selectedFolderId.value = folderId
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            val flow = when (val id = _selectedFolderId.value) {
                null -> noteRepository.getAllActiveNotes()
                else -> noteRepository.getNotesByFolder(id)
            }
            flow.collect { notesList ->
                _notes.value = notesList
            }
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            noteRepository.softDeleteNote(noteId)
        }
    }

    fun togglePin(noteId: Long, isPinned: Boolean) {
        viewModelScope.launch {
            noteRepository.togglePin(noteId, !isPinned)
        }
    }

    fun toggleFavorite(noteId: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            noteRepository.toggleFavorite(noteId, !isFavorite)
        }
    }
}
