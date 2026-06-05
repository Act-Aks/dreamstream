# DreamStream Agent Instructions

## Project Identity

DreamStream is a modern Kotlin Multiplatform multimedia app for discovering and streaming entertainment content through a native, polished, ad-free interface.

The product direction is inspired by the general idea of giving users a better native media experience, but it is not a CloudStream clone. Do not copy CloudStream architecture, code, naming, branding, provider APIs, or UI patterns unless the user explicitly supplies compatible source code and licensing context.

DreamStream should be designed as a long-lived, modular KMP application that can support multiple content sources, rich media discovery, playback, offline/cache-aware flows, and high-quality Android-first Compose UI while remaining portable to future Kotlin Multiplatform targets.

## Current State

The following modules and infrastructure are complete and compiled:

- `:build-logic` — 13 convention plugins (android app, tv, desktop, compose, domain, kmp library, feature, koin, ktor, room, serialization, testing, detekt) with full support utilities; **build-health audited and fixed**:
  - `Compose.kt`: removed duplicate `androidx.lifecycle` bundle (already transitive via CMP); `compose-components-resources` promoted to `api`
  - `DomainModuleConventionPlugin.kt`: removed unused `kermit` and `kotlinx-datetime`; `kotlinx-coroutines-core` promoted to `api`
  - `SerializationConventionPlugin.kt`: `kotlinx-serialization-core` promoted to `api`
  - `FeatureConventionPlugin.kt`: `core:presentation` promoted to `api` via new `asApi` param on `addProjectIfPresent`
  - Root `build.gradle.kts`: `subprojects { resolutionStrategy }` pins all `androidx.lifecycle` artifacts to `2.10.0` to prevent Koin pulling `2.11.0-beta01` and causing a `LocalViewModelStoreOwner` classpath conflict
  - **DAGP** (`com.autonomousapps.dependency-analysis`) configured in root build; `assertModuleGraph` passes all architectural rules (max height, no cross-feature deps); `buildHealth` runs at `warn` severity with 894 lines of non-fatal findings; `docs/module-graph.md` auto-generated (Mermaid diagram)
- `:core:domain` — core primitives, domain models, repository contracts, and KMP extension helpers:
  - `Error`, `Result<D,E>`, `EmptyResult`, `DataError` (Network + Local + Plugin), `DreamLogger`; tests passing
  - `DataError.Plugin` sealed enum: `CLASS_NOT_FOUND`, `INCOMPATIBLE_VERSION`, `INVALID_MANIFEST`, `LOAD_FAILED`, `SIGNATURE_INVALID`, `UNKNOWN`
  - `DataError.Local` gains `CORRUPTED` and `PERMISSION_DENIED`
  - `DispatcherProvider` interface + `DefaultDispatcherProvider` for injecting coroutine dispatchers
  - Extension helpers in `extensions/` package: `FlowExtensions` (asResult, onSuccess, onFailure, mapResult), `ListExtensions` (chunkedOrEmpty, distinctByKey, safeSubList, indexOfFirstOrNull), `NumberExtensions` (toReadableDuration, toReadableMinutes, toReadableFileSize), `StringExtensions` (URL/Base64/HTML utilities), `DreamLoggerExtensions` (info, warn, error shorthand)
  - `ResultExtensions` moved from `util/` to `extensions/` package
  - `LoggerFactory` interface for DI-friendly logger construction (implemented by `KermitDreamLoggerFactory` in `:core:data`)
  - **Domain models** (package `model/`) — all shared types previously in `:core:model`, now consolidated here:
    - `catalog/` — `Actor`, `ContentType`, `Episode`, `Quality`, `Season`, `SubtitleFormat`, `ThemeMode`, `CatalogRequest`, `CatalogResponse`, `CatalogSection` (the last three moved from `:core:plugin-api`)
    - `detail/` — `ContentDetail` sealed hierarchy (`AnimeDetail`, `LiveDetail`, `MovieDetail`, `SeriesDetail`), `ShowStatus`
    - `filter/` — `FilterOption`, `BooleanFilter`, `SingleSelectFilter`, `TextSearchFilter`
    - `media/` — `StreamLink`, `Subtitle`
    - `plugin/` — `InstalledPlugin`, `PluginManifest`, `PluginRepository`, `PluginStatus`, `RepositoryManifest`
    - `search/` — `SearchResult` sealed hierarchy (`AnimeResult`, `LiveResult`, `MovieResult`, `SeriesResult`), `SearchResultExtensions`
    - `user/` — `Bookmark`, `BookmarkCategory`, `UserPreferences`, `WatchHistory`
  - **Repository contracts** (package `repository/`) — `ContentRepository`, `BookmarkRepository`, `PluginRepository` (domain-level, not to be confused with the DB DAO), `PreferencesRepository`, `WatchHistoryRepository`, `StreamResult`
  - **System interfaces** (package `system/`) — `AppStorageProvider`, `PlatformInfo`, `TimeProvider`, `UuidProvider`
