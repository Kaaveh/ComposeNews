# GitHub Actions

This project has [GitHub Actions](https://github.com/features/actions) workflows to validate our code for us automatically. The project currently uses two workflows.

## Build

The [Build](/.github/workflows/build.yml) workflow automates the core checks for the repository: compile, unit tests, lint checks. This is set to run on every push.

## Danger Checks

The [Danger Checks](/.github/workflows/danger_checks.yml) is a separate workflow that is set to only run on pull request. This is because UI tests are slow and take up a lot of resources, so we only want to validate them when we're ready to merge changes into our base branch. 
