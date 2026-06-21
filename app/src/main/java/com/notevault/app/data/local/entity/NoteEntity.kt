package com.notevault.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["folderId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["folderId"]),
        Index(value = ["createdAt"]),
        Index(value = ["isPinned"]),
        Index(value = ["isFavorite"]),
        Index(value = ["isDeleted"])
    ]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val noteType: String = "text", // text, checklist, diary, drawing, photo
    val folderId: Long? = null,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,
    val deletedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val modifiedAt: Long = System.currentTimeMillis(),
    val reminderAt: Long? = null,
    val moodEmoji: String? = null, // For diary entries
    val weatherTag: String? = null, // For diary entries
    val tagsJson: String = "[]", // JSON array of tag strings
    val checklistJson: String? = null, // JSON for checklist items
    val colorHex: String? = null // Optional note-level color
)
