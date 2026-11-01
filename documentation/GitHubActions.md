# GitHub Actions

This project has [GitHub Actions](https://github.com/features/actions) workflows to validate our code for us automatically. The project currently uses two workflows.

## Build

The [Build](/.github/workflows/build.yml) workflow automates the core checks for the repository: compile, unit tests, and lint checks. This is set to run on every push.

Its `ios` job runs on macOS with the newest installed Xcode (60-minute timeout): every `commonTest` on the iOS simulator (`iosSimulatorArm64Test`), then `xcodebuild test` for `iosApp`, which builds the app and runs its Swift Testing tests. It picks the newest iOS runtime's first iPhone simulator and passes it to Gradle as `-PiosSimulatorDevice`.

## Danger Checks

The [Danger Checks](/.github/workflows/danger_checks.yml) is a separate workflow that is set to only run on pull request. This is because UI tests are slow and take up a lot of resources, so we only want to validate them when we're ready to merge changes into our base branch. 