- `:core:presentation` — `UiText`, `ObserveAsEvents`, `DataErrorUiText.toUiText()`:
  - All 24 `DataError` variants (Network + Local + Plugin) mapped to localized `StringResourceId`
  - Display name helpers: `ContentTypeDisplayName`, `EpisodeDisplayName`, `SeasonDisplayName`, `ShowStatusDisplayName`, `StreamLinkDisplayName`, `SubtitleDisplayName`, `PluginRepositoryDisplayName` — bridge `:core:domain` model types to localized `UiText`
  - `NavigationItem(icon, label)` data class for design-system navigation bar
  - `AppRoute` sealed hierarchy for top-level navigation type safety
  - **4 locales expanded** — English, German (`values-de`), Hindi (`values-hi`), Japanese (`values-ja`) now include plugin error messages, content-type labels, episode/season formats, and show-status strings
- `:core:design-system` — full glassmorphic design system:
  - **Color tokens** — vibrant electric palette: `Primary` violet `#A855F7`, `Secondary` cyan `#06B6D4`, `Tertiary` pink `#F472B6`; deep-space dark surfaces; ambient glow sources for gradient backdrops; glass tint tokens
  - **Shape tokens** — more-rounded ramp: `extraSmall 8dp` → `extraLarge 32dp`
  - **Glass token system** — `GlassStyle` data class with five named presets (`GlassDefaults.ultraThin` → `ultraThick`); `toHazeStyle()` converts to a Haze `HazeStyle` mixing white frosted tint + brand-violet tint
  - **Gradient brushes** — `DreamStreamGradients`: `contentScrim`, `brandPrimary`, `brandAccent`, `brandTricolor`, `shimmer`, `cardGlow`
  - **Components** (package `designsystem.components`) — `GradientBackground`, `GlassCard`, `GlassSurface`, `GlassTopBar`, `GlassNavigationBar` (full-width bottom nav driven by `NavigationItem` list with HazeState blur), `GlassNavigationBarItem`
  - **Blur engine** — `haze:1.7.2` declared as `api` dependency; `HazeState` is available to all feature modules via transitive resolution without re-declaring the dependency
- `:core:data` — KMP data layer providing repository and system implementations:
  - `UserPreferencesDataSource` + `UserPreferencesDataSourceImpl` — reads/writes `UserPreferences` via DataStore
  - `UserPreferencesKeys` — type-safe DataStore preference keys
  - `ContentRepositoryImpl` — coordinates plugin providers via `PluginRegistry` to serve catalog, search, detail, and stream links
  - `BookmarkRepositoryImpl` — delegates to `:core:database` bookmark DAO
  - `WatchHistoryRepositoryImpl` — delegates to `:core:database` watch-history DAO
  - `PluginRepositoryImpl` — wraps `PluginManager` to expose install/uninstall/list operations
  - `PreferencesRepositoryImpl` — reads/writes all user preferences via `UserPreferencesDataSource`
  - `KermitDreamLoggerFactory` — implements `LoggerFactory` using Kermit
  - System impls: `AppStorageProviderImpl`, `PlatformInfoImpl`, `TimeProviderImpl`, `UuidProviderImpl`
  - `CoreDataModule` + `CoreDataPreferencesModule` — Koin modules
