# Summary

Describe the change and why it is needed.

# Type of Change

- [ ] Feature
- [ ] Bug fix
- [ ] Refactor
- [ ] Documentation
- [ ] Build, CI, or tooling
- [ ] Test-only change

# Architecture Notes

- [ ] Changes follow `AGENTS.md` and the relevant `.opencode/skills/` guidance.
- [ ] Domain code remains pure Kotlin with no Android, Compose, Room, Ktor, or DI imports.
- [ ] Feature modules do not depend on other feature modules directly.
- [ ] Expected failures use typed errors and `Result<T, E>` instead of exceptions.
- [ ] New dependencies are justified and declared through `gradle/libs.versions.toml`.

# Source Integration and Privacy

- [ ] This change does not implement DRM circumvention, paywall bypassing, credential theft, token scraping, ad rendering, tracker preservation, or access-control evasion.
- [ ] Tokens, cookies, private URLs, PII, and playback history are not logged or committed.
- [ ] Provider-specific DTOs, parsers, URLs, headers, and quirks stay inside data-layer source implementations.

# Testing

Commands run:

```text

```

- [ ] Tests were added or updated for non-trivial behavior.
- [ ] `./gradlew check detekt` passes locally, or a reason is documented below.

# Screenshots or Recordings

Add screenshots or recordings for UI changes. Remove this section if not applicable.

# Additional Notes

Document follow-up work, known limitations, migration notes, or review areas.
