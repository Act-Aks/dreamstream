---
name: git-commit-workflow
description: Git commit, commit changes, create commit, grouped commits, conventional commits, staged changes. Use when the user asks an agent to commit work, group changes by issue/feature/fix/update/refactor/docs/test/chore, draft commit messages, or decide what should be staged together.
---

# Git Commit Workflow

Use this skill whenever the user asks to commit changes, prepare commits, group work into commits, write commit messages, or inspect staged and unstaged changes for commit readiness.

The goal is safe, understandable history: each commit should represent one coherent reason for change, not an arbitrary file batch.

## Hard Safety Rules

- Never commit unless the user explicitly asks for a commit.
- Never push unless the user explicitly asks for a push.
- Never use destructive git commands such as `git reset --hard`, `git checkout --`, `git clean`, or force push unless the user explicitly requests and confirms them.
- Never amend unless the user explicitly asks for amend, or a hook successfully created a commit and then modified files that must be included. Verify that the commit was created by this assistant before amending.
- Never skip hooks with `--no-verify`, `--no-gpg-sign`, or similar flags unless the user explicitly requests it.
- Never commit secrets, credentials, signing keys, `.env` files, tokens, cookies, private URLs, or generated credential files. Stop and warn the user if they ask to include them.
- Preserve unrelated user changes. If unrelated edits exist, do not stage them into the current commit.
- If unrelated changes are already staged, inspect them and ask before committing unless the user clearly asked to commit all staged work.

## Required Discovery Before Committing

Run these git commands before deciding what to stage or how to message the commit:

```bash
git status --short
git diff --stat
git diff
git diff --cached
git log --oneline -5
```

Use the output to identify:

- Modified, deleted, renamed, and untracked files.
- Which changes are already staged.
- Whether staged changes belong to the same logical unit as unstaged changes.
- Existing commit message style in the repository.
- Suspicious files that should not be committed.

If the diff is too large, summarize file groups and inspect representative hunks before committing. Do not guess from filenames alone when the grouping is ambiguous.

## Grouping Rules

Create one commit per coherent reason for change.

Good grouping examples:

- Feature implementation and its tests in one commit.
- Bug fix and regression test in one commit.
- Refactor that preserves behavior in its own commit.
- Documentation-only update in its own commit.
- Build configuration update in its own commit.

Avoid these groupings:

- Mixing a feature, unrelated formatting, and a dependency upgrade.
- Committing generated IDE files with source changes.
- Splitting tests away from the code they validate unless the user asks.
- Creating many tiny commits for files that only make sense together.

When changes fall into multiple unrelated groups, create multiple commits in a clear order:

1. Build or dependency foundations.
2. Core/domain/data contracts.
3. Feature behavior.
4. Presentation/UI behavior.
5. Tests.
6. Documentation or cleanup.

If grouping cannot be determined safely from the diff, ask one concise question with the proposed groups.

## Commit Message Format

Prefer Conventional Commits unless the existing repo history clearly uses another style.

Format:

```text
<type>(<scope>): <summary>

<body when useful>
```

Rules:

- Use lowercase type.
- Use an optional lowercase scope when it improves clarity.
- Keep the summary imperative and concise.
- Do not end the summary with a period.
- Body is optional; add it only when it explains why, risk, migration, or context.
- Reference issues in the body or footer when the user provides issue IDs.

Types:

- `feat`: new user-visible behavior or capability.
- `fix`: bug fix or behavioral correction.
- `refactor`: internal restructuring without intended behavior change.
- `perf`: performance improvement.
- `docs`: documentation-only change.
- `test`: test-only change.
- `build`: Gradle, dependency, version catalog, build logic, or tooling changes.
- `ci`: CI/CD workflow changes.
- `style`: formatting-only changes with no behavior impact.
- `chore`: maintenance that does not fit another type.
- `revert`: revert a previous commit.

Scope examples for DreamStream:

- `app`
- `build-logic`
- `core-domain`
- `core-data`
- `core-presentation`
- `design-system`
- `navigation`
- `di`
- `home`
- `search`
- `catalog`
- `details`
- `player`
- `downloads`
- `bookmarks`
- `settings`
- `sources`
- `docs`

Examples:

```text
docs(agents): define project architecture rules
```

```text
build(core): add shared Kotlin module conventions

Centralizes common KMP compiler and test configuration so feature modules stay declarative.
```

```text
fix(search): handle empty provider responses

Maps malformed or empty source responses to typed errors instead of leaving the screen in a loading state.
```

## Staging Rules

- Prefer path-based staging for each commit group.
- Stage only files that belong to the current commit group.
- Include untracked files only when they are relevant and expected.
- Do not use interactive git commands such as `git add -i`, `git add -p`, or interactive rebase.
- If partial-file staging is required, avoid committing until the user confirms how to split it, unless a non-interactive safe method is available and the split is obvious.

Useful non-interactive patterns:

```bash
git add AGENTS.md
git add .opencode/skills/git-commit-workflow/SKILL.md
git commit -m "docs(agents): add project instructions"
```

For multiple grouped commits, repeat discovery after each commit:

```bash
git status --short
git diff --stat
```

## Verification Before Commit

Run the narrowest meaningful verification for the staged group before committing when available.

Examples:

- Documentation-only changes: no build required; optionally verify file content.
- Gradle build logic: run the relevant Gradle help/build task if possible.
- Kotlin source changes: run the affected module tests.
- ViewModel changes: run affected unit tests.
- Compose UI changes: run affected UI or screenshot tests if present.

If there is no meaningful verification command yet, state that clearly in the final response.

## Commit Execution Flow

Use this sequence:

1. Inspect status, unstaged diff, staged diff, and recent log.
2. Identify commit groups and suspicious files.
3. If one safe group exists, stage only that group.
4. If multiple groups exist, either create multiple commits or ask if grouping is ambiguous.
5. Run relevant verification when available.
6. Commit with the selected message.
7. Run `git status --short` after each commit.
8. Report the commit hash, message, verification result, and remaining uncommitted changes.

## Handling Hooks

If a pre-commit hook fails:

- Do not amend.
- Fix the reported issue when it is in the commit's scope.
- Re-run verification.
- Create a new commit attempt.

If a hook modifies files after a commit succeeds:

- Verify the commit exists.
- Verify the commit was created by this assistant in the current session.
- Verify the branch has not been pushed.
- Amend only to include hook-generated modifications that belong to the same commit.

## User-Facing Syntax

Users can trigger this workflow with requests like:

```text
commit the changes
```

```text
commit everything related to the agents docs
```

```text
group the changes by feature/fix/docs and commit them
```

```text
create separate commits for the Gradle setup and the home feature
```

```text
commit only the staged changes
```

```text
commit issue #42 as a fix
```

```text
make conventional commits for the current worktree
```

When an issue identifier is provided, include it in the commit body or footer if useful:

```text
fix(search): handle provider timeout

Closes #42
```

## Final Response Format

After committing, respond with:

- Commit hash and message.
- Verification performed, or why none was run.
- Remaining uncommitted changes, if any.
- Whether a restart is needed for opencode config, skill, agent, or command changes.

Keep the response short and factual.
