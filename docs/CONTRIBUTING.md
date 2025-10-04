# 🤝 Contributing to DreamStream

<div align="center">

**Thank you for your interest in contributing to DreamStream!**

*We welcome contributions from developers of all skill levels*

[![Contributors](https://img.shields.io/github/contributors/Act-Aks/dreamstream?style=for-the-badge)](https://github.com/Act-Aks/dreamstream/graphs/contributors)
[![Pull Requests](https://img.shields.io/github/issues-pr/Act-Aks/dreamstream?style=for-the-badge)](https://github.com/Act-Aks/dreamstream/pulls)
[![Issues](https://img.shields.io/github/issues/Act-Aks/dreamstream?style=for-the-badge)](https://github.com/Act-Aks/dreamstream/issues)

</div>

---

## 📋 Table of Contents

- [🌟 Ways to Contribute](#-ways-to-contribute)
- [🚀 Getting Started](#-getting-started)
- [🔄 Development Workflow](#-development-workflow)
- [📝 Commit Guidelines](#-commit-guidelines)
- [🐛 Reporting Issues](#-reporting-issues)
- [💡 Feature Requests](#-feature-requests)
- [🔧 Pull Requests](#-pull-requests)
- [🧪 Testing](#-testing)
- [📚 Documentation](#-documentation)
- [🎨 Code Style](#-code-style)
- [🛡️ Code of Conduct](#️-code-of-conduct)
- [❓ Questions](#-questions)

---

## 🌟 Ways to Contribute

There are many ways to contribute to DreamStream:

### 🔧 Code Contributions
- **🐛 Bug Fixes**: Help us squash bugs and improve stability
- **✨ New Features**: Implement new functionality and enhancements
- **⚡ Performance**: Optimize existing code for better performance
- **🎨 UI/UX**: Improve the user interface and experience

### 📚 Non-Code Contributions
- **📖 Documentation**: Improve guides, API docs, and tutorials
- **🧪 Testing**: Write tests and help with quality assurance
- **🐛 Bug Reports**: Report issues with detailed reproduction steps
- **💡 Ideas**: Suggest new features and improvements
- **🌍 Translation**: Help translate the app to different languages
- **🎨 Design**: Contribute to UI/UX design and assets

### 🤝 Community Support
- **❓ Help Others**: Answer questions in discussions and issues
- **📢 Spread the Word**: Share DreamStream with others
- **⭐ Star the Repo**: Show your support by starring the repository

---

## 🚀 Getting Started

### Prerequisites

Before contributing, make sure you have:

- **Node.js** >= 22.0.0
- **Bun** >= 1.2.21
- **Git** >= 2.30.0
- Basic knowledge of **React Native** and **TypeScript**

### First-Time Setup

1. **Fork the Repository**
   ```bash
   # Click "Fork" button on GitHub
   # Then clone your fork
   git clone https://github.com/YOUR_USERNAME/dreamstream.git
   cd dreamstream
   ```

2. **Add Upstream Remote**
   ```bash
   git remote add upstream https://github.com/original-username/dreamstream.git
   ```

3. **Install Dependencies**
   ```bash
   bun install
   ```

4. **Verify Setup**
   ```bash
   bun dev
   # Should start the development server without errors
   ```

5. **Run Tests**
   ```bash
   bun test
   # All tests should pass
   ```

### Understanding the Codebase

- **📱 `apps/dreamstream/`** - Main React Native application
- **📦 `packages/common/`** - Shared utilities and types
- **🔌 `packages/providers/`** - Content provider implementations
- **📚 `docs/`** - Project documentation
- **🧪 `__tests__/`** - Test files

---

## 🔄 Development Workflow

### 1. Choose an Issue

- Browse [open issues](https://github.com/Act-Aks/dreamstream/issues)
- Look for issues labeled `good first issue` for beginners
- Comment on the issue to let others know you're working on it

### 2. Create a Branch

```bash
# Update your main branch
git checkout main
git pull upstream main

# Create a new branch
git checkout -b feature/your-feature-name
# or
git checkout -b fix/issue-description
```

### 3. Make Changes

- Follow our [code style guidelines](#-code-style)
- Write tests for new functionality
- Update documentation as needed
- Test your changes thoroughly

### 4. Test Your Changes

```bash
# Run linting and formatting
bun cq:check

# Run type checking
bun check-types

# Run all tests
bun test

# Test on different platforms
bun ios
bun android
bun web
```

### 5. Commit Changes

```bash
# Stage your changes
git add .

# Commit with conventional commit format
bun cmt
# or manually:
git commit -m "feat: add movie recommendation engine"
```

### 6. Push and Create PR

```bash
# Push to your fork
git push origin feature/your-feature-name

# Create Pull Request on GitHub
```

---

## 📝 Commit Guidelines

We use [Conventional Commits](https://www.conventionalcommits.org/) for clear commit messages:

### Commit Format

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

### Commit Types

| Type | Description |
|------|-------------|
| `feat` | New feature |
| `fix` | Bug fix |
| `docs` | Documentation changes |
| `style` | Code style changes (formatting, etc.) |
| `refactor` | Code refactoring |
| `perf` | Performance improvements |
| `test` | Adding or updating tests |
| `chore` | Maintenance tasks |
| `ci` | CI/CD changes |

### Examples

```bash
feat(search): add voice search functionality
fix(player): resolve video buffering issue
docs(readme): update installation instructions
style(components): format button styles
refactor(providers): simplify error handling
test(utils): add date formatting tests
chore(deps): update dependencies to latest versions
```

### Using Commitizen

We recommend using our interactive commit tool:

```bash
bun cmt
```

This will guide you through creating properly formatted commits.

---

## 🐛 Reporting Issues

### Before Reporting

1. **Search Existing Issues**: Check if the issue already exists
2. **Check Documentation**: Review docs and FAQ
3. **Test Latest Version**: Ensure you're using the latest version
4. **Minimal Reproduction**: Create a minimal example that reproduces the issue

### Issue Template

```markdown
## Bug Report

**Environment:**
- OS: [e.g., macOS 13.0, Windows 11, Ubuntu 22.04]
- Node.js version: [e.g., 22.1.0]
- Bun version: [e.g., 1.2.21]
- App version: [e.g., 1.0.0]
- Device: [e.g., iPhone 14 Pro, Pixel 6]

**Steps to Reproduce:**
1. Go to...
2. Click on...
3. Scroll down to...
4. See error

**Expected Behavior:**
A clear description of what you expected to happen.

**Actual Behavior:**
A clear description of what actually happened.

**Screenshots:**
If applicable, add screenshots to help explain the problem.

**Additional Context:**
Add any other context about the problem here.

**Possible Solution:**
If you have ideas on how to fix it, please share.
```

### Security Issues

**⚠️ Do not report security vulnerabilities in public issues.**

For security issues, please email us directly at: security@dreamstream.com

---

## 💡 Feature Requests

### Before Requesting

1. **Search Existing Requests**: Check if someone already requested it
2. **Consider Scope**: Ensure it fits DreamStream's vision
3. **Think About Users**: How will this benefit the user base?

### Feature Request Template

```markdown
## Feature Request

**Problem Statement:**
A clear description of what problem this feature would solve.

**Proposed Solution:**
A clear description of what you want to happen.

**Alternative Solutions:**
Alternative solutions or features you've considered.

**Use Cases:**
Specific use cases where this feature would be helpful.

**Additional Context:**
Add any other context, mockups, or examples here.
```

---

## 🔧 Pull Requests

### Before Submitting

- ✅ Fork the repo and create your branch from `main`
- ✅ Test your changes on multiple platforms (iOS, Android, Web)
- ✅ Ensure all tests pass
- ✅ Update documentation if needed
- ✅ Follow code style guidelines
- ✅ Add tests for new functionality

### PR Template

```markdown
## Pull Request

**Description:**
Brief description of what this PR does.

**Changes Made:**
- [ ] Feature/fix implementation
- [ ] Tests added/updated
- [ ] Documentation updated
- [ ] Code formatted and linted

**Testing:**
- [ ] iOS tested
- [ ] Android tested
- [ ] Web tested
- [ ] All tests pass

**Screenshots/Videos:**
If applicable, add screenshots or videos of the changes.

**Related Issues:**
Fixes #(issue number)

**Checklist:**
- [ ] My code follows the project's style guidelines
- [ ] I have performed a self-review of my code
- [ ] I have commented my code where necessary
- [ ] I have made corresponding changes to documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests that prove my fix/feature works
- [ ] New and existing tests pass locally
```

### Review Process

1. **Automated Checks**: CI will run tests and checks automatically
2. **Code Review**: Maintainers will review your code
3. **Feedback**: Address any requested changes
4. **Approval**: Once approved, your PR will be merged

### After Merge

- Your contribution will be included in the next release
- You'll be added to the contributors list
- Consider helping others with their PRs!

---

## 🧪 Testing

### Test Types

#### Unit Tests
```bash
# Run unit tests
bun test

# Run with coverage
bun test --coverage

# Watch mode
bun test --watch
```

#### Integration Tests
```bash
# Run integration tests
bun test:integration
```

#### E2E Tests
```bash
# Run end-to-end tests
bun test:e2e
```

### Writing Tests

#### Example Unit Test
```typescript
// __tests__/utils/formatDuration.test.ts
import { formatDuration } from '../src/utils/formatDuration'

describe('formatDuration', () => {
  it('should format duration correctly', () => {
    expect(formatDuration(90)).toBe('1h 30m')
    expect(formatDuration(45)).toBe('45m')
    expect(formatDuration(0)).toBe('0m')
  })

  it('should handle edge cases', () => {
    expect(formatDuration(-1)).toBe('0m')
    expect(formatDuration(1440)).toBe('24h 0m')
  })
})
```

#### Example Component Test
```typescript
// __tests__/components/MovieCard.test.tsx
import { render, screen, fireEvent } from '@testing-library/react-native'
import { MovieCard } from '../src/components/MovieCard'

const mockMovie = {
  id: '1',
  title: 'Test Movie',
  year: 2023,
  rating: 8.5,
  poster: 'https://example.com/poster.jpg'
}

describe('MovieCard', () => {
  it('should render movie information', () => {
    render(<MovieCard movie={mockMovie} />)

    expect(screen.getByText('Test Movie')).toBeTruthy()
    expect(screen.getByText('2023')).toBeTruthy()
  })

  it('should call onPress when tapped', () => {
    const onPress = jest.fn()
    render(<MovieCard movie={mockMovie} onPress={onPress} />)

    fireEvent.press(screen.getByText('Test Movie'))
    expect(onPress).toHaveBeenCalledWith(mockMovie)
  })
})
```

---

## 📚 Documentation

### Types of Documentation

#### Code Documentation
- Use JSDoc comments for functions and classes
- Write clear variable and function names
- Add inline comments for complex logic

```typescript
/**
 * Formats movie duration from minutes to human-readable string
 * @param minutes - Duration in minutes
 * @returns Formatted duration string (e.g., "2h 30m")
 * @example
 * ```typescript
 * formatDuration(150) // Returns "2h 30m"
 * formatDuration(45)  // Returns "45m"
 * ```
 */
export function formatDuration(minutes: number): string {
  if (minutes <= 0) return '0m'

  const hours = Math.floor(minutes / 60)
  const mins = minutes % 60

  return hours > 0 ? `${hours}h ${mins}m` : `${mins}m`
}
```

#### README Updates
- Update README.md when adding major features
- Keep installation instructions current
- Add examples for new functionality

#### API Documentation
- Document new APIs in `API.md`
- Include usage examples
- Specify parameter types and return values

#### Architecture Documentation
- Update `ARCHITECTURE.md` for structural changes
- Document design decisions
- Explain complex systems

---

## 🎨 Code Style

### Formatting

We use **Biome** via **Ultracite** for consistent code formatting:

```bash
# Format code
bun cq:fix

# Check formatting
bun cq:check
```

### TypeScript Guidelines

#### ✅ Do's
```typescript
// Use explicit types for public APIs
interface MovieSearchResult {
  readonly id: string
  readonly title: string
  readonly year: number
}

// Use const assertions for immutable data
const GENRES = ['action', 'comedy', 'drama'] as const
type Genre = typeof GENRES[number]

// Use utility types
type CreateMovieRequest = Omit<Movie, 'id' | 'createdAt'>

// Use proper error handling
type Result<T, E = Error> =
  | { success: true; data: T }
  | { success: false; error: E }
```

#### ❌ Don'ts
```typescript
// Don't use any
const processData = (data: any) => { /* ... */ }

// Don't use function declarations (use arrow functions)
function processMovie() { /* ... */ }

// Don't use var
var movieTitle = 'Example'

// Don't ignore TypeScript errors
// @ts-ignore
const result = someFunction()
```

### React Native Guidelines

#### Component Structure
```typescript
// Good component structure
interface MovieCardProps {
  readonly movie: Movie
  readonly onPress?: (movie: Movie) => void
  readonly variant?: 'default' | 'compact'
}

export const MovieCard = memo<MovieCardProps>(({
  movie,
  onPress,
  variant = 'default'
}) => {
  const handlePress = useCallback(() => {
    onPress?.(movie)
  }, [movie, onPress])

  return (
    <Pressable onPress={handlePress}>
      <Text>{movie.title}</Text>
    </Pressable>
  )
})

MovieCard.displayName = 'MovieCard'
```

#### Hooks Guidelines
```typescript
// Custom hook example
export function useMovieSearch(query: string) {
  const [results, setResults] = useState<SearchResult[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const search = useCallback(async () => {
    if (!query.trim()) {
      setResults([])
      return
    }

    setLoading(true)
    setError(null)

    try {
      const searchResults = await movieProvider.search(query)
      setResults(searchResults)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Search failed')
    } finally {
      setLoading(false)
    }
  }, [query])

  useEffect(() => {
    search()
  }, [search])

  return { results, loading, error, refetch: search }
}
```

---

## 🛡️ Code of Conduct

### Our Pledge

We are committed to providing a welcoming and inclusive experience for everyone. Please read our [Code of Conduct](CODE_OF_CONDUCT.md) before participating.

### Expected Behavior

- **Be Respectful**: Treat everyone with respect and kindness
- **Be Inclusive**: Welcome newcomers and help them get started
- **Be Constructive**: Provide helpful feedback and suggestions
- **Be Professional**: Keep discussions focused and productive

### Unacceptable Behavior

- Harassment, discrimination, or offensive language
- Personal attacks or trolling
- Spam or off-topic content
- Sharing private information without permission

### Reporting Issues

If you experience or witness unacceptable behavior:

1. **Document the incident**
2. **Report to maintainers** via email or GitHub
3. **Provide evidence** if available
4. **Be patient** while we investigate

---

## ❓ Questions

### Getting Help

- **📚 Documentation**: Check our documentation first
- **💬 Discussions**: Use [GitHub Discussions](https://github.com/Act-Aks/dreamstream/discussions)
- **💬 Discord**: Join our community chat
- **📧 Email**: Contact maintainers directly

### FAQ

#### Q: How do I set up the development environment?
A: Follow the [Development Guide](DEVELOPMENT.md) for detailed setup instructions.

#### Q: Can I work on multiple issues at once?
A: We recommend focusing on one issue at a time to ensure quality work.

#### Q: How long does code review take?
A: We aim to review PRs within 48-72 hours, but it may take longer for complex changes.

#### Q: Can I contribute if I'm a beginner?
A: Absolutely! Look for issues labeled `good first issue` and don't hesitate to ask for help.

#### Q: How do I become a maintainer?
A: Regular contributors who demonstrate expertise and commitment may be invited to become maintainers.

---

## 🎉 Recognition

### Contributors

All contributors will be recognized in:

- **README Contributors Section**
- **Release Notes**
- **Annual Contributor Highlights**

### Contribution Types

We recognize all types of contributions using the [All Contributors](https://allcontributors.org/) specification:

- 💻 Code
- 📖 Documentation
- 🐛 Bug reports
- 💡 Ideas & Planning
- 🎨 Design
- 🧪 Tests
- 🌍 Translation
- ❓ Answering Questions
- 📢 Talks & Promotion

---

<div align="center">

**Thank You for Contributing to DreamStream!**

*Together, we're building the future of entertainment discovery*

[![Contributors](https://contrib.rocks/image?repo=Act-Aks/dreamstream)](https://github.com/Act-Aks/dreamstream/graphs/contributors)

**[⬆ Back to Top](#-contributing-to-dreamstream)**

</div>
