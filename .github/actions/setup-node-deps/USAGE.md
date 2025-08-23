# Usage Guide - Node.js Dependencies Cache Setup

This guide provides comprehensive examples and best practices for using the Node.js Dependencies Cache Setup action in your GitHub workflows.

## Quick Start

### Basic Usage
The simplest way to use this action is with auto-detection:

```yaml
steps:
  - uses: actions/checkout@v4
  - uses: ./.github/actions/setup-node-deps
```

This will:
- Auto-detect your package manager based on lockfiles
- Setup Node.js using `.nvmrc` (if it exists)
- Cache dependencies intelligently
- Install dependencies with frozen lockfile

### With Custom Configuration
```yaml
steps:
  - uses: actions/checkout@v4
  - name: Setup Node.js and dependencies
    uses: ./.github/actions/setup-node-deps
    with:
      node-version-file: '.node-version'
      package-manager: 'pnpm'
      cache-prefix: 'v1-'
      frozen-lockfile: 'true'
```

## Complete Workflow Examples

### Basic CI Workflow
```yaml
name: CI
on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v4
      
      - name: Setup Node.js and dependencies
        id: setup
        uses: ./.github/actions/setup-node-deps
        with:
          cache-prefix: 'ci-'
      
      - name: Run linting
        run: npm run lint
      
      - name: Run tests
        run: npm test
      
      - name: Build
        run: npm run build
      
      - name: Show cache info
        run: |
          echo "Cache hit: ${{ steps.setup.outputs.cache-hit }}"
          echo "Package manager: ${{ steps.setup.outputs.package-manager }}"
```

### Multi-OS Testing
```yaml
name: Multi-OS Test
on: [push, pull_request]

jobs:
  test:
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
    runs-on: ${{ matrix.os }}
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Setup dependencies
        uses: ./.github/actions/setup-node-deps
        with:
          cache-prefix: '${{ matrix.os }}-'
      
      - name: Test
        run: npm test
```

### Multi-Package Manager Testing
```yaml
name: Package Manager Compatibility
on: [push, pull_request]

jobs:
  test:
    strategy:
      matrix:
        package-manager: [npm, yarn, pnpm]
        include:
          - package-manager: npm
            install-cmd: npm ci
          - package-manager: yarn
            install-cmd: yarn install --immutable
          - package-manager: pnpm
            install-cmd: pnpm install --frozen-lockfile
    
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Setup with ${{ matrix.package-manager }}
        uses: ./.github/actions/setup-node-deps
        with:
          package-manager: ${{ matrix.package-manager }}
          cache-prefix: '${{ matrix.package-manager }}-'
      
      - name: Run tests
        run: |
          case "${{ matrix.package-manager }}" in
            npm) npm test ;;
            yarn) yarn test ;;
            pnpm) pnpm test ;;
          esac
```

### Production Deployment
```yaml
name: Deploy
on:
  push:
    branches: [main]
    tags: ['v*']

jobs:
  deploy:
    runs-on: ubuntu-latest
    environment: production
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Setup for production
        uses: ./.github/actions/setup-node-deps
        with:
          cache-prefix: 'prod-'
          frozen-lockfile: 'true'
          install-command: 'npm ci --only=production'
      
      - name: Build for production
        run: npm run build
        env:
          NODE_ENV: production
      
      - name: Deploy
        run: npm run deploy
```

## Advanced Usage Patterns

### Conditional Installation
```yaml
- name: Setup dependencies
  uses: ./.github/actions/setup-node-deps
  with:
    install-command: |
      if [[ "${{ github.event_name }}" == "push" ]]; then
        npm ci --only=production
      else
        npm ci
      fi
```

### Custom Registry
```yaml
- name: Setup with private registry
  uses: ./.github/actions/setup-node-deps
  with:
    registry-url: 'https://npm.company.com'
    cache-prefix: 'private-'
  env:
    NODE_AUTH_TOKEN: ${{ secrets.NPM_TOKEN }}
```

### Matrix with Node Versions
```yaml
strategy:
  matrix:
    node-version: [16, 18, 20]
    
steps:
  - uses: actions/checkout@v4
  
  - name: Create .nvmrc
    run: echo "${{ matrix.node-version }}" > .nvmrc
  
  - name: Setup Node.js ${{ matrix.node-version }}
    uses: ./.github/actions/setup-node-deps
    with:
      cache-prefix: 'node${{ matrix.node-version }}-'
```

### Monorepo Support
```yaml
- name: Setup root dependencies
  uses: ./.github/actions/setup-node-deps
  with:
    cache-prefix: 'root-'

- name: Setup workspace dependencies
  working-directory: ./packages/frontend
  uses: ./.github/actions/setup-node-deps
  with:
    cache-prefix: 'frontend-'
    package-manager: 'yarn'
```

## Error Handling Examples

### With Retry Logic
```yaml
- name: Setup dependencies with retry
  uses: ./.github/actions/setup-node-deps
  with:
    cache-prefix: 'retry-'
  continue-on-error: true
  id: setup-deps

- name: Retry on failure
  if: steps.setup-deps.outcome == 'failure'
  uses: ./.github/actions/setup-node-deps
  with:
    cache-prefix: 'retry-fallback-'
    frozen-lockfile: 'false'
```

### Fallback Package Manager
```yaml
- name: Try pnpm first
  id: pnpm-setup
  uses: ./.github/actions/setup-node-deps
  with:
    package-manager: 'pnpm'
  continue-on-error: true

- name: Fallback to npm
  if: steps.pnpm-setup.outcome == 'failure'
  uses: ./.github/actions/setup-node-deps
  with:
    package-manager: 'npm'
```

