<div align="center">

# DreamStream

**A native-first Kotlin Multiplatform media experience built for discovery, playback, and long-term modular growth.**

[![Quality Gates](https://github.com/Act-Aks/dreamstream/actions/workflows/quality-gates.yml/badge.svg?branch=main)](https://github.com/Act-Aks/dreamstream/actions/workflows/quality-gates.yml)
[![License: Apache-2.0](https://img.shields.io/badge/License-Apache--2.0-0F172A?labelColor=7C3AED)](docs/legal/LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white)](gradle/libs.versions.toml)
[![Compose](https://img.shields.io/badge/Compose_Multiplatform-1.12.0--alpha01-4285F4?logo=jetpackcompose&logoColor=white)](gradle/libs.versions.toml)
[![Gradle](https://img.shields.io/badge/Gradle-9.5.1-02303A?logo=gradle&logoColor=white)](gradle/wrapper/gradle-wrapper.properties)
[![Platform](https://img.shields.io/badge/Targets-Android_%7C_Desktop-14B8A6)](#targets)

<br />

![DreamStream hero banner](docs/assets/readme-hero.svg)

<br />

<table>
  <tr>
    <td><strong>Native UI</strong><br />Compose-first, polished, responsive interfaces.</td>
    <td><strong>Modular Core</strong><br />Feature-layered KMP architecture with strict boundaries.</td>
    <td><strong>Privacy First</strong><br />No telemetry or sensitive logging without explicit design.</td>
  </tr>
  <tr>
    <td><strong>Typed Failures</strong><br />Expected failures flow through Result and domain errors.</td>
    <td><strong>Source Safe</strong><br />Provider details stay isolated in data-layer implementations.</td>
    <td><strong>Production Ready</strong><br />CI, Dependabot, issue forms, security policy, and contribution docs.</td>
  </tr>
</table>

</div>

## Status

DreamStream has three working vertical slices. The build infrastructure, convention plugins, shared domain library (`:core:model`), core modules, glassmorphic design system, home screen (`:feature:home`), details screen (`:feature:details`), and search screen (`:feature:search`) are all complete with 120 passing unit tests. Tapping a content card on the home screen navigates to the full detail view; the search screen provides keyword discovery across the stub catalog. The debug APK builds and runs. The project is in active feature development.

## Vision

DreamStream is a modern Kotlin Multiplatform multimedia app for discovering and streaming entertainment content through a native, polished, ad-free interface.

The project is designed as a long-lived modular application that can support multiple content sources, rich discovery, playback, offline-aware flows, and high-quality Android-first Compose UI while remaining portable to future KMP targets.

## Project Snapshot

| Area | Current Choice |
| --- | --- |
| Language | Kotlin 2.3.21 Multiplatform |
| UI | Compose Multiplatform 1.12.0-alpha01, Material 3 |
| Design system | Glassmorphic — electric violet/cyan/pink palette, `haze` blur, `GlassCard` / `GlassSurface` / `GlassTopBar` / `GradientBackground` |
| Build | Gradle Kotlin DSL, AGP 9.2.1, JDK 17 toolchain |
| Architecture | Feature-layered Clean Architecture |
| Presentation | MVI with `State`, `Action`, `Event`, `StateFlow`, and one-time event `Flow` |
| Dependency injection | Koin |
| Networking | Ktor Client |
| Serialization | KotlinX Serialization |
| Local data | Room, DataStore |
| Navigation | Type-safe Compose Navigation |
| Media | Media3 planned for Android playback abstractions |
| Testing | JUnit5, AssertK, Turbine, kotlinx-coroutines-test, Compose UI Test |
| Quality | Detekt, CI, Dependabot, EditorConfig |

## Targets

| Target | Status | Notes |
| --- | --- | --- |
| Android | Primary | `minSdk = 26`, `compileSdk = 37`, `targetSdk = 37` |
| Desktop JVM | Foundation ready | Compose Multiplatform with JDK 17 |

## Architecture At A Glance

```text
presentation -> domain <- data
```

DreamStream uses feature-layered modularization. Code lives in a feature module unless it is genuinely shared; shared contracts and utilities live in the right `core` module. Domain modules stay pure Kotlin and never import Android, Compose, Room, Ktor, or DI frameworks.

```text
DreamStream/
|-- build-logic/                  Convention plugins (13 plugins)
|   `-- convention/
|-- core/
|   |-- domain/                   Result<T, E>, Error, DataError, result helpers
|   |-- presentation/             UiText, ObserveAsEvents, error-to-UiText mapping
|   |-- design-system/            Glassmorphic theme, tokens, gradient brushes,
|   |                              GradientBackground, GlassCard, GlassSurface, GlassTopBar
|   `-- model/                    Shared domain types across features:
|                                  catalog/ (ContentType, Episode, Quality, Season, …)
|                                  detail/ (ContentDetail hierarchy, ShowStatus)
|                                  media/ (StreamLink, Subtitle)
|                                  plugin/ (DreamError, PluginManifest, …)
|                                  search/ (SearchResult hierarchy, SearchResultExtensions)
|-- feature/
|   |-- home/
|   |   |-- domain/               Content model, HomeRepository contract, HomeError
|   |   |-- data/                 InMemoryHomeRepository (stub, 3 sections / 10 items)
|   |   `-- presentation/         HomeViewModel (MVI), HomeScreen (glassmorphic UI),
|   |                              Koin module, HomeRoute — 38 unit tests
|   |-- details/
|   |   |-- domain/               DetailContent model, DetailsRepository contract, DetailsError
|   |   |-- data/                 InMemoryDetailsRepository (full records for all 10 IDs)
|   |   `-- presentation/         DetailsViewModel (SavedStateHandle), DetailsScreen,
|   |                              Koin module, DetailsRoute(contentId) — 38 unit tests
|   `-- search/
|       |-- domain/               SearchRepository contract, SearchError
|       |-- data/                 InMemorySearchRepository (keyword search across stub catalog)
|       `-- presentation/         SearchViewModel (MVI), SearchScreen (search bar + result grid),
|                                  Koin module, SearchRoute — 39 unit tests
|-- app/
|   `-- android/                  MainActivity, AppNavigation (home→details, search tab),
|                                  Koin assembly for all modules
|-- gradle/
|   `-- libs.versions.toml        Dependency and plugin versions
|-- settings.gradle.kts
`-- build.gradle.kts
```

Modules are added only when implementation needs them. Planned but not yet present:

```text
:core:data
:core:media
… and additional feature slices (player, catalog, bookmarks, settings, …)
```

## Guardrails

| Principle | What it means |
| --- | --- |
| Clean boundaries | `presentation -> domain <- data`; features do not depend on each other directly. |
| Typed errors | Expected failures use `Result<D, E : Error>` instead of exceptions. |
| Isolated sources | Provider DTOs, parsers, headers, URLs, and quirks stay inside data-layer source implementations. |
| Privacy by default | Tokens, cookies, private URLs, playback history, and PII are not logged or committed. |
| No speculative modules | Empty modules are not created for hypothetical future features. |
| Legal source rules | No DRM circumvention, paywall bypassing, credential theft, token scraping, ad rendering, tracker preservation, or access-control evasion. |

See [`AGENTS.md`](AGENTS.md) for the full architectural contract.

## Quick Start

### Prerequisites

- JDK 17
- Android Studio Ladybug or newer for Android development
- Git

### Clone

```bash
git clone https://github.com/Act-Aks/dreamstream.git DreamStream
cd DreamStream
```

### Build And Verify

```bash
./gradlew build
./gradlew check detekt
```

### Focused Commands

```bash
./gradlew :core:domain:allTests
./gradlew :core:domain:detekt
```

## Build System

All shared Gradle configuration lives in [`build-logic/convention`](build-logic/convention). Module build files stay small and declarative.

| Rule | Location |
| --- | --- |
| Dependency and plugin versions | [`gradle/libs.versions.toml`](gradle/libs.versions.toml) |
| Convention plugins | [`build-logic/convention`](build-logic/convention) |
| Detekt config | [`config/detekt/detekt.yml`](config/detekt/detekt.yml) |
| Quality gates workflow | [`.github/workflows/quality-gates.yml`](.github/workflows/quality-gates.yml) |
| Dependency updates | [`.github/dependabot.yml`](.github/dependabot.yml) |

## Testing Strategy

- Every ViewModel should have unit tests.
- Fakes are preferred over mocks for repositories and data sources.
- ViewModel tests use `Dispatchers.setMain(UnconfinedTestDispatcher())` and Turbine.
- Critical user flows should have Compose UI tests.
- Non-trivial parser, database, repository, and source behavior should have integration tests.

See [`.opencode/skills/android-testing/SKILL.md`](.opencode/skills/android-testing/SKILL.md) for the full testing playbook.

## Contributing

Start here:

| Document | Purpose |
| --- | --- |
| [`CONTRIBUTING.md`](docs/community/CONTRIBUTING.md) | Setup, workflow, architecture summary, and PR checklist |
| [`AGENTS.md`](AGENTS.md) | Product direction, non-negotiable principles, and module rules |
| [`.opencode/skills/`](.opencode/skills) | Implementation playbooks for each layer |
| [`CODE_OF_CONDUCT.md`](docs/community/CODE_OF_CONDUCT.md) | Community expectations and enforcement process |
| [`SECURITY.md`](docs/community/SECURITY.md) | Private vulnerability reporting process |
| [`CHANGELOG.md`](docs/release/CHANGELOG.md) | Notable project changes |

Issues and pull requests should use the templates in [`.github`](.github). Do not open public issues for security vulnerabilities or Code of Conduct incidents.

DreamStream uses Conventional Commits with feature or layer scopes:

```text
feat(core-domain): add Result and Error types
build(convention): refine convention plugins and version catalog
fix(search): handle empty provider responses
```

## License

DreamStream is licensed under the [Apache License 2.0](docs/legal/LICENSE).

Copyright 2026 DreamStream Contributors.
