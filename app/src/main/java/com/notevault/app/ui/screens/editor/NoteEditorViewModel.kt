package com.notevault.app.ui.screens.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notevault.app.data.local.entity.NoteEntity
import com.notevault.app.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
            _state.update { it.copy(isLoading = true) }
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
                _state.update { it.copy(isLoading = false) }
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
        _state.update { it.copy(title = title) }
    }

    fun updateContent(content: String) {
        _state.update { it.copy(content = content) }
    }

    fun saveNote(onSaved: () -> Unit = {}) {
        viewModelScope.launch {
            val currentState = _state.value
            _state.update { it.copy(isSaving = true) }

            val currentTime = System.currentTimeMillis()

            if (currentState.id != null) {
                val note = noteRepository.getNoteById(currentState.id)
                if (note != null) {
                    noteRepository.updateNote(
                        note.copy(
                            title = currentState.title,
                            content = currentState.content,
                            modifiedAt = currentTime
                        )
                    )
                }
            } else {
                val note = NoteEntity(
                    title = currentState.title,
                    content = currentState.content,
                    noteType = currentState.noteType,
                    folderId = currentState.folderId,
                    createdAt = currentTime,
                    modifiedAt = currentTime
                )
                val newId = noteRepository.insertNote(note)
                _state.update { it.copy(id = newId) }
            }

            _state.update { it.copy(isSaving = false) }
            onSaved()
        }
    }

    fun togglePin() {
        _state.update { current ->
            val newPinned = !current.isPinned
            current.id?.let { id ->
                viewModelScope.launch {
                    noteRepository.togglePin(id, newPinned)
                }
            }
            current.copy(isPinned = newPinned)
        }
    }

    fun toggleFavorite() {
        _state.update { current ->
            val newFavorite = !current.isFavorite
            current.id?.let { id ->
                viewModelScope.launch {
                    noteRepository.toggleFavorite(id, newFavorite)
                }
            }
            current.copy(isFavorite = newFavorite)
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
