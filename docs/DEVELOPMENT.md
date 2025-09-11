# 🚀 Development Guide

<div align="center">

**DreamStream Development Setup & Workflows**

*Everything you need to know to contribute to DreamStream*

</div>

---

## 📋 Table of Contents

- [🛠️ Prerequisites](#️-prerequisites)
- [🚀 Quick Start](#-quick-start)
- [🏗️ Development Environment](#️-development-environment)
- [📱 Mobile Development](#-mobile-development)
- [🌐 Web Development](#-web-development)
- [📦 Package Development](#-package-development)
- [🧪 Testing](#-testing)
- [🎨 Code Style](#-code-style)
- [🔄 Git Workflow](#-git-workflow)
- [🐛 Debugging](#-debugging)
- [⚡ Performance](#-performance)
- [📚 Common Tasks](#-common-tasks)
- [🆘 Troubleshooting](#-troubleshooting)

---

## 🛠️ Prerequisites

### System Requirements

| Requirement | Version | Purpose |
|-------------|---------|---------|
| **Node.js** | >= 22.0.0 | JavaScript runtime |
| **Bun** | >= 1.2.21 | Package manager & task runner |
| **Git** | >= 2.30.0 | Version control |

### Platform-Specific Requirements

#### iOS Development
- **macOS** (required for iOS development)
- **Xcode** >= 15.0
- **iOS Simulator** or physical device
- **Apple Developer Account** (for device testing)

#### Android Development
- **Android Studio** >= 2023.1.1
- **Android SDK** >= API 34
- **Android Emulator** or physical device
- **Java Development Kit (JDK)** >= 17

#### Web Development
- **Modern Browser** (Chrome, Firefox, Safari, Edge)
- **Browser Developer Tools**

### Verification Commands

```bash
# Check versions
node --version    # Should be >= 22.0.0
bun --version     # Should be >= 1.2.21
git --version     # Should be >= 2.30.0

# Check platform tools
xcode-select --version  # macOS only
javac -version         # Should be >= 17
```

---

## 🚀 Quick Start

### 1. Clone Repository

```bash
git clone https://github.com/your-username/dreamstream.git
cd dreamstream
```

### 2. Install Dependencies

```bash
# Install all dependencies across the monorepo
bun install

# Verify installation
bun run check-types
```

### 3. Start Development

```bash
# Start all development servers
bun dev

# Or start specific app
cd apps/dreamstream
bun dev
```

### 4. Open App

- **iOS**: Press `i` in terminal or scan QR code with Camera app
- **Android**: Press `a` in terminal or scan QR code with Expo Go
- **Web**: Press `w` in terminal or visit `http://localhost:8081`

---

## 🏗️ Development Environment

### Project Structure

```
dreamstream/
├── 🔧 Root Configuration
│   ├── package.json         # Monorepo dependencies
│   ├── turbo.json          # Turborepo configuration
│   ├── tsconfig.json       # TypeScript base config
│   └── biome.jsonc         # Code quality config
├── 📱 apps/
│   └── dreamstream/        # Main React Native app
│       ├── src/
│       │   ├── app/        # Expo Router pages
│       │   ├── components/ # React components
│       │   ├── hooks/      # Custom hooks
│       │   ├── store/      # Zustand stores
│       │   └── utils/      # App utilities
│       ├── assets/         # Images, fonts, etc.
│       └── app.json        # Expo configuration
├── 📦 packages/
│   ├── common/            # Shared utilities
│   └── providers/         # Content providers
└── 📚 docs/              # Documentation
```

### Environment Variables

Create environment files as needed:

```bash
# Root level
.env.local              # Global environment variables

# App level
apps/dreamstream/.env   # App-specific variables
```

Example `.env.local`:

```env
# Development settings
NODE_ENV=development
DEBUG=true

# API Configuration
API_TIMEOUT=30000
MAX_RETRIES=3

# Feature Flags
ENABLE_DEBUG_MODE=true
ENABLE_ANALYTICS=false
```

---

## 📱 Mobile Development

### Expo Development Build

```bash
cd apps/dreamstream

# Install local CLI
bun add -g @expo/cli

# Start development server
bun dev

# Platform-specific commands
bun ios        # Open iOS Simulator
bun android    # Open Android Emulator
bun web        # Open web browser
```

### Native Development

For features requiring native code:

```bash
# Create development build
npx expo prebuild

# iOS
npx expo run:ios

# Android
npx expo run:android
```

### Device Testing

#### iOS Physical Device

1. **Connect device** via USB
2. **Trust computer** on device
3. **Run**: `bun ios --device`

#### Android Physical Device

1. **Enable Developer Options**
2. **Enable USB Debugging**
3. **Connect device** via USB
4. **Run**: `bun android --device`

### Debugging Tools

#### React Native Debugger

```bash
# Install React Native Debugger
brew install --cask react-native-debugger

# Or download from GitHub releases
```

#### Flipper Integration

```bash
# Install Flipper
brew install --cask flipper

# Enable in development build
# Already configured in app.json
```

---

## 🌐 Web Development

### Local Development

```bash
cd apps/dreamstream

# Start web development
bun web

# Open browser
open http://localhost:8081
```

### Web-Specific Features

- **Responsive Design**: Automatically adapts to screen sizes
- **Progressive Web App**: Installable web app experience
- **Hot Reloading**: Instant updates during development

### Browser Compatibility

| Browser | Support Level |
|---------|---------------|
| Chrome | ✅ Full support |
| Firefox | ✅ Full support |
| Safari | ✅ Full support |
| Edge | ✅ Full support |
| Mobile Safari | ✅ Full support |
| Mobile Chrome | ✅ Full support |

---

## 📦 Package Development

### Working with Packages

```bash
# Navigate to package
cd packages/common

# Install package-specific dependencies
bun install

# Run package scripts
bun lint
bun format
bun build
```

### Creating New Packages

```bash
# Create package directory
mkdir packages/new-package
cd packages/new-package

# Initialize package.json
bun init
```

Example `package.json` for new package:

```json
{
  "name": "@dreamstream/new-package",
  "version": "1.0.0",
  "type": "module",
  "exports": {
    ".": "./src/index.ts"
  },
  "dependencies": {
    "typescript": "5.9.2"
  },
  "scripts": {
    "format": "bun ultracite fix",
    "lint": "bun ultracite check"
  }
}
```

### Package Dependencies

Update root `package.json` workspaces:

```json
{
  "workspaces": [
    "apps/*",
    "packages/*",
    "packages/new-package"
  ]
}
```

---

## 🧪 Testing

### Test Setup

```bash
# Run all tests
bun test

# Run tests for specific package
bun test --filter=@dreamstream/common

# Run tests in watch mode
bun test --watch
```

### Unit Tests

```typescript
// Example unit test
import { describe, it, expect } from '@jest/globals'
import { formatTitle } from '../src/utils'

describe('formatTitle', () => {
  it('should format title correctly', () => {
    expect(formatTitle('the matrix')).toBe('The Matrix')
  })

  it('should handle empty strings', () => {
    expect(formatTitle('')).toBe('')
  })
})
```

### Component Tests

```typescript
import { render, screen } from '@testing-library/react-native'
import { MovieCard } from '../src/components/MovieCard'

describe('MovieCard', () => {
  const mockMovie = {
    id: '1',
    title: 'Test Movie',
    year: '2023',
    poster: 'https://example.com/poster.jpg'
  }

  it('should render movie information', () => {
    render(<MovieCard movie={mockMovie} />)

    expect(screen.getByText('Test Movie')).toBeTruthy()
    expect(screen.getByText('2023')).toBeTruthy()
  })
})
```

### E2E Tests

```typescript
// Example Maestro test
# maestro/search-flow.yaml
appId: com.dreamstream.app
---
- launchApp
- tapOn: "Search"
- inputText: "Matrix"
- tapOn: "The Matrix"
- assertVisible: "The Matrix (1999)"
```

---

## 🎨 Code Style

### Biome Configuration

The project uses Biome via Ultracite for formatting and linting:

```jsonc
// biome.jsonc
{
  "$schema": "https://biomejs.dev/schemas/1.4.1/schema.json",
  "formatter": {
    "enabled": true,
    "lineWidth": 100,
    "indentStyle": "space",
    "indentWidth": 2
  },
  "linter": {
    "enabled": true,
    "rules": {
      "recommended": true,
      "style": {
        "useConst": "error",
        "useTemplate": "error"
      }
    }
  }
}
```

### Code Formatting

```bash
# Format all files
bun cq:fix

# Check formatting without fixing
bun cq:check

# Format specific package
cd packages/common
bun format
```

### TypeScript Guidelines

```typescript
// ✅ Good: Explicit types for public APIs
interface MovieSearchResult {
  readonly id: string
  readonly title: string
  readonly year: number
  readonly poster: string | null
}

// ✅ Good: Use const assertions
const MOVIE_GENRES = [
  'action',
  'comedy',
  'drama'
] as const

type MovieGenre = typeof MOVIE_GENRES[number]

// ✅ Good: Utility types for transformation
type CreateMovieRequest = Omit<Movie, 'id' | 'createdAt'>

// ❌ Bad: Any types
const processData = (data: any) => {
  // Don't do this
}

// ✅ Good: Generic constraints
function processData<T extends Record<string, unknown>>(data: T): T {
  // Type-safe processing
  return data
}
```

---

## 🔄 Git Workflow

### Branch Strategy

```bash
main              # Production-ready code
├── develop       # Integration branch
├── feature/*     # Feature development
├── bugfix/*      # Bug fixes
├── hotfix/*      # Critical fixes
└── release/*     # Release preparation
```

### Commit Conventions

Use conventional commits:

```bash
# Interactive commit tool
bun cmt

# Manual commit format
feat(search): add movie filtering by genre
fix(player): resolve video playback issue
docs(readme): update installation instructions
style(components): fix button hover state
refactor(providers): simplify error handling
test(utils): add tests for date formatting
chore(deps): update dependencies
```

### Pre-commit Hooks

Automatic code quality checks:

```bash
# Configured in .husky/pre-commit
bun cq:check      # Lint and format
bun check-types   # TypeScript check
bun test          # Run tests
```

### Pull Request Process

1. **Create Feature Branch**
   ```bash
   git checkout -b feature/movie-recommendations
   ```

2. **Make Changes & Commit**
   ```bash
   git add .
   bun cmt
   ```

3. **Push & Create PR**
   ```bash
   git push origin feature/movie-recommendations
   ```

4. **Code Review**
   - Automated checks must pass
   - At least one approving review
   - All conversations resolved

5. **Merge**
   - Squash and merge preferred
   - Update CHANGELOG.md if needed

---

## 🐛 Debugging

### React Native Debugging

#### JavaScript Debugging

```bash
# Enable remote debugging
# Shake device -> "Debug with Chrome"
# Or press 'j' in Metro terminal
```

#### Native iOS Debugging

```bash
# Open iOS project in Xcode
cd apps/dreamstream
npx expo prebuild
open ios/dreamstream.xcworkspace

# Use Xcode debugger for native code
```

#### Native Android Debugging

```bash
# Open Android project in Android Studio
cd apps/dreamstream
npx expo prebuild
open -a "Android Studio" android/
```

### Common Debug Commands

```typescript
// React Native debug utilities
import { Platform } from 'react-native'

// Platform-specific debugging
if (__DEV__) {
  console.log('Development mode')

  if (Platform.OS === 'ios') {
    // iOS-specific debug code
  }
}

// Performance debugging
import { InteractionManager } from 'react-native'

InteractionManager.runAfterInteractions(() => {
  // Run after animations complete
})
```

### Network Debugging

```typescript
// HTTP request debugging
import axios from 'axios'

const api = axios.create({
  timeout: 10000,
})

// Add request interceptor
api.interceptors.request.use(request => {
  console.log('Request:', request)
  return request
})

// Add response interceptor
api.interceptors.response.use(
  response => {
    console.log('Response:', response)
    return response
  },
  error => {
    console.error('Error:', error)
    return Promise.reject(error)
  }
)
```

---

## ⚡ Performance

### Development Performance

#### Fast Refresh

Ensure Fast Refresh is enabled:

```typescript
// In your components, avoid:
export default function MyComponent() {
  // Don't define components inside other components
  const InnerComponent = () => <View />

  return <InnerComponent />
}

// Instead do:
const InnerComponent = () => <View />

export default function MyComponent() {
  return <InnerComponent />
}
```

#### Bundle Size Analysis

```bash
# Analyze bundle size
npx expo export --platform web
npx bundler-analyzer dist/_expo/static/js/web

# Check asset sizes
du -sh apps/dreamstream/assets/*
```

### Runtime Performance

#### Component Optimization

```typescript
import { memo, useMemo, useCallback } from 'react'

// Memoize expensive components
const MovieCard = memo(({ movie, onPress }) => {
  const formattedYear = useMemo(() => {
    return new Date(movie.releaseDate).getFullYear()
  }, [movie.releaseDate])

  const handlePress = useCallback(() => {
    onPress(movie.id)
  }, [movie.id, onPress])

  return (
    <Pressable onPress={handlePress}>
      <Text>{movie.title} ({formattedYear})</Text>
    </Pressable>
  )
})
```

#### List Performance

```typescript
import { FlashList } from '@shopify/flash-list'

// Use FlashList for better performance
<FlashList
  data={movies}
  renderItem={({ item }) => <MovieCard movie={item} />}
  estimatedItemSize={120}
  keyExtractor={item => item.id}
/>
```

---

## 📚 Common Tasks

### Adding a New Screen

```bash
# 1. Create screen file
touch apps/dreamstream/src/app/new-screen.tsx

# 2. Add screen content
cat > apps/dreamstream/src/app/new-screen.tsx << 'EOF'
import { View, Text } from 'react-native'

export default function NewScreen() {
  return (
    <View>
      <Text>New Screen</Text>
    </View>
  )
}
EOF

# 3. Navigation is automatic with Expo Router
```

### Adding a New Component

```bash
# 1. Create component directory
mkdir -p apps/dreamstream/src/components/NewComponent

# 2. Create component files
touch apps/dreamstream/src/components/NewComponent/index.tsx
touch apps/dreamstream/src/components/NewComponent/types.ts
touch apps/dreamstream/src/components/NewComponent/styles.ts
```

### Adding a New Provider

```bash
# 1. Create provider file
touch packages/providers/src/NewProvider.ts

# 2. Implement provider
# 3. Export from index.ts
echo "export { NewProvider } from './NewProvider'" >> packages/providers/src/index.ts

# 4. Use in app
```

### Adding Dependencies

```bash
# Add to root (affects all packages)
bun add axios

# Add to specific app
cd apps/dreamstream
bun add react-native-super-grid

# Add to specific package
cd packages/common
bun add lodash
```

---

## 🆘 Troubleshooting

### Common Issues

#### Metro Bundler Issues

```bash
# Clear Metro cache
npx expo start --clear

# Reset everything
rm -rf node_modules
bun install
npx expo start --clear
```

#### iOS Simulator Issues

```bash
# Reset iOS Simulator
xcrun simctl erase all

# Restart simulator
# iOS Simulator -> Device -> Erase All Content and Settings
```

#### Android Emulator Issues

```bash
# Cold boot emulator
emulator -avd Pixel_6_API_34 -cold-boot

# Wipe emulator data
emulator -avd Pixel_6_API_34 -wipe-data
```

#### TypeScript Issues

```bash
# Restart TypeScript language server
# In VS Code: Cmd+Shift+P -> "TypeScript: Restart TS Server"

# Check TypeScript manually
bun tsc --noEmit
```

#### Build Issues

```bash
# Clean all builds
bun clean

# Rebuild everything
rm -rf node_modules
rm -rf apps/dreamstream/.expo
bun install
```

### Performance Issues

#### Slow Development Server

```bash
# Check Node.js version
node --version  # Should be >= 22

# Increase Node.js memory
export NODE_OPTIONS="--max-old-space-size=8192"
bun dev
```

#### Large Bundle Size

```bash
# Analyze what's taking space
npx expo export --platform web
npx bundler-analyzer dist/_expo/static/js/web

# Common culprits:
# - Large images (use Expo Image optimization)
# - Unused dependencies (check package.json)
# - Duplicate dependencies (check bun.lock)
```

### Getting Help

1. **Check Documentation**: Review relevant docs first
2. **Search Issues**: Look for similar problems in GitHub issues
3. **Discord/Community**: Join the community chat
4. **Create Issue**: Provide minimal reproduction case

#### Issue Template

```markdown
## Bug Report

**Environment:**
- OS: [macOS 13.0]
- Node.js: [22.1.0]
- Bun: [1.2.21]
- Expo: [54.0.1]

**Steps to Reproduce:**
1.
2.
3.

**Expected Behavior:**

**Actual Behavior:**

**Additional Context:**
```

---

<div align="center">

**[⬆ Back to Top](#-development-guide)**

*Happy coding! If you run into any issues, don't hesitate to ask for help.*

</div>
