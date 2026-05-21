# DreamStream

A modern Kotlin Multiplatform multimedia app for discovering and streaming entertainment content through a native, polished, ad-free interface.

DreamStream is designed as a long-lived, modular KMP application supporting multiple content sources, rich media discovery, playback, offline-aware flows, and a high-quality Compose UI. Android is the primary target, with Desktop (JVM) as an additional Compose Multiplatform target.

> **Status:** Early scaffold. The repository currently contains the build infrastructure (convention plugins, version catalog) and the first foundational `core` modules. Feature modules and the `:app` shell are not yet implemented.

## Tech Stack

| Area | Choice |
| --- | --- |
| Language | Kotlin 2.3.21 (Multiplatform) |
| UI | Compose Multiplatform 1.11.0, Material 3 |
| Build | Gradle (Kotlin DSL), AGP 9.2.1, JDK 17 toolchain |
| DI | Koin |
| Networking | Ktor Client |
| Serialization | KotlinX Serialization |
| Local DB | Room |
| Preferences | DataStore |
| Navigation | Type-safe Compose Navigation (Nav3) |
| Image loading | Coil |
| Logging | Kermit |
| Async | Coroutines + Flow |
| Static analysis | Detekt |
| Testing | JUnit5, AssertK, Turbine, kotlinx-coroutines-test, Compose UI Test |

## Targets

- **Android** — `minSdk = 26`, `compileSdk = 37`, `targetSdk = 37`
- **Desktop (JVM)** — JDK 17

## Project Structure

DreamStream uses **feature-layered modularization**: split by feature first, then by Clean Architecture layer (`presentation -> domain <- data`). Domain modules are pure Kotlin; features never depend on other features directly.

```text
DreamStream/
├── build-logic/                    Convention plugins (single source of build truth)
│   └── convention/
├── core/
│   ├── domain/                     Result<T, E>, Error, DataError, result helpers
│   ├── presentation/               UiText, ObserveAsEvents, error → UiText mapping
│   └── design-system/              Theme, color, typography, shape
├── gradle/
│   └── libs.versions.toml          Version catalog (single source of dependency truth)
├── settings.gradle.kts
└── build.gradle.kts
```

Planned (added as features are implemented):

```text
:app
:core:data           Shared HTTP client, DB setup, safe-call helpers
:core:media          Playback abstractions and media session logic
:feature:<name>:domain
:feature:<name>:data
:feature:<name>:presentation
```

Empty modules are not created speculatively. See [`AGENTS.md`](AGENTS.md) for the full architectural contract.

## Getting Started

### Prerequisites

- JDK 17 (a Gradle toolchain is configured; install via [SDKMAN!](https://sdkman.io/) or your platform package manager)
- Android Studio Ladybug or newer (for Android development)
- Git

### Clone

```bash
git clone <repository-url> DreamStream
cd DreamStream
```

### Build

```bash
./gradlew build
```

### Run Tests

```bash
# All tests
./gradlew check

# A single module
./gradlew :core:domain:allTests
```

### Static Analysis

```bash
./gradlew detekt
```

## Architecture

DreamStream follows Clean Architecture with strict module boundaries:

- **Presentation** — MVI: a single `State`, sealed `Action`, sealed `Event`, ViewModel exposing `StateFlow<State>`. Composables render state and forward actions; they contain no business logic. Root composables inject ViewModels with `koinViewModel()`; Screen composables receive only state and callbacks.
- **Domain** — Pure Kotlin. Owns models, repository contracts, use cases (only when they encode reusable business rules), and feature-specific errors. No Android, Compose, Room, Ktor, or DI imports.
- **Data** — Data sources (one per source: network/db/parser/preferences) and repositories that coordinate them. DTOs, Room entities, and Ktor calls live here and are mapped to domain models via extension functions.

### Error Handling

Expected failures use a typed `Result<D, E : Error>` wrapper with helpers (`map`, `onSuccess`, `onFailure`, `asEmptyDataResult`). Exceptions are not used for normal app failures. User-facing errors are mapped to `UiText` in the presentation layer.

### Dependency Injection

Koin, with one module per feature layer, all assembled in `:app`. Constructor-reference bindings (`singleOf`, `factoryOf`, `viewModelOf`) are preferred.

### Source Integration

Content sources are isolated behind data-source interfaces and are replaceable. Provider-specific DTOs, parsers, headers, and quirks stay inside data-layer source implementations and never leak into ViewModels or composables. DreamStream only integrates sources that are legally permitted, user-authorized, public-domain, open, self-hosted, or otherwise compatible with the user's rights and applicable terms — no DRM circumvention, paywall bypassing, or access-control evasion.

## Build Conventions

All shared Gradle configuration lives in [`build-logic/convention`](build-logic/convention) as convention plugins. Module build files stay declarative and minimal:

- Apply the relevant convention plugin (e.g. `kmpLibrary`, `domain`, `feature`, `compose`)
- Declare module-specific dependencies via the version catalog
- No hardcoded versions in module build files

All dependency and plugin versions are centralized in [`gradle/libs.versions.toml`](gradle/libs.versions.toml).

## Testing

- Every ViewModel has unit tests
- Fakes are preferred over mocks
- ViewModel tests use `Dispatchers.setMain(UnconfinedTestDispatcher())` and Turbine for state/event flows
- Critical user flows have Compose UI tests, using the robot pattern for complex screens

See `.opencode/skills/android-testing/SKILL.md` for the full testing playbook.

## Privacy

- Tokens, cookies, private URLs, and PII are never logged
- Secrets are not committed; use `local.properties` or secure platform storage
- Telemetry is opt-in only and is not added without an explicit, designed privacy model

## Contributing

Before contributing, read:

- [`AGENTS.md`](AGENTS.md) — product direction, non-negotiable principles, and module rules
- [`.opencode/skills/`](.opencode/skills) — detailed implementation playbooks for each layer

When in doubt, the skill files are the source of truth for implementation mechanics; `AGENTS.md` is the source of truth for DreamStream-specific product and domain rules.

### Commit Style

Conventional Commits with feature/layer scopes:

```text
feat(core-domain): add Result and Error types
build(convention): refine convention plugins and version catalog
fix(search): handle empty provider responses
```

Each commit should represent one coherent reason for change. See `.opencode/skills/git-commit-workflow/SKILL.md`.
