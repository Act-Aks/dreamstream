# 🔌 API Reference

<div align="center">

**DreamStream API Documentation**

*Complete reference for all APIs, interfaces, and data structures*

</div>

---

## 📋 Table of Contents

- [🎯 Overview](#-overview)
- [🏗️ Core Types](#️-core-types)
- [📱 App API](#-app-api)
- [📦 Common Package](#-common-package)
- [🔌 Providers Package](#-providers-package)
- [🎨 UI Components](#-ui-components)
- [📊 State Management](#-state-management)
- [🔧 Utilities](#-utilities)
- [❌ Error Handling](#-error-handling)

---

## 🎯 Overview

The DreamStream API is designed with TypeScript-first principles, ensuring type safety across the entire application. All APIs follow consistent patterns for error handling, data validation, and response formats.

### API Design Principles

- **🔒 Type Safety**: Full TypeScript coverage
- **📝 Consistent Naming**: Clear, descriptive function names
- **⚡ Performance**: Optimized for speed and memory usage
- **🛡️ Error Handling**: Comprehensive error management
- **📚 Documentation**: JSDoc comments for all public APIs

---

## 🏗️ Core Types

### Media Types

```typescript
/**
 * Base interface for all media content
 */
interface BaseMedia {
  /** Unique identifier for the content */
  readonly id: string
  /** Content title */
  readonly title: string
  /** Release year */
  readonly year: number
  /** Content genres */
  readonly genre: string[]
  /** User/critic rating (0-10) */
  readonly rating: number
  /** Poster image URL */
  readonly poster: string | null
  /** Backdrop image URL */
  readonly backdrop: string | null
  /** Content overview/description */
  readonly overview: string
}

/**
 * Movie data structure
 */
interface Movie extends BaseMedia {
  /** Movie duration in minutes */
  readonly duration: number
  /** Release date in ISO format */
  readonly releaseDate: string
  /** Production budget (optional) */
  readonly budget?: number
  /** Box office revenue (optional) */
  readonly revenue?: number
  /** Movie status */
  readonly status: 'released' | 'upcoming' | 'in-production'
}

/**
 * TV Series data structure
 */
interface Series extends BaseMedia {
  /** All seasons */
  readonly seasons: Season[]
  /** Series status */
  readonly status: 'ongoing' | 'completed' | 'cancelled'
  /** First air date */
  readonly firstAirDate: string
  /** Last air date (if completed) */
  readonly lastAirDate?: string
  /** Number of seasons */
  readonly numberOfSeasons: number
  /** Number of episodes */
  readonly numberOfEpisodes: number
}

/**
 * Season data structure
 */
interface Season {
  /** Unique season identifier */
  readonly id: string
  /** Season number */
  readonly number: number
  /** Season title/name */
  readonly title: string
  /** All episodes in season */
  readonly episodes: Episode[]
  /** Season overview */
  readonly overview: string
  /** Season poster */
  readonly poster: string | null
  /** Air date */
  readonly airDate: string
}

/**
 * Episode data structure
 */
interface Episode {
  /** Unique episode identifier */
  readonly id: string
  /** Episode number within season */
  readonly number: number
  /** Episode title */
  readonly title: string
  /** Episode overview */
  readonly overview: string
  /** Episode duration in minutes */
  readonly duration: number
  /** Air date in ISO format */
  readonly airDate: string
  /** Episode rating */
  readonly rating?: number
  /** Episode thumbnail */
  readonly thumbnail?: string
}
```

### Search & Results

```typescript
/**
 * Search result item
 */
interface SearchResult {
  /** Content identifier */
  readonly id: string
  /** Content title */
  readonly title: string
  /** Release year */
  readonly year: number
  /** Content type */
  readonly type: 'movie' | 'series'
  /** Poster image URL */
  readonly poster: string | null
  /** Search relevance score (0-1) */
  readonly relevance?: number
}

/**
 * Paginated search results
 */
interface SearchResponse {
  /** Search results */
  readonly results: SearchResult[]
  /** Current page number */
  readonly page: number
  /** Total number of pages */
  readonly totalPages: number
  /** Total number of results */
  readonly totalResults: number
  /** Query that was searched */
  readonly query: string
}

/**
 * Search filters
 */
interface SearchFilters {
  /** Content type filter */
  readonly type?: 'movie' | 'series' | 'all'
  /** Genre filter */
  readonly genre?: string[]
  /** Year range filter */
  readonly yearRange?: {
    readonly min: number
    readonly max: number
  }
  /** Rating filter (minimum) */
  readonly minRating?: number
  /** Sort criteria */
  readonly sortBy?: 'relevance' | 'year' | 'rating' | 'popularity'
  /** Sort order */
  readonly sortOrder?: 'asc' | 'desc'
}
```

### Streaming & Links

```typescript
/**
 * Streaming link data
 */
interface StreamingLink {
  /** Link URL */
  readonly url: string
  /** Video quality */
  readonly quality: '480p' | '720p' | '1080p' | '1440p' | '2160p' | 'unknown'
  /** Video format */
  readonly format: 'mp4' | 'webm' | 'mkv' | 'm3u8' | 'unknown'
  /** Link type */
  readonly type: 'direct' | 'embed' | 'torrent'
  /** Provider name */
  readonly provider: string
  /** Link reliability score (0-1) */
  readonly reliability?: number
  /** Language/subtitle info */
  readonly language?: string
  /** File size in bytes (if available) */
  readonly size?: number
}

/**
 * Subtitle track
 */
interface SubtitleTrack {
  /** Subtitle language */
  readonly language: string
  /** Language code (ISO 639-1) */
  readonly languageCode: string
  /** Subtitle URL */
  readonly url: string
  /** Subtitle format */
  readonly format: 'srt' | 'vtt' | 'ass' | 'ssa'
}
```

---

## 📱 App API

### Navigation

```typescript
/**
 * Navigation parameters for different screens
 */
interface NavigationParams {
  /** Movie detail screen */
  'movie/[id]': {
    readonly id: string
    readonly movie?: Movie
  }

  /** Series detail screen */
  'series/[id]': {
    readonly id: string
    readonly series?: Series
  }

  /** Episode detail screen */
  'episode/[id]': {
    readonly id: string
    readonly episode?: Episode
    readonly series?: Series
  }

  /** Video player screen */
  'player/[...params]': {
    readonly id: string
    readonly type: 'movie' | 'episode'
    readonly title: string
    readonly streamingLinks: StreamingLink[]
  }
}

/**
 * Navigation hook
 */
declare function useRouter(): {
  /** Navigate to screen */
  push: <T extends keyof NavigationParams>(
    route: T,
    params: NavigationParams[T]
  ) => void

  /** Go back */
  back: () => void

  /** Replace current screen */
  replace: <T extends keyof NavigationParams>(
    route: T,
    params: NavigationParams[T]
  ) => void
}
```

### Custom Hooks

```typescript
/**
 * Movie search hook
 */
declare function useMovieSearch(query: string, filters?: SearchFilters): {
  /** Search results */
  readonly results: SearchResult[]
  /** Loading state */
  readonly loading: boolean
  /** Error state */
  readonly error: string | null
  /** Refetch function */
  readonly refetch: () => void
}

/**
 * Movie details hook
 */
declare function useMovieDetails(id: string): {
  /** Movie data */
  readonly movie: Movie | null
  /** Loading state */
  readonly loading: boolean
  /** Error state */
  readonly error: string | null
  /** Refetch function */
  readonly refetch: () => void
}

/**
 * Streaming links hook
 */
declare function useStreamingLinks(
  id: string,
  type: 'movie' | 'series'
): {
  /** Available streaming links */
  readonly links: StreamingLink[]
  /** Loading state */
  readonly loading: boolean
  /** Error state */
  readonly error: string | null
  /** Refetch function */
  readonly refetch: () => void
}

/**
 * Favorites management hook
 */
declare function useFavorites(): {
  /** User's favorite items */
  readonly favorites: string[]
  /** Add item to favorites */
  readonly addToFavorites: (id: string) => void
  /** Remove item from favorites */
  readonly removeFromFavorites: (id: string) => void
  /** Check if item is favorited */
  readonly isFavorite: (id: string) => boolean
}

/**
 * Watchlist management hook
 */
declare function useWatchlist(): {
  /** User's watchlist */
  readonly watchlist: string[]
  /** Add item to watchlist */
  readonly addToWatchlist: (id: string) => void
  /** Remove item from watchlist */
  readonly removeFromWatchlist: (id: string) => void
  /** Check if item is in watchlist */
  readonly isInWatchlist: (id: string) => boolean
}
```

---

## 📦 Common Package

### Formatting Utilities

```typescript
/**
 * Format duration from minutes to readable string
 * @param minutes - Duration in minutes
 * @returns Formatted duration string
 * @example
 * formatDuration(90) // "1h 30m"
 * formatDuration(45) // "45m"
 */
declare function formatDuration(minutes: number): string

/**
 * Format year from ISO date string
 * @param dateString - ISO date string
 * @returns Year as number
 * @example
 * formatYear("2023-05-15T00:00:00.000Z") // 2023
 */
declare function formatYear(dateString: string): number

/**
 * Format rating to fixed decimal places
 * @param rating - Rating number
 * @param decimals - Number of decimal places (default: 1)
 * @returns Formatted rating string
 * @example
 * formatRating(8.567) // "8.6"
 * formatRating(7.0, 0) // "7"
 */
declare function formatRating(rating: number, decimals?: number): string

/**
 * Format file size to human readable string
 * @param bytes - File size in bytes
 * @returns Formatted size string
 * @example
 * formatFileSize(1024) // "1 KB"
 * formatFileSize(1048576) // "1 MB"
 */
declare function formatFileSize(bytes: number): string
```

### Validation Utilities

```typescript
/**
 * Validate movie/series ID
 * @param id - ID to validate
 * @returns True if valid ID
 */
declare function isValidMovieId(id: string): boolean

/**
 * Validate year
 * @param year - Year to validate
 * @returns True if valid year
 */
declare function isValidYear(year: number): boolean

/**
 * Validate URL
 * @param url - URL to validate
 * @returns True if valid URL
 */
declare function isValidUrl(url: string): boolean

/**
 * Validate email address
 * @param email - Email to validate
 * @returns True if valid email
 */
declare function isValidEmail(email: string): boolean
```

### Storage Utilities

```typescript
/**
 * Create consistent storage key
 * @param prefix - Key prefix
 * @param id - Item identifier
 * @returns Storage key string
 */
declare function createStorageKey(prefix: string, id: string): string

/**
 * Storage interface
 */
interface Storage {
  /** Get item from storage */
  getItem(key: string): Promise<string | null>
  /** Set item in storage */
  setItem(key: string, value: string): Promise<void>
  /** Remove item from storage */
  removeItem(key: string): Promise<void>
  /** Clear all storage */
  clear(): Promise<void>
}

/**
 * Create MMKV storage instance
 */
declare function createStorage(): Storage
```

### HTTP Utilities

```typescript
/**
 * HTTP client configuration
 */
interface HttpConfig {
  /** Base URL */
  readonly baseURL?: string
  /** Request timeout in milliseconds */
  readonly timeout?: number
  /** Default headers */
  readonly headers?: Record<string, string>
  /** Maximum redirects */
  readonly maxRedirects?: number
  /** Retry configuration */
  readonly retries?: {
    readonly count: number
    readonly delay: number
  }
}

/**
 * Create HTTP client
 * @param config - Client configuration
 * @returns Configured HTTP client
 */
declare function createHttpClient(config?: HttpConfig): AxiosInstance

/**
 * HTTP response wrapper
 */
interface ApiResponse<T = unknown> {
  /** Request success status */
  readonly success: boolean
  /** Response data */
  readonly data?: T
  /** Error message */
  readonly error?: string
  /** Additional message */
  readonly message?: string
  /** HTTP status code */
  readonly statusCode?: number
}
```

---

## 🔌 Providers Package

### Base Provider

```typescript
/**
 * Provider configuration options
 */
interface ProviderConfig {
  /** Request timeout in milliseconds */
  readonly timeout?: number
  /** Number of retry attempts */
  readonly retries?: number
  /** Rate limit (requests per minute) */
  readonly rateLimit?: number
  /** Custom headers */
  readonly headers?: Record<string, string>
}

/**
 * Abstract base provider class
 */
abstract class BaseProvider {
  /** Provider display name */
  abstract readonly name: string

  /** Provider base URL */
  abstract readonly baseUrl: string

  /**
   * Search for content
   * @param query - Search query
   * @param filters - Search filters
   * @returns Search results
   */
  abstract search(
    query: string,
    filters?: SearchFilters
  ): Promise<SearchResult[]>

  /**
   * Get movie details
   * @param id - Movie ID
   * @returns Movie data or null if not found
   */
  abstract getMovie(id: string): Promise<Movie | null>

  /**
   * Get series details
   * @param id - Series ID
   * @returns Series data or null if not found
   */
  abstract getSeries(id: string): Promise<Series | null>

  /**
   * Get streaming links
   * @param id - Content ID
   * @param type - Content type
   * @returns Available streaming links
   */
  abstract getStreamingLinks(
    id: string,
    type: 'movie' | 'series'
  ): Promise<StreamingLink[]>

  /**
   * Make HTTP request
   * @param url - Request URL
   * @param options - Request options
   * @returns Response body
   */
  protected makeRequest(
    url: string,
    options?: RequestInit
  ): Promise<string>

  /**
   * Parse HTML content
   * @param html - HTML string
   * @returns Cheerio instance
   */
  protected parseHtml(html: string): CheerioStatic

  /**
   * Get request headers
   * @returns Default headers
   */
  protected getHeaders(): Record<string, string>
}
```

### Provider Registry

```typescript
/**
 * Provider registry interface
 */
interface ProviderRegistry {
  /**
   * Register a provider
   * @param provider - Provider instance
   */
  register(provider: BaseProvider): void

  /**
   * Get all registered providers
   * @returns Array of providers
   */
  getAll(): BaseProvider[]

  /**
   * Get provider by name
   * @param name - Provider name
   * @returns Provider instance or null
   */
  getByName(name: string): BaseProvider | null

  /**
   * Search across all providers
   * @param query - Search query
   * @param filters - Search filters
   * @returns Aggregated results
   */
  search(
    query: string,
    filters?: SearchFilters
  ): Promise<SearchResult[]>
}

/**
 * Create provider registry
 * @returns Registry instance
 */
declare function createProviderRegistry(): ProviderRegistry
```

### Rate Limiting

```typescript
/**
 * Rate limiter configuration
 */
interface RateLimiterConfig {
  /** Maximum requests per time window */
  readonly maxRequests: number
  /** Time window in milliseconds */
  readonly timeWindow: number
}

/**
 * Rate limiter class
 */
declare class RateLimiter {
  constructor(config: RateLimiterConfig)

  /**
   * Execute function with rate limiting
   * @param fn - Function to execute
   * @returns Function result
   */
  execute<T>(fn: () => Promise<T>): Promise<T>
}
```

---

## 🎨 UI Components

### Component Props

```typescript
/**
 * Base component props
 */
interface BaseComponentProps {
  /** Custom style */
  readonly style?: ViewStyle | TextStyle
  /** Accessibility label */
  readonly accessibilityLabel?: string
  /** Accessibility role */
  readonly accessibilityRole?: string
  /** Test ID for testing */
  readonly testID?: string
}

/**
 * Movie card component props
 */
interface MovieCardProps extends BaseComponentProps {
  /** Movie data */
  readonly movie: Movie
  /** Press handler */
  readonly onPress?: (movie: Movie) => void
  /** Card variant */
  readonly variant?: 'default' | 'compact' | 'detailed'
  /** Show favorite button */
  readonly showFavorite?: boolean
  /** Show watchlist button */
  readonly showWatchlist?: boolean
}

/**
 * Search bar component props
 */
interface SearchBarProps extends BaseComponentProps {
  /** Current search value */
  readonly value: string
  /** Value change handler */
  readonly onChangeText: (text: string) => void
  /** Search handler */
  readonly onSearch: (query: string) => void
  /** Placeholder text */
  readonly placeholder?: string
  /** Loading state */
  readonly loading?: boolean
}

/**
 * Video player component props
 */
interface VideoPlayerProps extends BaseComponentProps {
  /** Video source URL */
  readonly source: string
  /** Video title */
  readonly title: string
  /** Available subtitles */
  readonly subtitles?: SubtitleTrack[]
  /** Playback position (seconds) */
  readonly position?: number
  /** Position change handler */
  readonly onPositionChange?: (position: number) => void
  /** Playback end handler */
  readonly onEnd?: () => void
}
```

### Theme Interface

```typescript
/**
 * Application theme
 */
interface Theme {
  /** Color palette */
  readonly colors: {
    readonly primary: string
    readonly secondary: string
    readonly background: string
    readonly surface: string
    readonly text: string
    readonly textSecondary: string
    readonly border: string
    readonly error: string
    readonly success: string
    readonly warning: string
  }

  /** Spacing values */
  readonly spacing: {
    readonly xs: number
    readonly sm: number
    readonly md: number
    readonly lg: number
    readonly xl: number
    readonly xxl: number
  }

  /** Typography styles */
  readonly typography: {
    readonly heading1: TextStyle
    readonly heading2: TextStyle
    readonly heading3: TextStyle
    readonly body: TextStyle
    readonly caption: TextStyle
  }

  /** Border radius values */
  readonly borderRadius: {
    readonly sm: number
    readonly md: number
    readonly lg: number
    readonly xl: number
  }
}

/**
 * Theme hook
 */
declare function useTheme(): Theme
```

---

## 📊 State Management

### Zustand Store

```typescript
/**
 * Application state interface
 */
interface AppState {
  // User preferences
  /** Current theme mode */
  readonly theme: 'light' | 'dark' | 'auto'
  /** Application language */
  readonly language: string

  // Content state
  /** Cached movies */
  readonly movies: Record<string, Movie>
  /** Cached series */
  readonly series: Record<string, Series>
  /** User favorites */
  readonly favorites: string[]
  /** User watchlist */
  readonly watchlist: string[]

  // UI state
  /** Global loading state */
  readonly loading: boolean
  /** Global error message */
  readonly error: string | null

  // Actions
  /** Set theme mode */
  setTheme(theme: 'light' | 'dark' | 'auto'): void
  /** Set language */
  setLanguage(language: string): void
  /** Cache movie data */
  setMovie(movie: Movie): void
  /** Cache series data */
  setSeries(series: Series): void
  /** Add to favorites */
  addToFavorites(id: string): void
  /** Remove from favorites */
  removeFromFavorites(id: string): void
  /** Add to watchlist */
  addToWatchlist(id: string): void
  /** Remove from watchlist */
  removeFromWatchlist(id: string): void
  /** Set loading state */
  setLoading(loading: boolean): void
  /** Set error state */
  setError(error: string | null): void
}

/**
 * Store hook
 */
declare function useAppStore(): AppState

/**
 * Store selector hook
 */
declare function useAppStore<T>(selector: (state: AppState) => T): T
```

### Store Slices

```typescript
/**
 * User slice state and actions
 */
interface UserSlice {
  readonly favorites: string[]
  readonly watchlist: string[]
  readonly settings: UserSettings

  addToFavorites(id: string): void
  removeFromFavorites(id: string): void
  addToWatchlist(id: string): void
  removeFromWatchlist(id: string): void
  updateSettings(settings: Partial<UserSettings>): void
}

/**
 * Content slice state and actions
 */
interface ContentSlice {
  readonly movies: Record<string, Movie>
  readonly series: Record<string, Series>
  readonly searchResults: SearchResult[]

  setMovie(movie: Movie): void
  setSeries(series: Series): void
  setSearchResults(results: SearchResult[]): void
  clearSearchResults(): void
}
```

---

## 🔧 Utilities

### Date & Time

```typescript
/**
 * Format date to relative time
 * @param date - Date to format
 * @returns Relative time string
 * @example
 * formatRelativeTime(new Date('2023-01-01')) // "2 months ago"
 */
declare function formatRelativeTime(date: Date | string): string

/**
 * Format date to localized string
 * @param date - Date to format
 * @param locale - Locale code
 * @returns Formatted date string
 */
declare function formatDate(date: Date | string, locale?: string): string

/**
 * Parse duration string to minutes
 * @param duration - Duration string (e.g., "1h 30m")
 * @returns Duration in minutes
 */
declare function parseDuration(duration: string): number
```

### String Utilities

```typescript
/**
 * Truncate string with ellipsis
 * @param str - String to truncate
 * @param maxLength - Maximum length
 * @returns Truncated string
 */
declare function truncateString(str: string, maxLength: number): string

/**
 * Convert string to title case
 * @param str - String to convert
 * @returns Title cased string
 */
declare function toTitleCase(str: string): string

/**
 * Remove HTML tags from string
 * @param html - HTML string
 * @returns Plain text
 */
declare function stripHtml(html: string): string

/**
 * Generate slug from string
 * @param str - String to convert
 * @returns URL-safe slug
 */
declare function generateSlug(str: string): string
```

### Array Utilities

```typescript
/**
 * Remove duplicates from array
 * @param array - Array with potential duplicates
 * @param keyFn - Key extraction function
 * @returns Array without duplicates
 */
declare function removeDuplicates<T>(
  array: T[],
  keyFn?: (item: T) => string | number
): T[]

/**
 * Chunk array into smaller arrays
 * @param array - Array to chunk
 * @param size - Chunk size
 * @returns Array of chunks
 */
declare function chunkArray<T>(array: T[], size: number): T[][]

/**
 * Shuffle array elements
 * @param array - Array to shuffle
 * @returns Shuffled array
 */
declare function shuffleArray<T>(array: T[]): T[]
```

---

## ❌ Error Handling

### Error Types

```typescript
/**
 * Base application error
 */
class AppError extends Error {
  constructor(
    message: string,
    public code: string,
    public statusCode?: number
  ) {
    super(message)
    this.name = 'AppError'
  }
}

/**
 * Network-related error
 */
class NetworkError extends AppError {
  constructor(message: string, statusCode?: number) {
    super(message, 'NETWORK_ERROR', statusCode)
    this.name = 'NetworkError'
  }
}

/**
 * Provider-specific error
 */
class ProviderError extends AppError {
  constructor(
    message: string,
    public providerName: string,
    code?: string
  ) {
    super(message, code || 'PROVIDER_ERROR')
    this.name = 'ProviderError'
  }
}

/**
 * Validation error
 */
class ValidationError extends AppError {
  constructor(
    message: string,
    public field?: string
  ) {
    super(message, 'VALIDATION_ERROR')
    this.name = 'ValidationError'
  }
}
```

### Error Handling Utilities

```typescript
/**
 * Error handler function type
 */
type ErrorHandler = (error: Error) => void

/**
 * Global error handler
 */
declare function handleError(error: Error): void

/**
 * Network error handler
 */
declare function handleNetworkError(error: NetworkError): void

/**
 * Create error boundary HOC
 */
declare function withErrorBoundary<P extends object>(
  Component: React.ComponentType<P>
): React.ComponentType<P>

/**
 * Error boundary hook
 */
declare function useErrorBoundary(): {
  readonly error: Error | null
  readonly retry: () => void
}
```

### Result Type

```typescript
/**
 * Result type for operations that can fail
 */
type Result<T, E = Error> = {
  readonly success: true
  readonly data: T
} | {
  readonly success: false
  readonly error: E
}

/**
 * Create success result
 */
declare function success<T>(data: T): Result<T, never>

/**
 * Create error result
 */
declare function failure<E extends Error>(error: E): Result<never, E>

/**
 * Handle result with callbacks
 */
declare function handleResult<T, E extends Error, R>(
  result: Result<T, E>,
  onSuccess: (data: T) => R,
  onError: (error: E) => R
): R
```

---

<div align="center">

**[⬆ Back to Top](#-api-reference)**

*This API documentation is generated from TypeScript definitions and kept in sync with the codebase.*

</div>
