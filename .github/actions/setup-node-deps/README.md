# Node.js Dependencies Cache Setup

A comprehensive GitHub Action that automatically detects and supports all major Node.js package managers (npm, yarn, pnpm, and bun) with intelligent dependency caching.

## Features

- 🔍 **Auto-detection**: Automatically detects package manager based on lockfiles
- 📦 **Multi-package manager support**: npm, yarn (classic & modern), pnpm, and bun
- ⚡ **Intelligent caching**: Optimized cache strategies for each package manager
- 🔒 **Frozen lockfile support**: Ensures reproducible builds with immutable installations
- 🎯 **Customizable**: Override detection and commands as needed
- 📊 **Cache analytics**: Provides cache hit information and detected package manager

## Supported Package Managers

| Package Manager | Detection Method | Cache Locations |
|----------------|------------------|-----------------|
| **npm** | `package-lock.json` | `~/.npm`, `node_modules` |
| **yarn (modern)** | `yarn.lock` + `.yarnrc.yml` | `node_modules`, `.yarn/install-state.gz`, `.yarn/cache` |
| **yarn (classic)** | `yarn.lock` (no .yarnrc.yml) | `node_modules`, `~/.cache/yarn` |
| **pnpm** | `pnpm-lock.yaml` | `node_modules`, `~/.pnpm-store` |
| **bun** | `bun.lockb` | `node_modules`, `~/.bun/install/cache` |

## Usage

### Basic Usage (Auto-detection)

```yaml
- name: Setup Node.js and install dependencies
  uses: ./.github/actions/setup-node-deps
```

### With Custom Configuration

```yaml
- name: Setup Node.js and install dependencies
  uses: ./.github/actions/setup-node-deps
  with:
    node-version-file: '.nvmrc'
    package-manager: 'pnpm'
    frozen-lockfile: 'true'
    registry-url: 'https://registry.npmjs.org/'
    cache-prefix: 'v1-'
```

### Force Specific Package Manager

```yaml
- name: Setup with pnpm
  uses: ./.github/actions/setup-node-deps
  with:
    package-manager: 'pnpm'
```

### Custom Install Command

```yaml
- name: Setup with custom command
  uses: ./.github/actions/setup-node-deps
  with:
    install-command: 'npm install --legacy-peer-deps'
```

## Inputs

| Input | Description | Required | Default |
|-------|-------------|----------|---------|
| `node-version-file` | Path to the .nvmrc file | No | `.nvmrc` |
| `package-manager` | Package manager to use (`auto`, `npm`, `yarn`, `pnpm`, `bun`) | No | `auto` |
| `cache-prefix` | Custom prefix for cache keys | No | `""` |
| `registry-url` | Custom registry URL | No | `https://registry.npmjs.org/` |
| `install-command` | Custom install command (overrides default) | No | `""` |
| `frozen-lockfile` | Use frozen/immutable lockfile for installation | No | `true` |

## Outputs

| Output | Description |
|--------|-------------|
| `cache-hit` | Boolean indicating if a cache hit occurred |
| `package-manager` | The detected or specified package manager |

## Auto-Detection Logic

The action uses the following priority order for package manager detection:

1. **bun**: If `bun.lockb` exists
2. **pnpm**: If `pnpm-lock.yaml` exists
3. **yarn**: If `yarn.lock` exists
   - Modern Yarn if `.yarnrc.yml` or `.yarnrc.yaml` exists
   - Classic Yarn otherwise
4. **npm**: If `package-lock.json` exists
5. **npm**: Default fallback if no lockfiles found

## Cache Strategy

### Cache Key Generation

The cache key is generated using:
- Operating system (`runner.os`)
- Package manager type
- Lockfile hash (SHA256)
- All package.json files hash (excluding node_modules)

### Cache Paths by Package Manager

**npm**:
- Global cache: `~/.npm`
- Local dependencies: `node_modules`

**yarn (modern)**:
- Local dependencies: `**/node_modules`
- Install state: `.yarn/install-state.gz`
- Yarn cache: `.yarn/cache`

**yarn (classic)**:
- Local dependencies: `**/node_modules`
- Global cache: `~/.cache/yarn`

**pnpm**:
- Local dependencies: `**/node_modules`
- Global store: `~/.pnpm-store`

**bun**:
- Local dependencies: `**/node_modules`
- Global cache: `~/.bun/install/cache`

## Complete Workflow Example

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
      
      - name: Setup Node.js and dependencies
        id: setup
        uses: ./.github/actions/setup-node-deps
        with:
          node-version-file: '.nvmrc'
          cache-prefix: 'ci-'
      
      - name: Check cache status
        run: |
          echo "Cache hit: ${{ steps.setup.outputs.cache-hit }}"
          echo "Package manager: ${{ steps.setup.outputs.package-manager }}"
      
      - name: Run tests
        run: npm test
      
      - name: Build
        run: npm run build
```

## Advanced Usage

### Matrix Strategy with Different Package Managers

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        package-manager: [npm, yarn, pnpm]
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Setup dependencies
        uses: ./.github/actions/setup-node-deps
        with:
          package-manager: ${{ matrix.package-manager }}
          frozen-lockfile: 'false'  # Allow different lockfiles
      
      - name: Run tests
        run: |
          case "${{ matrix.package-manager }}" in
            npm) npm test ;;
            yarn) yarn test ;;
            pnpm) pnpm test ;;
          esac
```

### Conditional Installation Commands

```yaml
- name: Setup with environment-specific command
  uses: ./.github/actions/setup-node-deps
  with:
    install-command: |
      if [[ "${{ github.event_name }}" == "push" ]]; then
        npm ci --production
      else
        npm ci
      fi
```

## Troubleshooting

### Cache Issues

If you're experiencing cache issues:

1. **Change cache prefix**: Use a different `cache-prefix` to invalidate existing caches
2. **Check lockfile changes**: Ensure lockfiles are committed when dependencies change
3. **Verify package.json**: Make sure all package.json files are properly formatted

### Package Manager Detection Issues

If auto-detection isn't working:

1. **Check lockfiles**: Ensure the correct lockfile exists in your repository
2. **Force specific manager**: Use the `package-manager` input to override detection
3. **Custom command**: Use `install-command` for special cases

### Performance Optimization

- Use `frozen-lockfile: true` for faster, more reliable installs
- Consider using `cache-prefix` with versioning for long-term cache management
- Monitor cache hit rates using the `cache-hit` output

## Contributing

When contributing to this action:

1. Test with all supported package managers
2. Update documentation for any new features
3. Ensure backward compatibility
4. Add appropriate error handling

## License

This action is available under the MIT License.