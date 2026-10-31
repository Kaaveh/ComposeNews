# Swift Interop

How SwiftUI talks to the shared ViewModels on iOS. Decided by the spike in #367 (D5 in #356); #369 builds the bridge on it.

## Decision

**SKIE 0.10.16** (Touchlab, Apache-2.0). #369 adds it to `gradle/libs.versions.toml` next to Kotlin and applies it only to the `shared` umbrella module, with analytics upload off:

```kotlin
plugins {
    alias(libs.plugins.skie)
}

skie {
    analytics { disableUpload.set(true) }
}
```

SKIE was the only candidate that passed every check on our code with no Kotlin changes: a typed `for await` over `state`, cancellation through `Task`, exhaustive `switch` over `LoadableData` and `Event`, and `ViewModelStore.clear()`.

## How it was tested

A throwaway branch (not merged) turned on the iOS targets, added `macosArm64`, and built four copies of one module. Each exported the real `MarketDetailViewModel`, wired to an in-memory repository, into a `Spike.framework`. A Swift harness per variant ran on macOS and was typechecked against the `iosSimulatorArm64` framework. Both targets go through the same Objective-C export.

Setup: Kotlin 2.4.21, AGP 9.3.3 with `com.android.kotlin.multiplatform.library`, KSP 2.3.12, Gradle 9.5.1 with the configuration cache on, Xcode 27.2, Swift 6 language mode, an M-series Mac mini. All dates are 2026.

## Results

| | No tool (baseline) | **SKIE 0.10.16** | KMP-NativeCoroutines 1.0.6 | Swift export (Alpha) |
|---|---|---|---|---|
| Builds on Kotlin 2.4.21 | Yes | Yes | Yes | No: its generated bridge fails to compile (below) |
| AGP 9 KMP plugin, configuration cache | Yes | Yes, no cache problems | Yes, no cache problems | Gradle side only works inside an Xcode build phase (direct integration) |
| `for await` over `state` | No: `watch { }` callback | `for await state in vm.state` | `for try await state in asyncSequence(for: vm.uiStateFlow)` | Docs: `asAsyncSequence()` |
| Cancelling the Swift `Task` stops collection | `close()` does (1 → 0 subscribers) | Yes (1 → 0) | Yes (1 → 0) | Not reached |
| `state` typed in Swift | No: `Any?`, needs `as!` | `SkieSwiftStateFlow<MarketDetailContractState>` | Only on annotated declarations | Not reached |
| Generic `UnidirectionalViewModel<EVENT, STATE>` | Erased: `state` is a raw `StateFlow`, `event(event: Any?)` | Erased: `SkieSwiftOptionalStateFlow<Any>` | Erased | Docs: generics erased to their bounds |
| `switch` over `LoadableData` / `Event` | `case is …`, needs `default` | `switch onEnum(of:)`, exhaustive | `case is …`, needs `default` | Docs: `sealedType()` since 2.4.20 |
| `ViewModelStore.clear()` runs `onCleared()` | Yes | Yes | Yes | Not reached |
| Swift-side dependency | None | None | `KMPNativeCoroutinesAsync` Swift package | None |
| Kotlin-side changes | `watch()` helper | None | `@NativeCoroutinesState` on every exposed flow | None |
| Debug link, `linkDebugFrameworkIosSimulatorArm64` (median of 3) | 2.9 s | 3.8 s | 2.7 s | n/a |
| Release link, `linkReleaseFrameworkIosSimulatorArm64` (one run) | 40.1 s | 42.2 s | 37.7 s | n/a |
| Release framework size | 6.6 MB | 8.6 MB (+2.0 MB, Swift module included) | 6.6 MB | n/a |
| Licence | n/a | Apache-2.0 | MIT | Apache-2.0 (Kotlin) |

`Flow<PagingData<T>>` wasn't tested. Markets no longer uses Paging (#389 RD6), so #370 isn't needed.

