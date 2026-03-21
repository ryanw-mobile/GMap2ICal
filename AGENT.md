# AI Agent Instructions

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

GMap2iCal is a Kotlin Multiplatform **Compose Desktop** application that converts Google Maps Activity Timeline JSON data to iCalendar (.ics) files. Single module (`:composeApp`), JVM/Desktop only target.

## Build & Development Commands

```bash
# Build
./gradlew build                    # Full build with tests
./gradlew composeApp:build         # Build app module only

# Run
./gradlew run                      # Run in dev mode
./gradlew runReleaseDistributable  # Run optimized build

# Test
./gradlew desktopTest              # Run tests
./gradlew allTests                 # Run all platform tests

# Code quality
./gradlew ktlintCheck              # Lint check
./gradlew ktlintFormat             # Auto-format
./gradlew jacocoTestReport         # Code coverage report (XML + HTML)

# Distribution
./gradlew packageDmg               # macOS package
./gradlew packageMsi               # Windows package
./gradlew packageDeb               # Linux package
```

CI runs: `xvfb-run ./gradlew build jacocoTestReport`

**Requirements**: JDK 17, Gradle 9.4.0+

## Architecture

Layered MVVM with clean architecture separation:

```
UI Layer        → Compose composables, ViewModels (MoKo MVVM)
Domain Layer    → Use cases, domain models, repository interfaces
Data Layer      → Repository implementations, DTOs, Ktor HTTP client
DI              → Koin (5 modules: App, Network, Repository, UseCase, ViewModel)
```

### Data Flow

```
User triggers export
  → MainScreenViewModel.startExport()
  → LocalFileRepository.getFileList()          # Find JSON files on disk
  → TimelineRepository.getTimeLine()           # Deserialize JSON → Timeline domain model
  → GetPlaceVisitVEventUseCase                 # PlaceVisit → VEvent
  → GetActivitySegmentVEventUseCase            # ActivitySegment → VEvent
  → (Optional) GoogleApiDataSource via Ktor    # Fetch place details from Google Places API
  → VEvent.export()                            # Serialize to iCalendar string
  → LocalFileRepository.exportICal()           # Write .ics file to disk
```

### Key Models

- `VEvent` — core iCalendar event model with `.export()` to produce .ics string
- `Timeline` — container for `ActivitySegment` and `PlaceVisit` entries
- DTOs in `data/models/` map to domain models in `domain/models/` via mappers

### Error Handling

Uses `Result<T>` pattern with a custom `.except()` extension for exception wrapping. Avoid throwing exceptions in business logic; propagate errors via `Result`.

## Key Technologies

| Concern | Library |
|---------|---------|
| UI | Compose Desktop 1.10.2 |
| HTTP | Ktor 3.4.1 (CIO engine) |
| DI | Koin 4.1.1 |
| Serialization | Kotlin Serialization |
| MVVM | MoKo MVVM 0.16.1 |
| Testing | JUnit 5, MockK |
| Linting | ktlint 14.2.0 |
| Coverage | JaCoCo (Gradle built-in) |

## Testing

Tests live in `composeApp/src/commonTest/`. Use fake/stub implementations (not mocks where possible) for repository interfaces. Test data builders are available in test utilities.

Run a single test class: `./gradlew desktopTest --tests "uk.ryanwong.gmap2ics.SomeTest"`

## Configuration

App configuration (`app/configs/`) currently uses compile-time config (`DefaultConfig`) specifying input JSON path, output .ics path, Google Places API key, and export toggles. A UI-based settings migration is planned but not yet implemented.

## Dependency Management

All versions are managed via the Gradle Version Catalog at `gradle/libs.versions.toml`. Add new dependencies there, not directly in `build.gradle.kts`.
