package com.notevault.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ChecklistItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String = "",
    val isChecked: Boolean = false
)