- `:core:plugin-api` — KMP module providing the plugin integration surface:
  - `DreamPlugin` / `PluginMetadata` / `PluginApiVersion` / `PluginContext` — plugin lifecycle contract
  - `ContentProvider` interface — catalog sections, search, detail, and stream resolution contracts using `Result<T, DataError>`
  - `ProviderType` enum (ANIME, MOVIE, TV, LIVE, MUSIC) and `VpnStatus` model
  - API models: `ApiContentDetail` sealed hierarchy, `ApiSearchResult` sealed hierarchy (catalog request/response types moved to `:core:domain`)
  - `Extractor` and `ApiMapper` interfaces for HTML/JSON source integration
  - Depends on `ktor-client-core`, `kotlinx-serialization-json`, `kotlinx-datetime`, `ksoup`; exposes `:core:domain` as `api`
- `:core:plugin-loader` — KMP module providing full plugin lifecycle management (replaces `:core:plugin-runtime`):
  - `PluginManager` — install/uninstall repos, fetch repo manifests via Ktor, manage plugin lifecycle with `StateFlow` of installed plugins
  - `PluginInstaller` — download and store plugin JARs to okio `Path` storage
  - `PluginRegistry` — in-memory registry of loaded `DreamPlugin` instances, maps provider ID to `ContentProvider`
  - `PluginVerifier` — manifest validation and API version compatibility checks
  - `PluginContextFactory` — creates `PluginContext` with injected Ktor client and logger
  - `PluginFileManifest` — on-disk manifest format for installed plugins
  - `PluginConstants` — shared path/filename constants
  - `PluginClassLoader` (expect/actual) — Android `DexClassLoader` / JVM `URLClassLoader`
- `:feature:home` — first vertical slice, fully tested and wired into `:app:android`:
  - `:feature:home:domain` — `Content` domain model, `HomeRepository` contract, `HomeError`; depends on `:core:domain` for shared types
  - `:feature:home:data` — `InMemoryHomeRepository` (hardcoded stub returning 3 sections, 10 items)
  - `:feature:home:presentation` — `HomeViewModel` (MVI with `HomeState`, `HomeAction`, `HomeEvent`), `HomeScreen` using `GradientBackground` + `GlassTopBar` + `GlassCard`; `ContentUi.typeName` is `UiText` (localized via `ContentTypeDisplayName`); Koin module; `HomeRoute`
  - **Tests** — 38 passing: `HomeViewModelTest` (7), `HomeMappingsTest` (20), `InMemoryHomeRepositoryTest` (11)
- `:feature:details` — second vertical slice, fully tested and wired into `:app:android`:
  - `:feature:details:domain` — `DetailContent` model (richer than the home list item — adds `synopsis`, `backdropUrl`, `genres`, `durationMinutes`), `DetailMediaType`, `DetailsRepository` contract, `DetailsError`; depends on `:core:domain` for shared types
  - `:feature:details:data` — `InMemoryDetailsRepository` (full detail records for all 10 catalog IDs from the home stub; returns `DetailsError.NotFound` for unknown IDs)
  - `:feature:details:presentation` — `DetailsViewModel` (reads `contentId` from `SavedStateHandle`; `OnBackClick` → `NavigateBack` event; `OnRetry` → reload), `DetailsScreen` (glassmorphic hero card, metadata + genre tags, synopsis, play button placeholder); `DetailContentUi.typeName` is `UiText`; Koin module
  - **Tests** — 38 passing: `DetailsViewModelTest` (6), `DetailsMappingsTest` (23), `InMemoryDetailsRepositoryTest` (9)
