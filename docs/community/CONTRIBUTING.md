# Contributing to DreamStream

Thanks for your interest in contributing to DreamStream. This document describes how to get set up, the architectural rules to follow, and how to submit changes.

By participating in this project you agree to abide by the [Code of Conduct](CODE_OF_CONDUCT.md).

## Sources of Truth

DreamStream has a strict architectural contract. Before opening a non-trivial PR, read:

- [`AGENTS.md`](../../AGENTS.md) — product direction, non-negotiable principles, and module rules
- [`.opencode/skills/`](../../.opencode/skills) — detailed implementation playbooks per layer (module structure, data, presentation/MVI, Compose UI, navigation, DI, error handling, testing, commit workflow)

When `AGENTS.md` and a skill appear to disagree, the skill is the source of truth for **implementation mechanics**, and `AGENTS.md` is the source of truth for **DreamStream-specific product and domain rules**.

## Getting Started

### Prerequisites

- JDK 17 (a Gradle toolchain is configured; install via [SDKMAN!](https://sdkman.io/) if needed)
- Android Studio Ladybug or newer for Android work
- Git

### Setup

```bash
git clone https://github.com/Act-Aks/dreamstream.git DreamStream
cd DreamStream
./gradlew build
```

### Useful Commands

```bash
./gradlew check                  # all tests + verifications
./gradlew :core:domain:allTests  # run tests for a single module
./gradlew detekt                 # static analysis
./gradlew :core:domain:detekt    # detekt for one module
```

## Branching and Workflow

1. Fork the repository and create a topic branch off `main`:
   ```bash
   git checkout -b feat/<short-description>
   ```
2. Keep changes small, correct, and idiomatic. Prefer focused PRs over large rewrites.
3. Add or update tests for any non-trivial change.
4. Run `./gradlew check detekt` before pushing.
5. Open a pull request against `main` and fill in the PR template.

## Architectural Rules (Summary)

These are non-negotiable. Full details are in [`AGENTS.md`](../../AGENTS.md) and the skills.

- **Modularization:** feature-layered — split by feature first, then by Clean Architecture layer (`presentation -> domain <- data`).
- **Domain modules are pure Kotlin.** No Android, Compose, Room, Ktor, or DI imports.
- **Features must not depend on other features directly.** Cross-feature interaction goes through navigation callbacks or shared `core` contracts.
- **Typed errors over exceptions.** Use `Result<D, E : Error>` for expected failures.
- **MVI for screens.** A single `State`, sealed `Action`, sealed `Event`, ViewModel exposing `StateFlow<State>`. Composables render state and forward actions; they hold no business logic.
- **Root/Screen split.** Root composables inject ViewModels via `koinViewModel()`; Screen composables receive only state and callbacks.
- **DI:** Koin, with one module per feature layer, all assembled in `:app`. Prefer `singleOf`, `factoryOf`, `viewModelOf`.
- **Build:** Kotlin DSL only. All versions live in `gradle/libs.versions.toml`. Shared Gradle config lives as convention plugins in `build-logic/convention`. Module build files stay declarative and minimal.
- **Don't create empty modules.** Add a module only when there is real code that justifies it.
- **No `Impl` suffixes.** Name implementations for what they wrap or how they behave (e.g. `KtorCatalogDataSource`, `OfflineFirstCatalogRepository`).

## Source Integration Rules

DreamStream may integrate multiple content sources, but only sources that are legally permitted, user-authorized, public-domain, open, self-hosted, or otherwise compatible with the user's rights and applicable terms.

Pull requests that implement DRM circumvention, paywall bypassing, credential theft, token scraping, ad rendering, or access-control evasion **will be rejected**.

## Testing

- Every ViewModel has unit tests.
- Prefer **fakes** over mocks for repositories and data sources.
- Use JUnit5, AssertK, Turbine, and `kotlinx-coroutines-test`.
- ViewModel tests use `Dispatchers.setMain(UnconfinedTestDispatcher())`.
- Add Compose UI tests for critical user flows; use the robot pattern when a screen has multiple tests.

See [`android-testing`](../../.opencode/skills/android-testing/SKILL.md) for the full playbook.

## Commit Style

DreamStream uses **Conventional Commits** with feature/layer scopes.

```text
feat(core-domain): add Result and Error types
build(convention): refine convention plugins and version catalog
fix(search): handle empty provider responses
docs(readme): document module structure
test(home-presentation): cover refresh flow
```

Each commit should represent **one coherent reason for change**. Group related work; split unrelated work. See [`git-commit-workflow`](../../.opencode/skills/git-commit-workflow/SKILL.md).

## Pull Request Checklist

Before requesting review:

- [ ] PR title follows Conventional Commits (`feat(scope): ...`)
- [ ] Changes follow `AGENTS.md` and the relevant skill
- [ ] No cross-feature dependencies introduced
- [ ] Domain modules contain no framework imports
- [ ] Tests added or updated where applicable
- [ ] `./gradlew check detekt` passes locally
- [ ] No secrets, tokens, or PII committed
- [ ] No new dependencies added without justification (and added to the version catalog)

## Reporting Bugs and Requesting Features

Use the [issue templates](../../.github/ISSUE_TEMPLATE). Search existing issues first to avoid duplicates.

## Security

Do **not** open public issues for security vulnerabilities. See [SECURITY.md](SECURITY.md) for the disclosure process.

## License

By contributing, you agree that your contributions will be licensed under the [Apache License 2.0](../legal/LICENSE).
