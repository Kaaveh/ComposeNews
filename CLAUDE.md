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
| `app-tv` | Android TV app |
| `feature:marketlist:presentation` | Market list ViewModel, contract, Koin module (KMP, no Compose) |
| `feature:marketlist:ui` | Paginated market list screen (Compose) |
| `feature:marketdetail:presentation` | Market detail ViewModel, contract, Koin module (KMP, no Compose) |
| `feature:marketdetail:ui` | Market detail screen (Compose, Coil) |
| `domain:market` | Business logic and use cases |
| `data:market-repository` | Repository implementations (Koin DI) |
| `data:market-remote` | Remote data source (Ktor) |
| `data:market-local` | Local data source (Room KMP) |
| `core:base` | Base ViewModels, `LoadableData`, dispatchers (KMP) |
| `core:sync` | Data sync: common `SyncScheduler`, WorkManager on Android (KMP) |
| `core:network:ktor` | Ktor HTTP client configuration |
| `core:uimarket` | `MarketModel` UI model and its mappers (KMP) |
| `core:ui` | Android Compose helpers: `use()`, `LoadableComponent`, `MarketModelSaver`, preview data |
| `core:extensions` | Kotlin extension functions |
| `core:testing` | Shared test fixtures and Kotest (KMP) |
| `library:designsystem` | Compose design system / theming |
| `library:navigation` | Navigation utilities |
| `konsist` | Architecture consistency (Konsist) tests |

KMP modules keep shared code in `commonMain` and Android-only code (`Context`, WorkManager, Room's Android driver) in `androidMain`; tests live in `commonTest`, or `androidHostTest` when they need the JVM. Konsist fails the build if any `commonMain` imports `androidx.compose.*` or `android.*`. `app`, `app-watch`, `app-tv`, the `feature:*:ui` modules, `core:ui` and `library:*` stay Android-only.

## Build System

Dependencies are managed via the **version catalog** at `gradle/libs.versions.toml`.

Module scripts apply a convention plugin from `build-logic/` (SDK levels, Java 17, test options, packaging, detekt, kotlinter), so they only hold their namespace and dependencies:
`composenews.android.application`, `composenews.android.library`, `composenews.android.library.compose`, `composenews.kmp.library` (KMP, Android target; iOS behind `composenews.ios`), `composenews.kmp.room` (KSP + Room). `baselineprofile` (`com.android.test`) is the one module without one.

```bash
# Build
./gradlew assemble
./gradlew assembleRelease

# Compose compiler metrics (for release optimization analysis)
./gradlew assembleRelease -PenableComposeCompilerMetrics=true -PenableComposeCompilerReports=true
```

## Dependency Injection

**Koin 4.x** (migrated from Dagger-Hilt). Key points:
- Initialized in `ComposeNewsApplication.kt` via `startKoin { }`, which then calls `SyncScheduler.schedule()`
- Shared modules expose a common Koin module, plus an Android one where they need `Context` (`localDatasourceAndroidModule`, `syncModule`). Each app adds the Android modules and its own `HttpClientEngine`
- WorkManager integration uses `koin-workmanager` — the `WorkerFactory` is registered in the manifest with `tools:node="remove"` to opt out of the default factory
- Compose integration via `koin-androidx-compose`
- Prefer `single` for stateless services/repos; prefer `factory` for ViewModels and scoped objects

## Architecture

- **MVI pattern**: UI emits Intents → ViewModel reduces to State via Flows
- **Repository pattern**: data layer abstracts remote + local sources
- **Reactive**: Kotlin Coroutine Flows throughout (no LiveData)
- **Pagination**: Paging 3 library in list features
- **Database**: Room KMP in `data:market-local`, on the system SQLite (`AndroidSQLiteDriver`) on Android (iOS will use `BundledSQLiteDriver`, #368); Android copies the legacy SQLDelight `MarketDatabase` file into `composenews.db` on first open (#360)
- **HTTP**: Ktor client (KMP-ready, replacing Retrofit)

## Testing

- **Framework**: Kotest 6.x (replacing JUnit)
- **Mocking**: MockK
- **Flow testing**: Turbine
- **Android integration**: Robolectric + `includeAndroidResources = true`. Robolectric tests stay on JUnit 4 (run by the vintage engine), since Kotest has no Robolectric runner here

```bash
./gradlew test                  # all unit tests
./gradlew :module:test          # single module
./gradlew :konsist:test         # architecture rules
```

KMP modules have no `test` task of their own; the KMP convention plugin registers one that runs the Android host tests (`testAndroidHostTest`), so the commands above cover them.

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
- Database (Room KMP, replacing SQLDelight: Room is multiplatform and stable now, so the earlier Room → SQLDelight move was reversed)
- DI (Koin)
- Testing (Kotest)

When adding new dependencies, prefer KMP-compatible alternatives.
