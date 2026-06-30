package com.notevault.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ChecklistItem(
    val id: String = "",
    val text: String = "",
    val isChecked: Boolean = false
) {
    companion object {
        fun create(text: String = "", isChecked: Boolean = false): ChecklistItem {
            return ChecklistItem(
                id = java.util.UUID.randomUUID().toString(),
                text = text,
                isChecked = isChecked
            )
        }
    }
}