- `:feature:search` — third vertical slice, fully tested and wired into `:app:android`:
  - `:feature:search:domain` — `SearchRepository` contract, `SearchError` (`EmptyQuery` | `NoResults` | `Unknown`)
  - `:feature:search:data` — `InMemorySearchRepository` (case-insensitive keyword search across all 10 catalog items, matching on title and genre)
  - `:feature:search:presentation` — `SearchViewModel` (MVI with `SearchState`, `SearchAction`, `SearchEvent`), `SearchScreen` (glassmorphic search bar + result grid using `GlassCard`), `SearchRoute`, Koin modules
  - **Tests** — 39 passing: `SearchViewModelTest` (9), `SearchMappingsTest` (18), `InMemorySearchRepositoryTest` (12)
- `:feature:settings` — fourth feature, wired into `:app:android`:
  - `:feature:settings:domain` — `AppLanguage` sealed class (6 languages: English, German, Hindi, Japanese, Taiwanese Mandarin, System Default), `SettingsRepository` contract (language + dark mode + notifications)
  - `:feature:settings:data` — KMP-split: `commonMain` Koin module; `androidMain` → `AndroidSettingsRepository` (AppCompatDelegate locale + night mode); `desktopMain` → `DesktopSettingsRepository` stub
  - `:feature:settings:presentation` — `SettingsViewModel` (MVI with `SettingsState`, `SettingsAction`), `SettingsScreen` (language picker with check indicators), `SettingsRoute`, Koin module
  - **Tests** — none yet (requires Android instrumentation)
- **Localization infrastructure**:
  - `LocalizationConventionPlugin` — registers Compose Multiplatform resource source sets and generates `LocaleConfig` for Android
  - `LocalizationValidationTask` — Gradle task validating every feature's `composeResources/values/strings.xml` against a required-keys schema
  - **4 locales** — English, German (`values-de`), Hindi (`values-hi`), Japanese (`values-ja`) across `:core:presentation` and all feature presentation modules
- `:app:shared` — KMP module hosting shared app wiring:
  - `App.kt` — shared Compose entry point wrapping `DreamStreamTheme` + `AppNavigation`
  - `di/InitKoin.kt` + `di/AppModule.kt` — platform-agnostic Koin startup helpers
  - `navigation/AppNavigation.kt` — Navigation3 `NavDisplay` inside a `Box` with an animated `GlassNavigationBar` that slides in/out based on back-stack depth (visible only at tab roots)
  - `navigation/BottomTab.kt` — enum mapping each tab (Home, Search, Settings) to its Navigation3 route, Material icon, and label; `switchTab` clears the back stack before pushing the new root
- `:app:android` — `MainActivity` delegates to `App()` from `:app:shared`; `DreamStreamApplication` initialises Koin via `initKoin`; debug APK builds and runs

**121 unit tests pass** across the project (5 core/domain + 1 core/presentation + 38 home + 38 details + 39 search).

All foundation steps and the first four feature slices are complete. The project has a glassmorphic bottom navigation bar, a KMP-shared app module, a full plugin-loader infrastructure, rich domain models consolidated into `:core:domain`, and a complete KMP data layer with repository implementations. The next areas to explore are: real data integration (connecting a live content source via `ContentRepositoryImpl` and a plugin), Room persistence testing, or a catalog/browsing feature.

## Architecture Source Of Truth

Follow the repo-local skills under `.opencode/skills/` whenever implementation touches their area:

- `.opencode/skills/android-module-structure/SKILL.md`
- `.opencode/skills/android-data-layer/SKILL.md`
- `.opencode/skills/android-error-handling/SKILL.md`
- `.opencode/skills/android-presentation-mvi/SKILL.md`
- `.opencode/skills/android-compose-ui/SKILL.md`
- `.opencode/skills/android-navigation/SKILL.md`
- `.opencode/skills/android-di-koin/SKILL.md`
- `.opencode/skills/android-testing/SKILL.md`
- `.opencode/skills/git-commit-workflow/SKILL.md`

