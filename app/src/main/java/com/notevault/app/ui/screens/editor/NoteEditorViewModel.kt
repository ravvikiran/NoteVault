package com.notevault.app.ui.screens.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notevault.app.data.local.entity.NoteEntity
import com.notevault.app.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoteEditorState(
    val id: Long? = null,
    val title: String = "",
    val content: String = "",
    val noteType: String = "text",
    val folderId: Long? = null,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val isSaving: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NoteEditorState())
    val state: StateFlow<NoteEditorState> = _state.asStateFlow()

    fun loadNote(noteId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val note = noteRepository.getNoteById(noteId)
            if (note != null) {
                _state.value = NoteEditorState(
                    id = note.id,
                    title = note.title,
                    content = note.content,
                    noteType = note.noteType,
                    folderId = note.folderId,
                    isPinned = note.isPinned,
                    isFavorite = note.isFavorite,
                    isLoading = false
                )
            } else {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    fun initNewNote(folderId: Long?, noteType: String) {
        _state.value = NoteEditorState(
            folderId = folderId,
            noteType = noteType,
            isLoading = false
        )
    }

    fun updateTitle(title: String) {
        _state.value = _state.value.copy(title = title)
    }

    fun updateContent(content: String) {
        _state.value = _state.value.copy(content = content)
    }

    fun saveNote(onSaved: () -> Unit = {}) {
        viewModelScope.launch {
            val state = _state.value
            _state.value = state.copy(isSaving = true)

            val currentTime = System.currentTimeMillis()

            if (state.id != null) {
                // Update existing
                val note = noteRepository.getNoteById(state.id)
                if (note != null) {
                    noteRepository.updateNote(
                        note.copy(
                            title = state.title,
                            content = state.content,
                            modifiedAt = currentTime
                        )
                    )
                }
            } else {
                // Create new
                val note = NoteEntity(
                    title = state.title,
                    content = state.content,
                    noteType = state.noteType,
                    folderId = state.folderId,
                    createdAt = currentTime,
                    modifiedAt = currentTime
                )
                val newId = noteRepository.insertNote(note)
                _state.value = _state.value.copy(id = newId)
            }

            _state.value = _state.value.copy(isSaving = false)
            onSaved()
        }
    }

    fun togglePin() {
        val state = _state.value
        _state.value = state.copy(isPinned = !state.isPinned)
        if (state.id != null) {
            viewModelScope.launch {
                noteRepository.togglePin(state.id, !state.isPinned)
            }
        }
    }

    fun toggleFavorite() {
        val state = _state.value
        _state.value = state.copy(isFavorite = !state.isFavorite)
        if (state.id != null) {
            viewModelScope.launch {
                noteRepository.toggleFavorite(state.id, !state.isFavorite)
            }
        }
    }

    fun deleteNote(onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            val id = _state.value.id
            if (id != null) {
                noteRepository.softDeleteNote(id)
            }
            onDeleted()
        }
    }
}