**Debugging.** In LLDB, a breakpoint set by Kotlin file and line (`MarketDetailViewModel.kt:92`) resolves to the Kotlin function in the debug framework, so Kotlin source maps are there. The spike didn't step through a live session. Readable Kotlin variables in Xcode need Touchlab's `xcode-kotlin` plugin, whichever tool we pick. That's from the plugin's docs; the spike didn't try it.

**Swift export failure.** The Swift export module compiled its own generated Kotlin bridge and failed on `MarketDetailContract.State(__market, __marketChart, __marketDetail, __chartRange)` with `actual type is 'LoadableData<Any?>', but 'LoadableData<MarketModel>' was expected`. It erases the generic arguments of a data class constructor and then can't call it, so any `State` built on `LoadableData<T>` is out until that's fixed.

## What #369 should do with it

- Expose the concrete ViewModel classes to Swift, not `UnidirectionalViewModel`. Only the concrete `state` comes through typed.
- `export(libs.lifecycle.viewmodel)` from `shared`, so Swift can own a `ViewModelStore` and call `clear()` when a screen goes away. That runs `onCleared()` and cancels `viewModelScope`.
- No hand-written Flow wrappers, `watch()` helpers or Combine adapters. Use `for await` on `state`, and `state.value` for the current value.
- Pattern-match sealed types with `switch onEnum(of: …)` and no `default`, so a new subtype breaks the Swift build.
- `viewModelScope` runs on `Dispatchers.Main`, the main queue, so collect inside a `@MainActor` task. In Swift 6 mode that compiled with no warnings.

## Kotlin version lag

SKIE hooks into the Kotlin/Native compiler, so each SKIE release supports a fixed list of Kotlin versions, and a Kotlin bump has to wait for SKIE. Recent history:

| Kotlin | Released | SKIE with support | Lag |
|---|---|---|---|
| 2.3.0 | 2025-12-16 | 0.10.9, 2026-01-06 | 21 days |
| 2.3.10 | 02-05 | 0.10.10, 02-09 | 4 days |
| 2.3.20 | 03-16 | 0.10.11, 04-02 | 17 days |
| 2.3.21 | 04-23 | 0.10.12, 05-18 | 25 days |
| 2.4.0 | 06-03 | 0.10.13, 06-24 | 21 days |
| 2.4.10 | 07-14 | 0.10.14, 07-27 | 13 days |
| 2.4.20 | 09-07 | 0.10.15, 09-25 | 18 days |
| 2.4.21 | 10-08 | 0.10.16, 10-29 | 21 days (touchlab/SKIE#211 merged 10-25; the first publish failed) |

Expect two to four weeks. Support for the last two Kotlin versions came from an outside contributor, with Touchlab reviewing and publishing.

KMP-NativeCoroutines also ships against specific Kotlin versions, but it publishes `-kotlin-X-RC` builds before a Kotlin release, so its lag is usually shorter.

## If SKIE falls behind

1. **Hold the Kotlin bump.** Treat Kotlin and SKIE as one upgrade: bump Kotlin only once a SKIE release lists that version. A lag of two to four weeks costs nothing, because nothing in the app needs a Kotlin patch on its release day.
2. **If SKIE stops shipping** (no release within about two months of a Kotlin minor), move to KMP-NativeCoroutines:
   - Add `@NativeCoroutinesState` wrappers in `shared`, as the spike did with an extension property, so the presentation modules stay free of it.
   - Add the `KMPNativeCoroutinesAsync` package to `iosApp`.
   - Swap `for await vm.state` for `asyncSequence(for: vm.stateFlow)` and `onEnum(of:)` for `case is` switches.

   Keeping state observation in one Swift helper in `iosApp` keeps that swap small.
3. **Check Swift export again** when it leaves Alpha. It's JetBrains' own long-term answer and would remove the third-party compiler plugin. Rerun this comparison once it builds `LoadableData<T>`-based state.
