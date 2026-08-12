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
./gradlew lintKotlin               # Lint check
./gradlew formatKotlin             # Auto-format
./gradlew jacocoTestReport         # Code coverage report (XML + HTML)

# Distribution
./gradlew packageDmg               # macOS package
./gradlew packageMsi               # Windows package
./gradlew packageDeb               # Linux package
```

CI runs: `xvfb-run ./gradlew build jacocoTestReport`

CI is split across two workflows so that pull requests from forks can be built safely:

- **Gradle Build** (`main_build.yml`) — builds and tests on `push` to `main` and on `pull_request`. Runs with no repository secrets because it executes contributor code. Uploads the JaCoCo XML report as the `jacoco-coverage` artifact, and nothing else: this job's output is attacker-controlled, so the commit the report belongs to is deliberately *not* passed through the artifact. Its job id `build` is a required status check on `main`, so do not rename it.
- **Coverage Report** (`coverage_report.yml`) — triggered by `workflow_run` when Gradle Build completes. Downloads the artifact and uploads coverage to Codacy using `CODACY_PROJECT_TOKEN`. `workflow_run` always runs the copy of the file on the default branch in this repository's context, so the secret is never reachable from fork code.

The commit both workflows are talking about is taken from `github.event.workflow_run.head_sha`, which GitHub generates and contributor code cannot influence. For a pull-request-triggered run this is the pull request HEAD commit rather than the merge commit, which is what Codacy needs and what a status must be posted against. Never reintroduce a scheme that reads the commit out of the artifact — a malicious pull request could then name any commit and publish a passing gate status onto another branch.

`workflow_run` results are not attached to the pull request that triggered them, so Coverage Report posts its own commit status against that SHA, under the context `coverage-report`. **That context must be listed in the required status checks for `main`** — it is what makes coverage upload a blocking gate rather than an advisory run. The status is posted on every outcome, including a failed build, so the check never silently disappears.

Both workflow names matter as identifiers: `workflow_run` matches Gradle Build by its `name:`, and branch protection matches the status by its context string.

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
| Linting | Kotlinter 5.4.2 |
| Coverage | JaCoCo (Gradle built-in) |

## Testing

Tests live in `composeApp/src/commonTest/`. Use fake/stub implementations (not mocks where possible) for repository interfaces. Test data builders are available in test utilities.

Run a single test class: `./gradlew desktopTest --tests "uk.ryanwong.gmap2ics.SomeTest"`

## Configuration

App configuration (`app/configs/`) currently uses compile-time config (`DefaultConfig`) specifying input JSON path, output .ics path, Google Places API key, and export toggles. A UI-based settings migration is planned but not yet implemented.

## Dependency Management

All versions are managed via the Gradle Version Catalog at `gradle/libs.versions.toml`. Add new dependencies there, not directly in `build.gradle.kts`.