If this file and a skill appear to disagree, prefer the skill for detailed implementation mechanics and this file for DreamStream-specific product and domain rules.

## Non-Negotiable Principles

- Keep changes small, correct, and idiomatic.
- Prefer Kotlin Multiplatform-compatible code unless a module is intentionally Android-only.
- Use feature-layered modularization: split by feature first, then by layer.
- Use Clean Architecture boundaries: `presentation -> domain <- data`.
- Keep domain models pure Kotlin with no Android, Ktor, Room, Compose, or framework imports.
- Features must not depend on other features directly.
- Shared behavior used by multiple features belongs in the appropriate `core` module.
- Do not add compatibility abstractions, wrappers, or indirection without a concrete need.
- Use typed errors and `Result<T, E>` for expected failures. Do not throw exceptions for normal app failures.
- Prioritize user privacy, predictable behavior, and maintainability over clever shortcuts.

## Expected Module Structure

Use this structure as the target architecture once implementation begins:

```text
:app:android
:app:shared
:build-logic
:core:domain
:core:data
:core:presentation
:core:design-system
:core:plugin-api
:core:plugin-loader
:core:database        optional when Room is introduced
:core:media           optional for playback abstractions and media session logic
:core:network         optional only if networking grows beyond core:data
:feature:<name>:domain
:feature:<name>:data
:feature:<name>:presentation
```

Use feature modules for product areas such as `home`, `search`, `catalog`, `details`, `player`, `downloads`, `bookmarks`, `settings`, and `source` only when those features are actually being implemented.

Do not create a module for a single trivial class. Start in the nearest existing module and extract only when the API surface becomes shared or complex.

## Gradle And Build Rules

- Use Kotlin DSL for Gradle files.
- Use `gradle/libs.versions.toml` for all dependency and plugin versions.
- Do not hardcode dependency versions in module build files.
- Prefer convention plugins in `:build-logic` for repeated Gradle configuration.
- Keep build files declarative and minimal.
- Preserve Gradle configuration cache compatibility where practical.
- Do not add dependencies casually. Pick stable, widely used libraries that match the skills.

Preferred core libraries:

- DI: Koin
- Networking: Ktor Client
- Serialization: KotlinX Serialization
- Local database: Room
- Preferences: DataStore
- UI: Compose Multiplatform / AndroidX Compose as appropriate
- Navigation: Type-safe Compose Navigation
- Image loading: Coil
- Logging: Kermit
- Async: Coroutines and Flow
- Background work: WorkManager where Android-specific scheduling is required
- Testing: JUnit5, AssertK, Turbine, `kotlinx-coroutines-test`, Compose UI tests

## Streaming And Source Rules

DreamStream may support multiple content sources, but source integration must be isolated and replaceable.

- Only integrate sources that are legally permitted, user-authorized, public-domain, open, self-hosted, or otherwise compatible with the user's rights and applicable terms.
- Do not implement DRM circumvention, paywall bypassing, credential theft, token scraping, or access-control evasion.
- Do not execute, render, or preserve third-party ads, trackers, popups, or malicious scripts.
- Treat source websites as unreliable external systems. Expect layout changes, network failures, rate limits, geo-blocking, missing media, malformed metadata, and unavailable streams.
- Keep provider-specific DTOs, parsers, URL builders, headers, and quirks inside data-layer source implementations.
- Domain models should describe DreamStream concepts such as content, episode, season, stream, subtitle, artwork, source, and playback progress without leaking website-specific names.
- Prefer explicit provider capability models over boolean flags when capabilities become meaningful.
- Never let a ViewModel or composable parse HTML, inspect provider JSON, construct provider URLs, or know provider-specific rules.
- Use typed errors for source failures, parsing failures, blocked content, unsupported media, and unavailable streams.
- Be conservative with caching. Do not cache secrets, session tokens, or private user data without a clear need and safe storage.
- Add rate limiting, backoff, and cancellation where source calls can be repeated rapidly by search or scrolling.

