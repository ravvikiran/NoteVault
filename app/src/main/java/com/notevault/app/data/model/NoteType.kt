package com.notevault.app.data.model

enum class NoteType(val value: String, val label: String) {
    TEXT("text", "Text Note"),
    CHECKLIST("checklist", "Checklist"),
    DIARY("diary", "Diary Entry"),
    DRAWING("drawing", "Drawing"),
    PHOTO("photo", "Photo Note");

    companion object {
        fun fromValue(value: String): NoteType =
            entries.find { it.value == value } ?: TEXT
    }
}
