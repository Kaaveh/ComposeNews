# ComposeNews — Claude Code Guide

## Project Overview

ComposeNews is a production-ready Android crypto application demonstrating modern Android development best practices. It follows **MVI + Clean Architecture** with a multi-module structure organized by feature and layer. The project is actively migrating toward **Kotlin Multiplatform (KMP)**.

- **Min SDK**: 23 (Android 6.0) | **Target SDK**: 36 (Android 15)
- **Language**: Kotlin 2.x | **UI**: Jetpack Compose
- **Java toolchain**: 17+

## Module Structure

| Module | Purpose |
|--------|---------|
| `app` | Main Android application entry point |
| `app-watch` | Wear OS companion app |
| `feature:marketlist` | Paginated market list screen |
| `feature:marketdetail` | Market detail screen |
| `domain:market` | Business logic and use cases |
| `data:market-repository` | Repository implementations (Koin DI) |
| `data:market-remote` | Remote data source (Ktor) |
| `data:market-local` | Local data source (SQLDelight) |
| `data:sqldelight` | SQLDelight database setup |
| `core:base` | Base ViewModels and shared utilities |
| `core:sync` | WorkManager-based data sync |
| `core:network:ktor` | Ktor HTTP client configuration |
| `core:uimarket` | Shared Compose UI components |
| `core:extensions` | Kotlin extension functions |
| `core:test` | Shared testing utilities |
| `library:designsystem` | Compose design system / theming |
| `library:navigation` | Navigation utilities |
| `konsist` | Architecture consistency (Konsist) tests |

## Build System

Dependencies are managed via the **version catalog** at `gradle/libs.versions.toml`.

```bash
# Build
./gradlew assemble
./gradlew assembleRelease

# Compose compiler metrics (for release optimization analysis)
./gradlew assembleRelease -PenableComposeCompilerMetrics=true -PenableComposeCompilerReports=true
```

## Dependency Injection

**Koin 4.x** (migrated from Dagger-Hilt). Key points:
- Initialized in `ComposeNewsApplication.kt` via `startKoin { }`
- WorkManager integration uses `koin-workmanager` — the `WorkerFactory` is registered in the manifest with `tools:node="remove"` to opt out of the default factory
- Compose integration via `koin-androidx-compose`
- Prefer `single` for stateless services/repos; prefer `factory` for ViewModels and scoped objects

## Architecture

- **MVI pattern**: UI emits Intents → ViewModel reduces to State via Flows
- **Repository pattern**: data layer abstracts remote + local sources
- **Reactive**: Kotlin Coroutine Flows throughout (no LiveData)
- **Pagination**: Paging 3 library in list features
- **Database**: SQLDelight (KMP-ready, replacing Room)
- **HTTP**: Ktor client (KMP-ready, replacing Retrofit)

## Testing

- **Framework**: Kotest 6.x (replacing JUnit)
- **Mocking**: MockK
- **Flow testing**: Turbine
- **Android integration**: Robolectric + `includeAndroidResources = true`

```bash
./gradlew test                  # all unit tests
./gradlew :module:test          # single module
```

## Code Quality

```bash
./gradlew lintKotlin            # Kotlinter formatting check
./gradlew formatKotlin          # auto-format
./gradlew detektAll             # static analysis (runs in parallel)
```

Git hooks enforce formatting and analysis before commits/pushes:

```bash
./gradlew installGitHooks       # one-time setup
```

## CI / GitHub Actions

| Workflow | Trigger | Steps |
|----------|---------|-------|
| `build.yml` | Push to master, PRs, version tags | test → lint → detekt → assemble APK |
| `claude.yml` | `@claude` mention in issues/PRs | Claude Code action |
| `claude-code-review.yml` | PR opened/updated | Automated code review |
| `danger_checks.yml` | PRs targeting `kmp` or `android_version` | Danger Kotlin PR checks |

## KMP Migration Status

The following have already been migrated to KMP-compatible libraries:
- Serialization (kotlinx.serialization)
- HTTP (Ktor)
- Database (SQLDelight)
- DI (Koin)
- Testing (Kotest)

When adding new dependencies, prefer KMP-compatible alternatives.