## Data Layer Rules

Follow `.opencode/skills/android-data-layer/SKILL.md` and `.opencode/skills/android-error-handling/SKILL.md`.

- A data source accesses one source: network API, website parser, local database, file system, preferences, or media store.
- A repository coordinates multiple data sources. Do not call a class a repository if it wraps only one source.
- Put data source and repository interfaces in the domain layer when ViewModels depend on them.
- Put DTOs, Room entities, Ktor calls, parser objects, and mappers in the data layer.
- Keep DTOs, entities, and domain models separate.
- Use extension mappers such as `ContentDto.toContent()` and `ContentEntity.toContent()`.
- Name implementations for what they wrap or how they behave, such as `KtorCatalogDataSource`, `HtmlSourceDataSource`, `RoomBookmarkDataSource`, or `OfflineFirstCatalogRepository`.
- Do not use `Impl` suffixes.
- Configure shared Ktor behavior centrally and inject `HttpClient` through Koin.
- Use safe-call helpers that map network and serialization failures into `DataError.Network` or feature-specific errors.
- Use Room as the single source of truth for offline-first flows when offline support is required.

## Domain Layer Rules

- Domain modules are pure Kotlin.
- Domain owns models, repository/data-source contracts, use cases when they add real value, and feature-specific errors.
- Avoid use-case classes for simple one-line forwarding unless they encode reusable business rules.
- Domain must not depend on `data`, `presentation`, Android, Compose, Room, Ktor, or DI frameworks.
- Prefer immutable data classes and sealed interfaces.
- Model IDs and source IDs explicitly so content from different providers cannot collide accidentally.

## Presentation And MVI Rules

Follow `.opencode/skills/android-presentation-mvi/SKILL.md`.

Every screen should have:

- A single `State` data class.
- A sealed `Action` interface for user actions.
- A sealed `Event` interface for one-time side effects.
- A ViewModel exposing `StateFlow<State>` and an event `Flow` backed by `Channel`.

Rules:

- Update state with `_state.update { it.copy(...) }`.
- Keep all application state in the ViewModel.
- Use `SavedStateHandle` only for essential process-death state.
- Map domain models to `Ui` models in presentation when formatting or UI-specific fields are needed.
- Use `UiText` for user-facing strings that may come from resources or need localization.
- Map user-facing errors with `.toUiText()` in `core:presentation` or feature presentation modules.
- ViewModels should not depend on data-layer implementations.

## Compose UI Rules

Follow `.opencode/skills/android-compose-ui/SKILL.md`.

- Composables render state and forward actions. They do not contain business logic.
- Use a Root/Screen split in the same file.
- Root composables inject ViewModels with `koinViewModel()`, collect state, observe events, and pass callbacks.
- Screen composables receive only state and action callbacks.
- Use `collectAsStateWithLifecycle()` for ViewModel state.
- Do not use `remember` or `rememberSaveable` for app state.
- Use `remember` only for Compose-owned state such as scroll, pager, focus, or animation state.
- Add stable lazy-list keys when a unique ID is obvious.
- Prefer animations that avoid unnecessary recomposition, such as `graphicsLayer` for alpha, scale, rotation, and translation.
- Provide meaningful previews for every Screen composable using realistic sample states.
- Use localized string resources for accessibility descriptions.
- Set `contentDescription = null` only for decorative visuals.

## Navigation Rules

Follow `.opencode/skills/android-navigation/SKILL.md`.

- Use type-safe Compose Navigation with KotlinX Serialization route objects.
- Define one navigation graph per feature in that feature's presentation module.
- Assemble feature graphs in `:app`.
- Use callbacks for cross-feature navigation.
- Do not import another feature's route directly for feature-to-feature navigation.
- Pass scalar IDs through navigation, not complex objects.
- Destination ViewModels should load required data from IDs.

