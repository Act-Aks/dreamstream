# Changelog

All notable changes to DreamStream will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project intends to follow [Semantic Versioning](https://semver.org/spec/v2.0.0.html) once public releases begin.

## [Unreleased]

### Added

- **Search screen** (`:feature:search` — all three layers):
  - `:feature:search:domain` — `SearchRepository` contract, `SearchError` sealed interface (`EmptyQuery` | `NoResults` | `Unknown`).
  - `:feature:search:data` — `InMemorySearchRepository` performs case-insensitive keyword search across all 10 catalog items from the home stub, matching on title and genre; returns `SearchError.EmptyQuery` for blank input and `SearchError.NoResults` when no items match.
  - `:feature:search:presentation` — `SearchViewModel` (MVI with `SearchState`, `SearchAction`, `SearchEvent`); `SearchScreen` renders a glassmorphic search bar and a result grid using `GlassCard`; `SearchRoute` is the serializable type-safe nav route; Koin modules for data and presentation layers assembled in `:app:android`.
  - **39 unit tests** — `SearchViewModelTest` (9), `SearchMappingsTest` (18), `InMemorySearchRepositoryTest` (12).

- **Shared domain library** (`:core:model`):
  - `catalog/` subpackage — `Actor`, `ContentType` (with `displayName` singular label and `isEpisodic` flag), `Episode`, `Quality`, `Season`, `SubtitleFormat`, `ThemeMode`.
  - `detail/` subpackage — `ContentDetail` sealed hierarchy (`AnimeDetail`, `LiveDetail`, `MovieDetail`, `SeriesDetail`), `ShowStatus`, `displayRating` extension.
  - `media/` subpackage — `StreamLink`, `Subtitle`.
  - `plugin/` subpackage — `DreamError`, `InstalledPlugin`, `PluginManifest`, `PluginRepository`, `PluginStatus`, `RepositoryManifest`.
  - `search/` subpackage — `SearchResult` sealed hierarchy (`AnimeResult`, `LiveResult`, `MovieResult`, `SeriesResult`), `SearchResultExtensions` (`year`, `rating`, `displayRating`).
  - `:feature:home:domain` and `:feature:details:domain` now depend on `:core:model`; local type duplicates and private mapping helpers removed from all three mapper classes.

- **Details screen** (`:feature:details` — all three layers):
  - `:feature:details:domain` — `DetailContent` model (richer than the home list item: adds `synopsis`, `backdropUrl`, `genres: List<String>`, `durationMinutes: Int?`), `DetailMediaType` enum, `DetailsRepository` contract, `DetailsError` (`NotFound` | `LoadFailed`).
  - `:feature:details:data` — `InMemoryDetailsRepository` with full detail records for all 10 content IDs from the home stub (t1–t4, n1–n3, r1–r3); returns `DetailsError.NotFound` for unknown IDs.
  - `:feature:details:presentation` — `DetailsViewModel` reads `contentId` from `SavedStateHandle`, loads the detail record on init, sends `NavigateBack` event on back-click, and reloads on retry. `DetailsScreen` renders a glassmorphic hero surface, metadata row (year · rating · duration · type), `FlowRow` genre tags, synopsis card, and a play button placeholder. `DetailsRoute(contentId: String)` is the serializable Navigation3 `NavKey`.
  - `detailsDataModule` and `detailsPresentationModule` Koin modules assembled in `:app:android`.
  - **35 unit tests** — `DetailsViewModelTest` (6), `DetailsMappingsTest` (20 including duration formatting), `InMemoryDetailsRepositoryTest` (9).

- **Home slice unit tests** (`:feature:home` — all three layers):
  - `HomeViewModelTest` (6) — already present; now verifies init load, loading state, error event, error state shape, refresh, and content-click navigation.
  - `HomeMappingsTest` (17) — covers `Content.toContentUi()` field mapping, year/rating/null formatting, all five `ContentType` display names, `HomeSection.toHomeSectionUi()` id/title/items/order, and both `HomeError.toUiText()` variants.
  - `InMemoryHomeRepositoryTest` (11) — verifies `Result.Success`, three sections with correct IDs/titles/item counts, item integrity (non-empty id and title), and stable successive calls.
  - **34 unit tests** total for the home slice.

- **Navigation wired — home → details → back**:
  - `AppNavigation` now pushes `DetailsRoute(contentId)` onto the back stack when a content card is tapped on the home screen, and pops it on back-press or back-click inside the details screen.

- **Glassmorphic design system** (`core:design-system`):
  - Vibrant dark color palette — electric violet `#A855F7` (primary), electric cyan `#06B6D4` (secondary), hot pink `#F472B6` (tertiary), deep-space dark surfaces with cool undertone.
  - Glass token system — `GlassStyle` data class with five named presets (`ultraThin`, `thin`, `regular`, `thick`, `ultraThick`) encoding blur radius, background alpha, border alpha/width, primary tint, and noise factor. `GlassStyle.toHazeStyle()` converts to a Haze `HazeStyle`.
  - `DreamStreamGradients` object — `contentScrim`, `brandPrimary` (violet→cyan), `brandAccent` (violet→pink), `brandTricolor`, `shimmer`, `cardGlow` gradient brushes.
  - `GradientBackground` composable — full-screen ambient backdrop with three radial glow blobs (purple upper-left, cyan upper-right, pink lower-right) painted via `Canvas`; registers as a Haze `hazeSource` when a `HazeState` is provided.
  - `GlassCard` composable — clickable glassmorphic card using `hazeChild` with transparent fill and a white rim-light border.
  - `GlassSurface` composable — non-clickable glass container with the same frosted appearance.
  - `GlassTopBar` composable — Material3 `TopAppBar` with `hazeChild` and transparent background; blurs scroll content beneath it; supports `navigationIcon` and `actions` slots.
  - `haze:1.7.2` wired as `api` dependency so `HazeState` is available to all consuming feature modules transitively.
  - More-rounded shape ramp: `extraSmall 8dp` → `extraLarge 32dp`.

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
- **120 unit tests** pass across the project (5 core/domain + 38 home + 38 details + 39 search).
- DreamStream now has three complete vertical slices: home, details, and search.
