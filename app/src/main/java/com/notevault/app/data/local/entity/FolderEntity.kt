package com.notevault.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "folders",
    indices = [Index(value = ["sortOrder"])]
)
data class FolderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val colorHex: String = "#8B7355", // Warm brown default
    val iconName: String = "folder", // Material icon name
    val sortOrder: Int = 0,
    val isDefault: Boolean = false, // Pre-built folders can't be deleted
    val createdAt: Long = System.currentTimeMillis()
)