## Dependency Injection Rules

Follow `.opencode/skills/android-di-koin/SKILL.md`.

- Use Koin.
- Create one Koin module per feature layer only when that layer has dependencies to provide.
- Assemble all Koin modules in `:app`.
- Prefer `singleOf`, `factoryOf`, and `viewModelOf` constructor-reference bindings.
- Use lambda bindings only for factory methods, qualifiers, platform-specific setup, or post-construction configuration.
- Inject ViewModels only at Root composables.
- Do not pass ViewModels down the composable tree.

## Testing Rules

Follow `.opencode/skills/android-testing/SKILL.md`.

- Unit-test every ViewModel.
- Unit-test non-trivial domain and data logic.
- Prefer fakes over mocks for repositories and data sources.
- Use JUnit5, AssertK, Turbine, and `kotlinx-coroutines-test`.
- Use `Dispatchers.setMain(UnconfinedTestDispatcher())` for ViewModel tests.
- Inject dispatchers only when a directly unit-tested class dispatches to a non-main dispatcher.
- Write integration tests where database, parser, or network behavior is non-trivial.
- Write Compose UI tests for critical user flows.
- Use the robot pattern when a screen has multiple UI tests or complex flows.

## Media Playback Rules

- Keep playback state separate from catalog/discovery state.
- Put reusable playback contracts and models in a core media module once playback logic becomes non-trivial.
- Keep platform-specific player implementations behind interfaces.
- Do not let feature screens depend directly on a specific player engine unless the feature is explicitly platform-bound.
- Model subtitles, audio tracks, quality variants, headers, and stream expiration explicitly when needed.
- Treat stream resolution as a fallible operation returning typed errors.
- Avoid long-running playback or download work in ViewModels when a platform service or worker is required.

## Privacy And Security Rules

- Do not log tokens, cookies, private URLs, personally identifiable information, or playback history.
- Do not commit secrets, API keys, signing files, generated credentials, or local environment files.
- Use `local.properties`, platform build config, or secure storage for secrets depending on the target.
- Avoid broad file-system access.
- Treat provider headers, cookies, and tokens as sensitive.
- Do not add telemetry unless explicitly requested and designed with opt-in privacy controls.

## Code Style Rules

- Prefer clear names over comments.
- Add comments only for non-obvious decisions, protocol quirks, parser edge cases, or platform constraints.
- Keep functions focused and cohesive.
- Avoid premature abstractions.
- Prefer immutable collections at boundaries.
- Use coroutines and Flow for async streams of data.
- Keep cancellation cooperative in long-running parser, resolver, download, and playback-preparation code.
- Avoid global mutable state.

## Agent Workflow

- Inspect the existing code before editing.
- Use the relevant skills before adding or reviewing modules, data sources, ViewModels, composables, navigation, DI, errors, or tests.
- Preserve unrelated user changes in the worktree.
- Do not make broad rewrites when a focused change solves the task.
- When adding implementation, also add or update tests unless the change is purely mechanical or there is not yet a testable module.
- Run the narrowest relevant Gradle verification command available.
- If no meaningful verification command exists yet, state that clearly in the final response.

## Initial Implementation Guidance

For the first real implementation, establish the foundation in this order:

1. Create `:build-logic` with convention plugins and version-catalog-backed dependency management.
2. Create `:app` and core modules needed immediately.
3. Add `core:domain` with `Error`, `Result`, `EmptyResult`, and result extension helpers.
4. Add `core:presentation` with `UiText` and event observation utilities.
5. Add `core:design-system` with the app theme and first reusable components.
6. Add one vertical feature slice instead of scaffolding every future feature.
7. Add Koin wiring in `:app` only after there are real modules to wire.
8. Add tests for the vertical slice before expanding horizontally.

Do not create empty modules for hypothetical future features.
