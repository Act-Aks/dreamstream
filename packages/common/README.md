# @dreamstream/common

<div align="center">

**Shared utilities, types, and constants for DreamStream**

[![npm version](https://img.shields.io/npm/v/@dreamstream/common.svg)](https://www.npmjs.com/package/@dreamstream/common)
[![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

*Core utilities and type definitions used across the DreamStream ecosystem*

</div>

---

## 📋 Table of Contents

- [🎯 Overview](#-overview)
- [📦 Installation](#-installation)
- [🏗️ Structure](#️-structure)
- [🔧 API Reference](#-api-reference)
- [📝 Usage Examples](#-usage-examples)
- [🧪 Testing](#-testing)
- [🤝 Contributing](#-contributing)

---

## 🎯 Overview

The `@dreamstream/common` package provides shared utilities, TypeScript definitions, and constants used throughout the DreamStream application. It serves as the foundation for type safety and consistent data handling across all packages and apps.

### Key Features

- 🔒 **Type Safety**: Comprehensive TypeScript definitions for all data structures
- ⚡ **Utility Functions**: Common formatting, validation, and helper functions
- 📊 **Constants**: Application-wide configuration and theme constants
- 🌐 **HTTP Utilities**: Configured HTTP client and request helpers
- 💾 **Storage Utilities**: Consistent local storage abstractions

---

## 📦 Installation

```bash
# Using Bun (recommended)
bun add @dreamstream/common

# Using npm
npm install @dreamstream/common

# Using yarn
yarn add @dreamstream/common
```

### Development Dependencies

```bash
# Install development dependencies
bun install
```

---

## 🏗️ Structure

```
packages/common/
├── src/
│   ├── types/           # TypeScript definitions
│   │   ├── movie.ts     # Movie-related types
│   │   ├── series.ts    # Series-related types
│   │   ├── user.ts      # User-related types
│   │   └── api.ts       # API response types
│   ├── utils/           # Utility functions
│   │   ├── format.ts    # Data formatting utilities
│   │   ├── validation.ts # Input validation functions
│   │   ├── storage.ts   # Storage utilities
│   │   └── http.ts      # HTTP client utilities
│   ├── constants/       # Application constants
│   │   ├── api.ts       # API endpoints
│   │   ├── config.ts    # Configuration constants
│   │   └── theme.ts     # Theme and styling constants
│   └── index.ts         # Main package exports
├── package.json
├── tsconfig.json
└── README.md
```

---

## 🔧 API Reference

### Types

#### Media Types

```typescript
// Base media interface
interface BaseMedia {
  readonly id: string
  readonly title: string
  readonly year: number
  readonly genre: string[]
  readonly rating: number
  readonly poster: string | null
  readonly backdrop: string | null
  readonly overview: string
}

// Movie interface
interface Movie extends BaseMedia {
  readonly duration: number
  readonly releaseDate: string
  readonly status: 'released' | 'upcoming' | 'in-production'
}

// Series interface
interface Series extends BaseMedia {
  readonly seasons: Season[]
  readonly status: 'ongoing' | 'completed' | 'cancelled'
  readonly firstAirDate: string
  readonly numberOfSeasons: number
  readonly numberOfEpisodes: number
}
```

#### Search Types

```typescript
// Search result interface
interface SearchResult {
  readonly id: string
  readonly title: string
  readonly year: number
  readonly type: 'movie' | 'series'
  readonly poster: string | null
  readonly relevance?: number
}

// Search response interface
interface SearchResponse {
  readonly results: SearchResult[]
  readonly page: number
  readonly totalPages: number
  readonly totalResults: number
  readonly query: string
}
```

### Utility Functions

#### Formatting Utilities

```typescript
/**
 * Format duration from minutes to readable string
 * @param minutes - Duration in minutes
 * @returns Formatted duration string
 */
function formatDuration(minutes: number): string

/**
 * Format year from ISO date string
 * @param dateString - ISO date string
 * @returns Year as number
 */
function formatYear(dateString: string): number

/**
 * Format rating to fixed decimal places
 * @param rating - Rating number
 * @param decimals - Number of decimal places
 * @returns Formatted rating string
 */
function formatRating(rating: number, decimals?: number): string
```

#### Validation Utilities

```typescript
/**
 * Validate movie/series ID
 * @param id - ID to validate
 * @returns True if valid ID
 */
function isValidMovieId(id: string): boolean

/**
 * Validate year
 * @param year - Year to validate
 * @returns True if valid year
 */
function isValidYear(year: number): boolean

/**
 * Validate URL
 * @param url - URL to validate
 * @returns True if valid URL
 */
function isValidUrl(url: string): boolean
```

#### HTTP Utilities

```typescript
/**
 * Create HTTP client with default configuration
 * @param baseURL - Base URL for requests
 * @param timeout - Request timeout in milliseconds
 * @returns Configured Axios instance
 */
function createApiClient(baseURL: string, timeout?: number): AxiosInstance
```

### Constants

#### API Constants

```typescript
const API_ENDPOINTS = {
  SEARCH: '/search',
  MOVIE: '/movie',
  SERIES: '/series',
  TRENDING: '/trending'
} as const
```

#### Configuration Constants

```typescript
const CONFIG = {
  API_TIMEOUT: 10000,
  MAX_RETRIES: 3,
  CACHE_DURATION: 5 * 60 * 1000, // 5 minutes
  ITEMS_PER_PAGE: 20
} as const
```

#### Theme Constants

```typescript
const COLORS = {
  PRIMARY: '#6366F1',
  SECONDARY: '#8B5CF6',
  SUCCESS: '#10B981',
  ERROR: '#EF4444',
  WARNING: '#F59E0B',
  BACKGROUND: '#0F172A',
  SURFACE: '#1E293B',
  TEXT: '#F8FAFC'
} as const

const SPACING = {
  XS: 4,
  SM: 8,
  MD: 16,
  LG: 24,
  XL: 32,
  XXL: 48
} as const
```

---

## 📝 Usage Examples

### Basic Import and Usage

```typescript
import {
  Movie,
  formatDuration,
  formatRating,
  isValidMovieId,
  COLORS,
  SPACING,
  createApiClient
} from '@dreamstream/common'

// Using types
const movie: Movie = {
  id: 'movie-123',
  title: 'The Matrix',
  year: 1999,
  duration: 136,
  rating: 8.7,
  // ... other properties
}

// Using utility functions
const formattedDuration = formatDuration(movie.duration) // "2h 16m"
const formattedRating = formatRating(movie.rating) // "8.7"
const isValidId = isValidMovieId(movie.id) // true

// Using constants
const primaryColor = COLORS.PRIMARY // "#6366F1"
const defaultSpacing = SPACING.MD // 16

// Creating HTTP client
const apiClient = createApiClient('https://api.example.com')
```

### React Native Component Example

```typescript
import React from 'react'
import { View, Text, StyleSheet } from 'react-native'
import {
  Movie,
  formatDuration,
  formatRating,
  COLORS,
  SPACING
} from '@dreamstream/common'

interface MovieCardProps {
  movie: Movie
}

export const MovieCard: React.FC<MovieCardProps> = ({ movie }) => {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>{movie.title}</Text>
      <Text style={styles.metadata}>
        {movie.year} • {formatDuration(movie.duration)} • ⭐ {formatRating(movie.rating)}
      </Text>
      <Text style={styles.overview} numberOfLines={3}>
        {movie.overview}
      </Text>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: COLORS.SURFACE,
    padding: SPACING.MD,
    borderRadius: 12,
    marginBottom: SPACING.SM
  },
  title: {
    color: COLORS.TEXT,
    fontSize: 18,
    fontWeight: '600',
    marginBottom: SPACING.XS
  },
  metadata: {
    color: COLORS.TEXT_SECONDARY,
    fontSize: 14,
    marginBottom: SPACING.SM
  },
  overview: {
    color: COLORS.TEXT,
    fontSize: 14,
    lineHeight: 20
  }
})
```

### Validation Example

```typescript
import {
  isValidMovieId,
  isValidYear,
  isValidUrl,
  ValidationError
} from '@dreamstream/common'

function validateMovieData(data: any) {
  const errors: string[] = []

  if (!data.id || !isValidMovieId(data.id)) {
    errors.push('Invalid movie ID')
  }

  if (!data.year || !isValidYear(data.year)) {
    errors.push('Invalid release year')
  }

  if (data.poster && !isValidUrl(data.poster)) {
    errors.push('Invalid poster URL')
  }

  if (errors.length > 0) {
    throw new ValidationError(`Validation failed: ${errors.join(', ')}`)
  }

  return true
}
```

### HTTP Client Example

```typescript
import { createApiClient, ApiResponse } from '@dreamstream/common'

const apiClient = createApiClient('https://api.moviedb.com', 15000)

// Add request interceptor
apiClient.interceptors.request.use(request => {
  console.log('Making request:', request.url)
  return request
})

// Add response interceptor
apiClient.interceptors.response.use(
  response => response,
  error => {
    console.error('API Error:', error.message)
    return Promise.reject(error)
  }
)

// Usage in service
export async function searchMovies(query: string): Promise<SearchResult[]> {
  try {
    const response = await apiClient.get<ApiResponse<SearchResult[]>>('/search', {
      params: { q: query }
    })

    if (response.data.success) {
      return response.data.data || []
    }

    throw new Error(response.data.error || 'Search failed')
  } catch (error) {
    console.error('Search error:', error)
    return []
  }
}
```

---

## 🧪 Testing

### Running Tests

```bash
# Run all tests
bun test

# Run tests in watch mode
bun test --watch

# Run tests with coverage
bun test --coverage
```

### Test Examples

#### Unit Test Example

```typescript
// __tests__/utils/formatDuration.test.ts
import { formatDuration } from '../src/utils/format'

describe('formatDuration', () => {
  it('should format duration correctly', () => {
    expect(formatDuration(90)).toBe('1h 30m')
    expect(formatDuration(45)).toBe('45m')
    expect(formatDuration(120)).toBe('2h 0m')
  })

  it('should handle edge cases', () => {
    expect(formatDuration(0)).toBe('0m')
    expect(formatDuration(-1)).toBe('0m')
    expect(formatDuration(1440)).toBe('24h 0m')
  })
})
```

#### Validation Test Example

```typescript
// __tests__/utils/validation.test.ts
import { isValidMovieId, isValidYear } from '../src/utils/validation'

describe('validation utilities', () => {
  describe('isValidMovieId', () => {
    it('should validate correct movie IDs', () => {
      expect(isValidMovieId('movie-123')).toBe(true)
      expect(isValidMovieId('abc123')).toBe(true)
      expect(isValidMovieId('test_movie')).toBe(true)
    })

    it('should reject invalid movie IDs', () => {
      expect(isValidMovieId('')).toBe(false)
      expect(isValidMovieId('movie with spaces')).toBe(false)
      expect(isValidMovieId('movie@123')).toBe(false)
    })
  })

  describe('isValidYear', () => {
    it('should validate reasonable years', () => {
      expect(isValidYear(2023)).toBe(true)
      expect(isValidYear(1900)).toBe(true)
      expect(isValidYear(2030)).toBe(true)
    })

    it('should reject unreasonable years', () => {
      expect(isValidYear(1800)).toBe(false)
      expect(isValidYear(2050)).toBe(false)
      expect(isValidYear(-1)).toBe(false)
    })
  })
})
```

---

## 🤝 Contributing

We welcome contributions to the common package! Please see our [Contributing Guide](../../CONTRIBUTING.md) for details.

### Development Setup

```bash
# Install dependencies
bun install

# Run tests
bun test

# Format code
bun format

# Lint code
bun lint
```

### Adding New Utilities

1. Create your utility function in the appropriate file under `src/utils/`
2. Export it from `src/index.ts`
3. Add comprehensive tests in `__tests__/`
4. Update this README with usage examples
5. Submit a pull request

### Adding New Types

1. Define types in the appropriate file under `src/types/`
2. Export from `src/index.ts`
3. Add JSDoc documentation
4. Include usage examples
5. Update API documentation

---

<div align="center">

**Made with ❤️ by the DreamStream team**

[Documentation](../../docs/) • [Contributing](../../CONTRIBUTING.md) • [License](../../LICENSE)

</div>
