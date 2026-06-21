# NoteVault

A premium all-in-one note organizer for Android — beautiful, organized, fully offline.

## Concept

NoteVault is a modern redesign of the AwesomeNote concept: a beautiful, organized notes app where everything has a place. Color-coded folders, multiple note types, calendar view, and fast capture. It feels like a premium leather notebook went digital — warm, personal, organized.

## Features (Phase 1 — Implemented)

- ✅ Color-coded folder system with 9 default folders
- ✅ Text note creation and editing with auto-save
- ✅ Pin & Favorite notes
- ✅ Folder navigation sidebar (drawer)
- ✅ Bottom navigation (Today, Notes, Calendar, Search, Settings)
- ✅ Today dashboard with daily prompt
- ✅ Warm premium theme (light + dark mode)
- ✅ Material You dynamic color support (optional)
- ✅ Soft shadows, layered cards, smooth animations
- ✅ Room database for local storage
- ✅ Full JSON export/import for backup
- ✅ MVVM + Clean Architecture with Hilt DI

## Features (Planned)

- Checklists with animated checkboxes
- Diary entries (mood + weather tags)
- Drawing/sketch notes
- Photo notes
- Calendar view
- Full-text search with filters
- Tags system
- Reminders (local notifications)
- Quick capture widget
- App lock (PIN/biometric)
- Trash with 30-day recovery
- Sort & view modes
- Note sharing (text/PDF/image)

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Database | Room |
| Serialization | Kotlin Serialization (JSON) |
| Async | Coroutines + Flow |
| Navigation | Compose Navigation |
| Images | Coil |
| Animations | Lottie + Compose Animations |

## Constraints

- Zero cost — no paid services, no cloud backend
- All data stored locally (Room DB + JSON exports)
- Fully offline — no internet required
- Optional Google Sign-In for personalization only

## Building

Open in Android Studio (Iguana or later), sync Gradle, and run on device/emulator (API 26+).

```bash
./gradlew assembleDebug
```

## Architecture

```
app/
├── data/
│   ├── local/        # Room entities, DAOs, database
│   ├── model/        # Domain models (NoteType, ChecklistItem, etc.)
│   ├── repository/   # Repository layer
│   └── export/       # JSON export/import
├── di/               # Hilt modules
└── ui/
    ├── components/   # Reusable composables (FolderSidebar, etc.)
    ├── navigation/   # Nav graph, bottom nav
    ├── screens/      # Feature screens (notes, editor, today, etc.)
    └── theme/        # Material 3 theme (warm palettes)
```
