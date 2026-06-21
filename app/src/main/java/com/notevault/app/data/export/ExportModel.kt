package com.notevault.app.data.export

import kotlinx.serialization.Serializable

/**
 * Full vault export model for backup/restore.
 * All data is serialized to a single JSON file.
 */
@Serializable
data class NoteVaultExport(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val folders: List<FolderExport> = emptyList(),
    val notes: List<NoteExport> = emptyList(),
    val tags: List<String> = emptyList()
)

@Serializable
data class FolderExport(
    val id: Long,
    val name: String,
    val colorHex: String,
    val iconName: String,
    val sortOrder: Int,
    val isDefault: Boolean
)

@Serializable
data class NoteExport(
    val id: Long,
    val title: String,
    val content: String,
    val noteType: String,
    val folderId: Long? = null,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val createdAt: Long,
    val modifiedAt: Long,
    val reminderAt: Long? = null,
    val moodEmoji: String? = null,
    val weatherTag: String? = null,
    val tags: List<String> = emptyList(),
    val checklistItems: List<ChecklistItemExport>? = null,
    val colorHex: String? = null
)

@Serializable
data class ChecklistItemExport(
    val id: String,
    val text: String,
    val isChecked: Boolean
)
