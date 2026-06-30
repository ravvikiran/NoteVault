# NoteVault

A premium all-in-one note organizer for Android — beautiful, organized, fully offline.

## Concept

NoteVault is a modern redesign of the AwesomeNote concept: a beautiful, organized notes app where everything has a place. Color-coded folders, multiple note types, calendar view, and fast capture. It feels like a premium leather notebook went digital — warm, personal, organized.

## Screenshots

*Coming soon — app is in active development.*

## Features (Phase 1 — Implemented)

- ✅ Color-coded folder system with 9 default folders (Inbox, Ideas, Diary, Journal, Shopping, To-Do, Work, Study, Scrap)
- ✅ Text note creation and editing with auto-save on back navigation
- ✅ Pin & Favorite notes
- ✅ Soft-delete with trash (30-day recovery ready)
- ✅ Folder navigation sidebar (Material3 drawer)
- ✅ Bottom navigation (Today, Notes, Calendar, Search, Settings)
- ✅ Today dashboard with daily prompt
- ✅ Warm premium theme (light + dark mode with earth-tone palettes)
- ✅ Material You dynamic color support (opt-in)
- ✅ Layered cards with soft shadows, smooth Compose animations
- ✅ Room database for local storage
- ✅ Full JSON export/import for backup and migration
- ✅ MVVM + Clean Architecture with Hilt DI
- ✅ Edge-to-edge UI with transparent system bars

## Features (Planned)

- Checklists with animated checkboxes
- Diary entries (mood + weather tags)
- Drawing/sketch notes (canvas with pen tool)
- Photo notes (camera/gallery)
- Calendar view
- Full-text search with folder/date/type filters
- Tags system with tag cloud
- Reminders via local notifications
- Quick capture home screen widget
- App lock (PIN/biometric)
- Sort & view modes (list, compact, card grid)
- Note sharing (text, PDF, image)

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 1.9 |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt (with KSP) |
| Database | Room |
| Serialization | Kotlin Serialization (JSON) |
| Async | Coroutines + Flow |
| Navigation | Compose Navigation |
| Images | Coil |
| Animations | Lottie + Compose Animations |
| Build | Gradle 8.5 + AGP 8.2.2 |

## Requirements

- Android Studio Iguana (2023.2.1) or later
- JDK 17 (bundled with Android Studio)
- Android SDK 34
- Min device: Android 8.0 (API 26)

## Building

1. Clone the repository
2. Open in Android Studio
3. Let Gradle sync (it will download all dependencies)
4. Run on device or emulator (API 26+)

```bash
# Command line build
./gradlew assembleDebug    # Linux/Mac
gradlew.bat assembleDebug  # Windows
```

## Constraints

- **Zero cost** — no paid services, no cloud backend
- **All data stored locally** — Room DB + JSON exports on device
- **Fully offline** — no internet permission required for core features
- **Optional** Google Sign-In for personalization only (not implemented yet)

## Architecture

```
app/
├── data/
│   ├── local/          # Room database, entities, DAOs
│   ├── model/          # Domain models (NoteType, ChecklistItem, FolderColor)
│   ├── repository/     # Repository layer (single source of truth)
│   └── export/         # JSON export/import for backup
├── di/                 # Hilt dependency injection modules
└── ui/
    ├── components/     # Reusable composables (FolderSidebar)
    ├── navigation/     # Nav graph, routes, bottom nav
    ├── screens/        # Feature screens
    │   ├── today/      # Today dashboard
    │   ├── notes/      # Notes list
    │   └── editor/     # Note editor
    └── theme/          # Material 3 theme (warm palettes, typography)
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/my-feature`)
3. Commit changes (`git commit -am 'Add my feature'`)
4. Push to branch (`git push origin feature/my-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
