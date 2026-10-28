# Spec: Gradle Cache Reduction — Align ComposeNews with XO

## Objective

`~/.gradle` keeps growing because every project on this machine pins its own Gradle, AGP, Kotlin and
KSP versions. Each distinct Gradle version adds a distribution (~130–300 MB) and its own
`caches/<version>` folder (`caches/9.5.1` is 5.6 GB today), and each distinct library version adds
another copy to `modules-2`.

ComposeNews is on a toolchain nothing else on the machine has cached (Gradle 9.2.1, Kotlin 2.3.10,
AGP 9.0.1). Building it as it is would download a new Gradle and start a new cache tree.

**Goal:** move ComposeNews onto the toolchain and library versions XO already uses (and Footballi
where XO doesn't have the library), so building ComposeNews reuses what is already in `~/.gradle`.
Then **delete the obsolete cached files**.

**User:** the maintainer, building ComposeNews, XO, Footballi and side projects on a 16 GB Mac mini
with limited disk and RAM.

**Scope:** ComposeNews only. XO, Footballi and the other projects are not edited.

## Assumptions

1. "Match XO" applies to the toolchain *and* to every library XO shares with ComposeNews.
2. Libraries XO doesn't use take Footballi's version **only if it is higher**. ComposeNews is never
   downgraded to match Footballi.
3. Libraries only ComposeNews uses (SQLDelight, Paging, Wear, Detekt, Kotlinter, Konsist, MockK,
   Chucker, Coil 2, etc.) keep their versions, except where the new Kotlin forces a bump.
4. Target SDK stays at 36. Compile SDK moves to 37 (XO's value, platform already installed):
   Compose BOM 2026.08.00 and OkHttp 5.5.0 (pulled in by Ktor 3.6.0) fail `checkAarMetadata` on 36.
   Approved by the maintainer on 2026-10-28.
5. The Gradle distribution comes from `services.gradle.org`, not XO's `nexus.oddrun.ir` mirror.

## Tech Stack (current → target)

### Toolchain

| Item | Catalog key / file | Current | Target | Source |
|---|---|---|---|---|
| Gradle | `gradle/wrapper/gradle-wrapper.properties` | 9.2.1 | **9.5.1** | XO (tooti-android uses the same services.gradle.org URL) |
| AGP | `androidGradlePlugin` | 9.0.1 | **9.3.3** | XO |
| AGP built-in Kotlin | `androidBuiltInKotlin` | 9.0.1 | **remove key, use `androidGradlePlugin`** | it's the same AGP artifact |
| Kotlin | `kotlin` | 2.3.10 | **2.4.21** | XO |
| KSP | `ksp` | 2.3.6 | **2.3.12** | XO |

### Shared libraries

| Library | Catalog key | Current | Target | Source |
|---|---|---|---|---|
| activity-compose | `composeActivity` | 1.12.4 | **1.13.0** | XO |
| Ktor (all modules) | `ktor` | 3.4.0 | **3.6.0** | XO |
| kotlinx-serialization-json | `kotlinx-serialization` | 1.10.0 | **1.11.0** | XO |
| kotlinx-coroutines-test | `coroutinesTest` | 1.10.2 | **1.11.0** | XO |
| Kotest (all modules) | `kotest` | 6.1.3 | **6.2.5** | XO |
| Koin (all modules) | `koin` | 4.0.4 | **4.2.2** | XO (koin-bom) |
| Compose BOM | `composeBOM` | 2026.02.00 | **2026.08.00** | Footballi |
| Navigation 3 | `nav3Core` | 1.0.1 | **1.1.1** | Footballi |
| OkHttp logging-interceptor | `loggingInterceptor` | 5.3.2 | **5.5.0** | Footballi |
| TV Material | `composeTvMaterial` (was `composeTv`) | 1.0.0 | **1.1.0** | Footballi (see Open Question 2) |
| TV Foundation | `composeTvFoundation` (was `composeTv`) | 1.0.0 | **1.0.0** | 1.1.0 doesn't exist |
| Robolectric | `robolectric` | 4.14.1 | **4.16.1** | Footballi |

### Unchanged on purpose (ComposeNews is already higher than Footballi; XO doesn't use them)

`ktx` 1.17.0, `lifecycle` 2.10.0, `workRuntimeKtx` 2.11.1, `espressoCore` 3.7.0, `junitExt` 1.3.0.
XO's lifecycle 2.11.0 is the JetBrains fork (`org.jetbrains.androidx.lifecycle`), a different
artifact, so matching it saves nothing.

### Cache state checked on 2026-10-28

- Already cached: AGP 9.3.3, Kotlin 2.4.21, KSP 2.3.12, activity 1.13.0, Ktor 3.6.0,
  serialization 1.11.0, coroutines-test 1.11.0, Kotest 6.2.5, koin-bom 4.2.2, OkHttp 5.5.0,
  tv-material 1.1.0, Robolectric 4.16.1. Gradle 9.5.1 from services.gradle.org is in
  `wrapper/dists/gradle-9.5.1-bin/iq79hdu3mqx29lgffhp8bfmx`.
- Not cached yet: Compose BOM 2026.08.00 and Navigation 3 1.1.1. Footballi declares them but hasn't
  resolved them here, so they download once and are then shared with Footballi.
- ComposeNews's current versions (AGP 9.0.1, Kotlin 2.3.10, KSP 2.3.6, BOM 2026.02.00, Koin 4.0.4,
  Ktor 3.4.0) are **not** in the cache, and `caches/9.2.1` doesn't exist.

## Commands

```bash
# 0. Before any change: no Gradle command and no Android Studio sync in ComposeNews (see Boundaries)

# 1. Wrapper bump: edit the URL by hand, then let the wrapper write the matching scripts/jar
#    (run once, after the edit, so it runs on 9.5.1 and never downloads 9.2.1)
./gradlew wrapper --gradle-version 9.5.1 --distribution-type bin

# 2. Verification (in this order)
./gradlew help                       # configuration + plugin resolution
./gradlew test                       # all unit tests (Kotest + Robolectric)
./gradlew lintKotlin                 # Kotlinter
./gradlew detektAll                  # Detekt
./gradlew assemble                   # every variant of app, app-tv, app-watch
./gradlew :konsist:test              # architecture rules

# 3. Confirm nothing new and unexpected landed in ~/.gradle
ls ~/.gradle/caches ~/.gradle/wrapper/dists
du -sh ~/.gradle/caches/* ~/.gradle/wrapper/dists/*

# 4. Free RAM afterwards (global rule)
./gradlew --stop
ps -axo pid,rss,command | grep -E 'GradleDaemon|kotlin-daemon|kotlin-build-tools' | grep -v grep
```

## Cache Deletion (REQUIRED)

**The obsolete cached files MUST be deleted.** The migration is not done until every "Delete" row
below has been removed from disk and the "After" check passes. Aligning versions only stops new
growth; it doesn't shrink `~/.gradle`.

### Delete

| # | Path | Why it is obsolete | Size |
|---|---|---|---|
| D1 | `~/.gradle/wrapper/dists/gradle-9.5.0-bin/68l0te977qub6k1hg50kck580u` | Duplicate 9.5.0 download. No project's `distributionUrl` hashes to it (Footballi, KomposerTemp and Standing all use `bvnork1r7n8i6kp5cnkibsc9q`). | ~130–300 MB |
| D2 | `~/.gradle/caches/9.2.1` | ComposeNews's old Gradle cache tree. Absent today; delete it if it appears before cleanup (for example after an accidental pre-migration sync). | 0 today |
| D3 | `~/.gradle/wrapper/dists/gradle-9.2.1-bin` | ComposeNews's old Gradle distribution. Same rule as D2. | 0 today |
| D4 | ComposeNews's old dependency versions under `~/.gradle/caches/modules-2/files-2.1/` (every "Current" version in the tables above, e.g. `com.android.tools.build/gradle/9.0.1`, `org.jetbrains.kotlin/*/2.3.10`, `com.google.devtools.ksp/*/2.3.6`, `androidx.compose/compose-bom/2026.02.00`, `io.insert-koin/*/4.0.4`, `io.ktor/*/3.4.0`, `io.kotest/*/6.1.3`) | Replaced by the target versions. Absent today; delete any that appear before cleanup. | 0 today |
| D5 | ComposeNews project-local caches: `.gradle/`, `.kotlin/`, every `build/` directory | Configuration cache and Kotlin incremental caches written by the old toolchain are invalid on 9.5.1 / 2.4.21. Absent today; delete any that exist before the first build on the new toolchain. | 0 today |

```bash
# D1
rm -rf ~/.gradle/wrapper/dists/gradle-9.5.0-bin/68l0te977qub6k1hg50kck580u

# D2, D3
rm -rf ~/.gradle/caches/9.2.1 ~/.gradle/wrapper/dists/gradle-9.2.1-bin

# D4 (prints first; delete only after reviewing the list)
M=~/.gradle/caches/modules-2/files-2.1
for p in com.android.tools.build/gradle/9.0.1 'org.jetbrains.kotlin/*/2.3.10' \
         'com.google.devtools.ksp/*/2.3.6' androidx.compose/compose-bom/2026.02.00 \
         'io.insert-koin/*/4.0.4' 'io.ktor/*/3.4.0' 'io.kotest/*/6.1.3' \
         'org.jetbrains.kotlinx/kotlinx-serialization-*/1.10.0' \
         androidx.activity/activity-compose/1.12.4 'androidx.navigation3/*/1.0.1' \
         com.squareup.okhttp3/logging-interceptor/5.3.2 'androidx.tv/tv-material/1.0.0' \
         'org.robolectric/*/4.14.1' org.jetbrains.kotlinx/kotlinx-coroutines-test/1.10.2; do
  ls -d $M/$p 2>/dev/null
done                                    # review, then re-run with: rm -rf $M/$p

# D5 (Gradle daemons stopped first)
cd ~/Project/Android/ComposeNews && ./gradlew --stop
rm -rf .gradle .kotlin && find . -type d -name build -not -path './.git/*' -prune -exec rm -rf {} +
```

**Kotlin 2.3.10 / AGP 9.0.1 / KSP 2.3.6 / BOM 2026.02.00 are also used by ComposeBreak and
AwesomeLineup.** D4 still deletes them; those projects download them again the next time they build.
That's expected, and it's the signal to align them in a later migration.

### Keep (still used by another project; deleting them only forces a re-download)

| Path | Used by |
|---|---|
| `caches/9.5.1`, `wrapper/dists/gradle-9.5.1-bin/iq79hdu3mqx29lgffhp8bfmx` | ComposeNews (after migration), tooti-android |
| `wrapper/dists/gradle-9.5.1-bin/ecnrzc7y1nxu7r9hwct0gfwia` | XO (nexus mirror URL) |
| `caches/9.5.0`, `wrapper/dists/gradle-9.5.0-bin/bvnork1r7n8i6kp5cnkibsc9q` | Footballi, KomposerTemp, KMP/Standing |
| `caches/9.7.1`, `wrapper/dists/gradle-9.7.1-bin` | chuck |
| `caches/modules-2` (everything outside D4), `jars-9`, `journal-1` | all projects |
| `caches/build-cache-1` (3.1 GB) | all projects; Gradle prunes it after 7 days (see Open Question 3) |

### After (all must be true)

- `ls ~/.gradle/wrapper/dists/gradle-9.5.0-bin` lists only `bvnork1r7n8i6kp5cnkibsc9q`.
- `~/.gradle/caches/9.2.1` and `~/.gradle/wrapper/dists/gradle-9.2.1-bin` don't exist.
- The D4 `ls` loop prints nothing.
- After the verification build, `~/.gradle/caches` has no new `<version>` folder and
  `~/.gradle/wrapper/dists` has no new distribution.

## Project Structure (files touched)

```
gradle/wrapper/gradle-wrapper.properties   → distributionUrl 9.2.1 → 9.5.1 (services.gradle.org)
gradle/wrapper/gradle-wrapper.jar          → regenerated by `./gradlew wrapper`
gradlew, gradlew.bat                       → regenerated by `./gradlew wrapper`
gradle/libs.versions.toml                  → [versions] bumps; drop androidBuiltInKotlin
gradle.properties                          → only if AGP 9.3 rejects a current opt-out flag
build.gradle.kts / module build files      → only for breaking API or DSL changes
source files                               → only for breaking API changes (Koin, Ktor, Nav3, Kotest)
SPEC.md                                    → this file
```

No new modules, no new dependencies, no build-logic changes.

## Code Style

Version catalog edits keep the existing key names and layout. Only the values change, except that
`androidBuiltInKotlin` is removed and its plugin points to the AGP version:

```toml
kotlin = "2.4.21"
ksp = "2.3.12"
androidGradlePlugin = "9.3.3"

# [plugins]
android-built-in1-kotlin = { id = "com.android.built-in-kotlin", version.ref = "androidGradlePlugin" }
```

```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-9.5.1-bin.zip
```

Any source fix that breaking changes force follows the existing MVI + Koin conventions in
CLAUDE.md and must pass `lintKotlin` and `detektAll`. No `@Suppress` added just to silence new
warnings without a comment saying why.

## Testing Strategy

No new tests: this is a toolchain migration. The existing suites are the regression net.

| Level | Command | Covers |
|---|---|---|
| Configuration | `./gradlew help` | plugin resolution, catalog, config-cache compatibility |
| Unit | `./gradlew test` | ViewModels, use cases, repositories (Kotest 6.2.5, MockK, Turbine, Robolectric 4.16.1) |
| Architecture | `./gradlew :konsist:test` | module and layer rules |
| Static | `./gradlew lintKotlin detektAll` | formatting and analysis on Kotlin 2.4.21 |
| Build | `./gradlew assemble` | app, app-tv, app-watch, baselineprofile |
| CI | `build.yml` on the PR | same pipeline on a clean runner |

The test count must not drop. A test that fails after the bump is fixed, not deleted or disabled.

## Boundaries

**Always**
- Change the wrapper URL **before** the first Gradle command or Android Studio sync, so 9.2.1 is
  never downloaded.
- Do every step on a branch off `kmp` and open a PR to `kmp`.
- Run the full verification sequence before committing.
- Delete the D1–D5 cached files and run the "After" check.
- Stop Gradle and Kotlin daemons when done.

**Ask first**
- Bumping any library not listed in the Tech Stack tables, including bumps Kotlin 2.4 forces on
  Detekt, Kotlinter or SQLDelight.
- Changing compileSdk or targetSdk.
- Changing `gradle.properties` opt-out flags (`android.builtInKotlin`, `android.newDsl`,
  `android.disallowKotlinSourceSets`).
- Deleting anything in `~/.gradle` that isn't in the Delete table.
- Editing CI workflows.

**Never**
- Edit XO, Footballi or any other project.
- Delete `caches/modules-2` wholesale, or any path in the Keep table.
- Downgrade a ComposeNews library to match Footballi.
- Use the `nexus.oddrun.ir` distribution URL.
- Delete or disable failing tests, or skip lint/detekt to make the build pass.

## Risks

| Risk | Mitigation |
|---|---|
| Detekt 1.23.8 was built against an older Kotlin and may fail on the Kotlin 2.4.21 classpath | Run `detektAll` early. If it fails, ask before pinning Detekt's Kotlin dependency or moving to Detekt 2.x. |
| Kotlinter 5.4.2 / SQLDelight 2.2.1 may not support Kotlin 2.4.x | Same: verify, then ask before bumping. |
| Koin 4.0 → 4.2 API changes (ViewModel DSL, compose / WorkManager integration) | Fix call sites, then `test` + `assemble`. |
| AGP 9.3 may warn about or drop the `gradle.properties` opt-outs | Read the AGP 9.3 release notes, ask before changing a flag. |
| `composeTv` drives both tv-material and tv-foundation; tv-foundation 1.1.0 may not exist | Open Question 2. |
| Compose BOM 2026.08.00 changes UI behaviour | Compose UI tests in `test`, plus a manual check of the list and detail screens. |

## Success Criteria

1. `gradle-wrapper.properties` points to `https\://services.gradle.org/distributions/gradle-9.5.1-bin.zip`.
2. `libs.versions.toml` matches every "Target" value above, and `androidBuiltInKotlin` is gone.
3. `./gradlew help test lintKotlin detektAll assemble` all succeed locally, and `build.yml` is green
   on the PR.
4. The test count is the same as before the migration.
5. Building ComposeNews created no new folder in `~/.gradle/caches` or `~/.gradle/wrapper/dists`.
   The only new `modules-2` entries are Compose BOM 2026.08.00 (and the Compose artifacts it pins)
   and Navigation 3 1.1.1.
6. **D1–D5 are deleted and every "After" check passes.**
7. No Gradle or Kotlin daemon from the migration is still running.

## Open Questions

1. **Koin target:** XO uses the Koin BOM. Keep per-module `version.ref = "koin"` at 4.2.2 (smallest
   diff), or switch ComposeNews to the BOM like XO? Default: keep version refs.
2. ~~**TV Foundation**~~ Resolved: `tv-foundation` tops out at 1.0.0, so `composeTv` is split into
   `composeTvMaterial = "1.1.0"` and `composeTvFoundation = "1.0.0"`.
3. **Build cache:** `caches/build-cache-1` is 3.1 GB. It's not version-specific so it isn't in the
   Delete list, but it is safe to delete (it only costs one slower build per project). Add it?
4. **Next migrations:** ComposeBreak, R and sdp-compose (Gradle 9.2.1) and AwesomeLineup
   (Kotlin 2.3.10, AGP 9.0.1) will rebuild the D2–D4 caches as soon as they build. Spec them next?
