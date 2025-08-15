# Dreamstream Monorepo

A Turborepo-powered monorepo using Bun for package management. It currently contains a React Native app built with Expo Router.

## Overview

- Monorepo tool: Turborepo
- Package manager: Bun (bun.lock)
- Runtime: Node.js >= 22
- Lint/Format: Biome via Ultracite (biome.jsonc)
- TypeScript across the repo (tsconfig.json)

## Repository Structure

- apps/
  - dreamstream/ — Expo + React Native application (Expo Router)
- packages/ — shared packages (reserved; may be empty)
- turbo.json — Turborepo task pipeline configuration
- biome.jsonc — Biome configuration (lint/format)
- tsconfig.json — TypeScript config
- package.json — workspace and scripts

## Prerequisites

- Node.js >= 22
- Bun >= 1.2.20 (this repo is configured with "packageManager": "bun@1.2.20")
- For running the mobile app:
  - iOS: Xcode + iOS Simulator (macOS)
  - Android: Android Studio + Android Emulator or a physical device with USB debugging enabled

## Install

At the repo root:

```
bun install
```

This installs dependencies across all workspaces (apps and packages).

## Common Scripts (run at repo root)

- Develop all: `bun run dev`
- Build all: `bun run build`
- Lint all: `bun run lint`
- Format all: `bun run format`
- Type-check all: `bun run check-types`

These scripts fan out to each workspace via Turborepo.

You can target a specific app/package using Turborepo filters. Examples:

```
# Dev only the dreamstream app
bun x turbo run dev --filter=dreamstream

# Build only the dreamstream app
bun x turbo run build --filter=dreamstream
```

## App: apps/dreamstream (Expo)

Navigate into the app to use Expo convenience scripts:

```
cd apps/dreamstream

# Start Expo (interactive dev tools)
bun run dev

# Platform-specific entry points
bun run ios
bun run android
bun run web
```

Notes:
- The app uses Expo Router and React Native 0.79 with React 19.
- When running on devices/emulators, ensure the proper tooling (Xcode/Android Studio) is installed.

## Environment Variables

- Turborepo is configured to consider `.env*` files as task inputs (see turbo.json). Place environment files at the appropriate workspace root.
- Example files: `.env`, `.env.local`, `.env.development`, etc.
- Restart processes after changing env files.

## Linting and Formatting

- Biome is configured at the root (biome.jsonc) and run via Ultracite wrappers.
- Run across the monorepo:

```
bun run lint
bun run format
```

- Some workspaces (like apps/dreamstream) also expose `bun ultracite` directly via their own scripts.

## Type Checking

TypeScript is used across the repo. To run type checks:

```
bun run check-types
```

## Remote Caching (optional)

Turborepo supports remote caching via Vercel. To enable:

```
# Authenticate turbo
bun x turbo login

# Link this repo to a remote cache project
bun x turbo link
```

Remote caching can speed up CI and team development by sharing build artifacts.

## Commit Conventions

Conventional commits are supported via Commitizen:

```
bun run cmt
```

This opens an interactive prompt to craft a conventional commit message.

## Troubleshooting

- If Turborepo commands are not found globally, always prefer `bun x turbo ...` from the repo root.
- Ensure Bun and Node versions match the required versions listed above.
- Clear caches if you see inconsistent builds: `rm -rf node_modules .turbo` and then `bun install`.

## License

This repository is part of the Dreamstream project. Unless otherwise specified, it is provided under the project’s license.
