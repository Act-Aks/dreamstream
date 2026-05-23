# Changelog

All notable changes to DreamStream will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project intends to follow [Semantic Versioning](https://semver.org/spec/v2.0.0.html) once public releases begin.

## [Unreleased]

### Added

- **Glassmorphic design system** (`core:design-system`):
  - Vibrant dark color palette — electric violet `#A855F7` (primary), electric cyan `#06B6D4` (secondary), hot pink `#F472B6` (tertiary), deep-space dark surfaces with cool undertone.
  - Glass token system — `GlassStyle` data class with five named presets (`ultraThin`, `thin`, `regular`, `thick`, `ultraThick`) encoding blur radius, background alpha, border alpha/width, primary tint, and noise factor. `GlassStyle.toHazeStyle()` converts to a Haze `HazeStyle`.
  - `DreamStreamGradients` object — `contentScrim`, `brandPrimary` (violet→cyan), `brandAccent` (violet→pink), `brandTricolor`, `shimmer`, `cardGlow` gradient brushes.
  - `GradientBackground` composable — full-screen ambient backdrop with three radial glow blobs (purple upper-left, cyan upper-right, pink lower-right) painted via `Canvas`; registers as a Haze `hazeSource` when a `HazeState` is provided.
  - `GlassCard` composable — clickable glassmorphic card using `hazeChild` with transparent fill and a white rim-light border.
  - `GlassSurface` composable — non-clickable glass container with the same frosted appearance.
  - `GlassTopBar` composable — Material3 `TopAppBar` with `hazeChild` and transparent background; blurs scroll content beneath it.
  - `haze:1.7.2` wired as `api` dependency so `HazeState` is available to all consuming feature modules transitively.
  - More-rounded shape ramp: `extraSmall 8dp` → `extraLarge 32dp`.

- **Home screen glassmorphic UI** (`:feature:home:presentation`):
  - `HomeScreen` updated to use `GradientBackground` as the full-screen `hazeSource`, `GlassTopBar` instead of a plain `TopAppBar`, and `GlassCard` for every content card in the horizontal section rows.
  - Three `@Preview` composables added (loading, empty, and content states) with realistic sample data.

- **Foundation modules** (completed in earlier sessions):
  - Apache-2.0 project licensing with `DreamStream Contributors` as the copyright holder.
  - Community documentation for contributing, Code of Conduct enforcement, and security vulnerability reporting.
  - GitHub issue forms, pull request template, CI workflow, and Dependabot configuration.
  - EditorConfig defaults for consistent formatting across Kotlin, Gradle, Markdown, YAML, TOML, XML, and properties files.
  - `:build-logic` — 13 convention plugins with full version-catalog-backed dependency management.
  - `:core:domain` — `Result<D,E>`, `EmptyResult`, `DataError`, result extension helpers; unit tests passing.
  - `:core:presentation` — `UiText`, `ObserveAsEvents`, `DataErrorUiText.toUiText()`.
  - `:feature:home` full vertical slice — domain model, stub repository, MVI ViewModel, Koin module, navigation route, and screen composable wired into `:app:android`.

### Notes

- DreamStream is in active feature development. Versioned releases have not started yet.
- The debug APK for `:app:android` builds and runs. Glassmorphic blur is hardware-accelerated on Android 12+ (API 31+); older devices render a frosted tint fallback via Haze.
- Unit tests for `HomeViewModel` and `:feature:home:domain` are the next planned addition before expanding to a second feature slice.