## Performance Optimization

### Cache Warming
```yaml
- name: Warm cache
  if: github.event_name == 'schedule'
  uses: ./.github/actions/setup-node-deps
  with:
    cache-prefix: 'warm-'
```

### Separate Dev and Prod Caches
```yaml
- name: Setup for development
  if: github.event_name == 'pull_request'
  uses: ./.github/actions/setup-node-deps
  with:
    cache-prefix: 'dev-'
    frozen-lockfile: 'false'

- name: Setup for production
  if: github.event_name == 'push' && github.ref == 'refs/heads/main'
  uses: ./.github/actions/setup-node-deps
  with:
    cache-prefix: 'prod-'
    frozen-lockfile: 'true'
```

## Package Manager Specific Examples

### npm
```yaml
- name: Setup npm with specific configuration
  uses: ./.github/actions/setup-node-deps
  with:
    package-manager: 'npm'
    install-command: 'npm ci --no-audit --no-fund --prefer-offline'
```

### Yarn Modern (Berry)
```yaml
- name: Setup Yarn Berry
  uses: ./.github/actions/setup-node-deps
  with:
    package-manager: 'yarn'
    install-command: 'yarn install --immutable --inline-builds'
```

### pnpm
```yaml
- name: Setup pnpm with workspace
  uses: ./.github/actions/setup-node-deps
  with:
    package-manager: 'pnpm'
    install-command: 'pnpm install --frozen-lockfile --recursive'
```

### Bun
```yaml
- name: Setup Bun
  uses: ./.github/actions/setup-node-deps
  with:
    package-manager: 'bun'
    install-command: 'bun install --frozen-lockfile --no-save'
```

## Debugging and Troubleshooting

### Enable Verbose Logging
```yaml
- name: Setup with debug info
  uses: ./.github/actions/setup-node-deps
  with:
    cache-prefix: 'debug-'
  env:
    ACTIONS_STEP_DEBUG: true
```

### Cache Analysis
```yaml
- name: Analyze cache performance
  run: |
    echo "Cache hit: ${{ steps.setup.outputs.cache-hit }}"
    echo "Package manager: ${{ steps.setup.outputs.package-manager }}"
    
    if [[ "${{ steps.setup.outputs.cache-hit }}" == "true" ]]; then
      echo "✅ Cache hit - dependencies restored from cache"
    else
      echo "📦 Cache miss - dependencies installed fresh"
    fi
```

### Validate Installation
```yaml
- name: Validate dependencies
  run: |
    # Check if node_modules exists
    if [[ ! -d "node_modules" ]]; then
      echo "❌ node_modules not found"
      exit 1
    fi
    
    # Check if main dependency exists
    if [[ ! -f "node_modules/react/package.json" ]]; then
      echo "❌ Expected dependency not found"
      exit 1
    fi
    
    echo "✅ Dependencies validated"
```

## Best Practices

### 1. Use Specific Cache Prefixes
```yaml
# Good - specific and versioned
cache-prefix: 'v2-ci-'

# Avoid - generic
cache-prefix: 'cache-'
```

### 2. Pin Package Manager Versions
```yaml
# Create .tool-versions file
- name: Create tool versions
  run: |
    echo "nodejs 18.19.0" > .tool-versions
    echo "pnpm 8.15.0" >> .tool-versions
```

### 3. Separate Build and Test Dependencies
```yaml
# Test job - full dependencies
- name: Setup for testing
  uses: ./.github/actions/setup-node-deps
  with:
    cache-prefix: 'test-'

# Build job - production only
- name: Setup for building
  uses: ./.github/actions/setup-node-deps
  with:
    cache-prefix: 'build-'
    install-command: 'npm ci --only=production'
```

### 4. Handle Multiple Lockfiles
```yaml
# Cleanup conflicting lockfiles
- name: Clean lockfiles
  run: |
    # Remove lockfiles that don't match your preferred package manager
    rm -f yarn.lock pnpm-lock.yaml
    # Keep only package-lock.json for npm
```

### 5. Monitor Cache Hit Rates
```yaml
- name: Cache metrics
  run: |
    echo "::notice title=Cache Status::Hit: ${{ steps.setup.outputs.cache-hit }}"
    
    if [[ "${{ steps.setup.outputs.cache-hit }}" == "false" ]]; then
      echo "::warning title=Cache Miss::Consider checking if lockfile changed"
    fi
```

## Common Issues and Solutions

### Issue: Cache Never Hits
**Solution**: Check that your lockfile is committed and doesn't change between runs.

```yaml
- name: Verify lockfile
  run: |
    if [[ -n "$(git status --porcelain package-lock.json)" ]]; then
      echo "::error::package-lock.json is modified"
      git diff package-lock.json
      exit 1
    fi
```

### Issue: Different Package Managers Detected
**Solution**: Explicitly specify the package manager.

```yaml
- name: Force package manager
  uses: ./.github/actions/setup-node-deps
  with:
    package-manager: 'npm'  # Force npm even if yarn.lock exists
```

### Issue: Installation Fails on Windows
**Solution**: Use cross-platform install commands.

```yaml
- name: Cross-platform setup
  uses: ./.github/actions/setup-node-deps
  with:
    install-command: |
      if [[ "$RUNNER_OS" == "Windows" ]]; then
        npm ci --no-audit
      else
        npm ci --no-audit --prefer-offline
      fi
```

This usage guide covers the most common scenarios and advanced use cases for the Node.js Dependencies Cache Setup action. Refer to the main README.md for complete input/output documentation.