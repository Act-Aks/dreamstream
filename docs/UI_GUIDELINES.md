# 🎨 UI Guidelines & Design System

<div align="center">

**DreamStream Design System Documentation**

*A comprehensive guide to the visual design language and component library*

</div>

---

## 📋 Table of Contents

- [🎯 Design Philosophy](#-design-philosophy)
- [🎨 Color System](#-color-system)
- [📐 Typography](#-typography)
- [📏 Spacing & Layout](#-spacing--layout)
- [🖼️ Iconography](#️-iconography)
- [📱 Components](#-components)
- [🌊 Animations](#-animations)
- [♿ Accessibility](#-accessibility)
- [📱 Responsive Design](#-responsive-design)
- [🌙 Dark Mode](#-dark-mode)

---

## 🎯 Design Philosophy

DreamStream's design system is built around the concept of **cinematic elegance** - creating an immersive entertainment experience that feels both premium and accessible.

### Core Principles

#### 🎬 **Cinematic First**
- Dark themes by default to reduce eye strain during extended viewing
- High contrast ratios for excellent readability
- Rich imagery with subtle overlays and gradients

#### ⚡ **Performance Driven**
- Smooth 60fps animations with React Native Reanimated
- Optimized image loading with progressive enhancement
- Minimal re-renders with efficient state management

#### ♿ **Accessibility Focused**
- WCAG 2.1 AA compliance
- Screen reader optimized
- Large touch targets (minimum 44px)
- High contrast color combinations

#### 📱 **Platform Native**
- iOS and Android specific adaptations
- Respect platform conventions and gestures
- Web-responsive design for all screen sizes

---

## 🎨 Color System

### Primary Palette

```typescript
const colors = {
  // Primary Colors
  primary: {
    50: '#EEF2FF',   // Very light indigo
    100: '#E0E7FF',  // Light indigo
    500: '#6366F1',  // Primary indigo
    600: '#5B21B6',  // Dark indigo
    900: '#312E81'   // Very dark indigo
  },

  // Secondary Colors
  secondary: {
    50: '#F5F3FF',   // Very light purple
    100: '#EDE9FE',  // Light purple
    500: '#8B5CF6',  // Primary purple
    600: '#7C3AED',  // Dark purple
    900: '#581C87'   // Very dark purple
  }
}
```

### Semantic Colors

```typescript
const semanticColors = {
  // Success
  success: {
    light: '#34D399',  // Green 400
    main: '#10B981',   // Green 500
    dark: '#059669'    // Green 600
  },

  // Error
  error: {
    light: '#F87171',  // Red 400
    main: '#EF4444',   // Red 500
    dark: '#DC2626'    // Red 600
  },

  // Warning
  warning: {
    light: '#FBBF24',  // Yellow 400
    main: '#F59E0B',   // Yellow 500
    dark: '#D97706'    // Yellow 600
  },

  // Info
  info: {
    light: '#60A5FA',  // Blue 400
    main: '#3B82F6',   // Blue 500
    dark: '#2563EB'    // Blue 600
  }
}
```

### Theme Colors

#### Dark Theme (Default)
```typescript
const darkTheme = {
  background: '#0F172A',     // Slate 900
  surface: '#1E293B',       // Slate 800
  surfaceVariant: '#334155', // Slate 700
  text: '#F8FAFC',          // Slate 50
  textSecondary: '#94A3B8',  // Slate 400
  textDisabled: '#64748B',   // Slate 500
  border: '#475569',         // Slate 600
  borderLight: '#64748B',    // Slate 500
  overlay: 'rgba(0, 0, 0, 0.8)'
}
```

#### Light Theme
```typescript
const lightTheme = {
  background: '#FFFFFF',     // White
  surface: '#F8FAFC',       // Slate 50
  surfaceVariant: '#F1F5F9', // Slate 100
  text: '#0F172A',          // Slate 900
  textSecondary: '#64748B',  // Slate 500
  textDisabled: '#94A3B8',   // Slate 400
  border: '#CBD5E1',         // Slate 300
  borderLight: '#E2E8F0',    // Slate 200
  overlay: 'rgba(255, 255, 255, 0.9)'
}
```

### Color Usage Guidelines

#### ✅ Do's
- Use primary colors for main actions and navigation
- Apply semantic colors consistently (green for success, red for error)
- Maintain sufficient contrast ratios (4.5:1 for normal text, 3:1 for large text)
- Use surface colors for cards and elevated elements

#### ❌ Don'ts
- Don't use pure black or white unless specifically needed
- Avoid using color alone to convey information
- Don't mix warm and cool grays in the same interface
- Never use red and green as the only way to distinguish information

---

## 📐 Typography

### Type Scale

```typescript
const typography = {
  // Display Styles (for large headings)
  display1: {
    fontSize: 57,
    lineHeight: 64,
    fontWeight: '400',
    letterSpacing: -0.25
  },
  display2: {
    fontSize: 45,
    lineHeight: 52,
    fontWeight: '400'
  },

  // Headline Styles
  headline1: {
    fontSize: 32,
    lineHeight: 40,
    fontWeight: '600',
    letterSpacing: -0.5
  },
  headline2: {
    fontSize: 28,
    lineHeight: 36,
    fontWeight: '600',
    letterSpacing: -0.25
  },
  headline3: {
    fontSize: 24,
    lineHeight: 32,
    fontWeight: '600'
  },

  // Title Styles
  title1: {
    fontSize: 22,
    lineHeight: 28,
    fontWeight: '500'
  },
  title2: {
    fontSize: 16,
    lineHeight: 24,
    fontWeight: '500',
    letterSpacing: 0.15
  },

  // Body Styles
  body1: {
    fontSize: 16,
    lineHeight: 24,
    fontWeight: '400',
    letterSpacing: 0.5
  },
  body2: {
    fontSize: 14,
    lineHeight: 20,
    fontWeight: '400',
    letterSpacing: 0.25
  },

  // Label Styles
  label1: {
    fontSize: 14,
    lineHeight: 20,
    fontWeight: '500',
    letterSpacing: 0.1
  },
  label2: {
    fontSize: 12,
    lineHeight: 16,
    fontWeight: '500',
    letterSpacing: 0.5
  },

  // Caption
  caption: {
    fontSize: 12,
    lineHeight: 16,
    fontWeight: '400',
    letterSpacing: 0.4
  }
}
```

### Font Families

#### Primary Font (Inter)
- **Usage**: Body text, UI elements, navigation
- **Weights**: 400 (Regular), 500 (Medium), 600 (Semibold)
- **Characteristics**: Excellent readability, neutral appearance

#### Secondary Font (SF Pro Display / Roboto)
- **Usage**: Headings, titles, emphasized text
- **Weights**: 400 (Regular), 500 (Medium), 600 (Semibold), 700 (Bold)
- **Platform**: SF Pro on iOS, Roboto on Android

### Typography Usage

```jsx
// Example usage in components
<Text style={[typography.headline1, { color: colors.text }]}>
  Movie Title
</Text>

<Text style={[typography.body1, { color: colors.textSecondary }]}>
  Movie description goes here...
</Text>

<Text style={[typography.caption, { color: colors.textDisabled }]}>
  2023 • Action, Sci-Fi • 2h 28m
</Text>
```

---

## 📏 Spacing & Layout

### Spacing Scale

```typescript
const spacing = {
  xs: 4,    // 0.25rem
  sm: 8,    // 0.5rem
  md: 16,   // 1rem
  lg: 24,   // 1.5rem
  xl: 32,   // 2rem
  xxl: 48,  // 3rem
  xxxl: 64  // 4rem
}
```

### Layout Grid

#### Mobile (< 768px)
- **Margins**: 16px
- **Gutters**: 16px
- **Columns**: 4-6 columns

#### Tablet (768px - 1024px)
- **Margins**: 24px
- **Gutters**: 24px
- **Columns**: 8-12 columns

#### Desktop (> 1024px)
- **Margins**: 32px
- **Gutters**: 32px
- **Columns**: 12 columns
- **Max Width**: 1200px

### Component Spacing

```typescript
const componentSpacing = {
  // Card padding
  cardPadding: spacing.md,        // 16px

  // List item spacing
  listItemPadding: spacing.md,    // 16px
  listItemGap: spacing.sm,        // 8px

  // Section spacing
  sectionGap: spacing.xl,         // 32px

  // Button padding
  buttonPadding: {
    vertical: spacing.md,         // 16px
    horizontal: spacing.lg        // 24px
  },

  // Input padding
  inputPadding: spacing.md,       // 16px

  // Screen margins
  screenMargin: spacing.md        // 16px
}
```

---

## 🖼️ Iconography

### Icon System

#### Icon Sizes
```typescript
const iconSizes = {
  xs: 12,   // Small inline icons
  sm: 16,   // Body text icons
  md: 24,   // Default size
  lg: 32,   // Large interactive elements
  xl: 48,   // Hero icons
  xxl: 64   // Extra large display icons
}
```

#### Icon Families
- **Expo Vector Icons**: Primary icon set
- **Custom Icons**: Brand-specific and unique interface elements
- **Platform Icons**: iOS SF Symbols, Android Material Icons

#### Icon Usage Guidelines

```jsx
// Standard icon usage
import { Ionicons } from '@expo/vector-icons'

// Navigation icons
<Ionicons
  name="home"
  size={iconSizes.md}
  color={colors.text}
/>

// Interactive icons with proper touch targets
<Pressable style={{ padding: spacing.sm }}>
  <Ionicons
    name="heart"
    size={iconSizes.md}
    color={colors.error.main}
  />
</Pressable>
```

### Icon Style Guidelines

#### ✅ Do's
- Use outlined icons for inactive states
- Use filled icons for active/selected states
- Maintain consistent visual weight across icon families
- Ensure icons have sufficient contrast against backgrounds

#### ❌ Don'ts
- Don't mix different icon styles in the same interface
- Avoid overly complex icons at small sizes
- Don't use icons without clear meaning or context

---

## 📱 Components

### Button System

#### Primary Button
```jsx
const PrimaryButton = ({
  title,
  onPress,
  disabled = false,
  loading = false,
  size = 'medium'
}) => {
  const buttonStyles = {
    backgroundColor: disabled
      ? colors.surface
      : colors.primary[500],
    paddingVertical: size === 'large' ? spacing.lg : spacing.md,
    paddingHorizontal: size === 'large' ? spacing.xl : spacing.lg,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
    minHeight: 44 // Accessibility minimum
  }

  return (
    <Pressable
      style={buttonStyles}
      onPress={onPress}
      disabled={disabled || loading}
    >
      <Text style={[typography.label1, { color: colors.background }]}>
        {loading ? 'Loading...' : title}
      </Text>
    </Pressable>
  )
}
```

#### Secondary Button
```jsx
const SecondaryButton = ({ title, onPress, disabled = false }) => (
  <Pressable
    style={{
      borderWidth: 1,
      borderColor: colors.border,
      paddingVertical: spacing.md,
      paddingHorizontal: spacing.lg,
      borderRadius: 12,
      alignItems: 'center',
      justifyContent: 'center',
      minHeight: 44
    }}
    onPress={onPress}
    disabled={disabled}
  >
    <Text style={[typography.label1, { color: colors.text }]}>
      {title}
    </Text>
  </Pressable>
)
```

### Card Components

#### Movie Card
```jsx
const MovieCard = ({ movie, onPress, variant = 'default' }) => {
  const cardWidth = variant === 'compact' ? 120 : 160
  const cardHeight = variant === 'compact' ? 180 : 240

  return (
    <Pressable
      onPress={() => onPress(movie)}
      style={{
        width: cardWidth,
        marginRight: spacing.md
      }}
    >
      <ExpoImage
        source={{ uri: movie.poster }}
        style={{
          width: cardWidth,
          height: cardHeight,
          borderRadius: 12,
          backgroundColor: colors.surface
        }}
        contentFit="cover"
        placeholder="blur"
        transition={200}
      />

      <View style={{ paddingTop: spacing.sm }}>
        <Text
          style={[typography.label1, { color: colors.text }]}
          numberOfLines={2}
        >
          {movie.title}
        </Text>
        <Text
          style={[typography.caption, { color: colors.textSecondary }]}
          numberOfLines={1}
        >
          {movie.year} • {formatRating(movie.rating)}
        </Text>
      </View>
    </Pressable>
  )
}
```

#### Detail Card
```jsx
const DetailCard = ({ children, style }) => (
  <View
    style={[{
      backgroundColor: colors.surface,
      borderRadius: 16,
      padding: spacing.md,
      marginVertical: spacing.sm,
      shadowColor: colors.text,
      shadowOffset: { width: 0, height: 2 },
      shadowOpacity: 0.1,
      shadowRadius: 8,
      elevation: 4
    }, style]}
  >
    {children}
  </View>
)
```

### Input Components

#### Search Bar
```jsx
const SearchBar = ({
  value,
  onChangeText,
  placeholder = "Search movies and series...",
  loading = false
}) => (
  <View style={{
    backgroundColor: colors.surface,
    borderRadius: 12,
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: spacing.md,
    minHeight: 44,
    borderWidth: 1,
    borderColor: colors.border
  }}>
    <Ionicons
      name="search"
      size={iconSizes.md}
      color={colors.textSecondary}
    />

    <TextInput
      style={[
        typography.body1,
        {
          flex: 1,
          marginLeft: spacing.sm,
          color: colors.text,
          paddingVertical: spacing.sm
        }
      ]}
      value={value}
      onChangeText={onChangeText}
      placeholder={placeholder}
      placeholderTextColor={colors.textDisabled}
    />

    {loading && (
      <ActivityIndicator
        size="small"
        color={colors.primary[500]}
      />
    )}
  </View>
)
```

---

## 🌊 Animations

### Animation Principles

#### Timing Functions
```typescript
const animations = {
  // Easing curves
  easeInOut: Easing.bezier(0.4, 0, 0.2, 1),
  easeOut: Easing.bezier(0, 0, 0.2, 1),
  easeIn: Easing.bezier(0.4, 0, 1, 1),

  // Duration standards
  duration: {
    fast: 150,      // Quick UI feedback
    normal: 250,    // Standard transitions
    slow: 400,      // Complex animations
    enter: 300,     // Element entry
    exit: 200       // Element exit
  }
}
```

#### Common Animations

##### Fade In/Out
```jsx
import Animated, {
  useSharedValue,
  useAnimatedStyle,
  withTiming
} from 'react-native-reanimated'

const FadeInView = ({ children, visible }) => {
  const opacity = useSharedValue(visible ? 1 : 0)

  const animatedStyle = useAnimatedStyle(() => ({
    opacity: withTiming(opacity.value, {
      duration: animations.duration.normal
    })
  }))

  useEffect(() => {
    opacity.value = visible ? 1 : 0
  }, [visible])

  return (
    <Animated.View style={animatedStyle}>
      {children}
    </Animated.View>
  )
}
```

##### Slide Transition
```jsx
const SlideInView = ({ children, direction = 'right' }) => {
  const translateX = useSharedValue(direction === 'right' ? 100 : -100)

  const animatedStyle = useAnimatedStyle(() => ({
    transform: [{
      translateX: withTiming(translateX.value, {
        duration: animations.duration.normal,
        easing: animations.easeOut
      })
    }]
  }))

  useEffect(() => {
    translateX.value = 0
  }, [])

  return (
    <Animated.View style={animatedStyle}>
      {children}
    </Animated.View>
  )
}
```

##### Scale Animation
```jsx
const ScaleButton = ({ children, onPress }) => {
  const scale = useSharedValue(1)

  const animatedStyle = useAnimatedStyle(() => ({
    transform: [{ scale: scale.value }]
  }))

  const handlePressIn = () => {
    scale.value = withTiming(0.95, { duration: animations.duration.fast })
  }

  const handlePressOut = () => {
    scale.value = withTiming(1, { duration: animations.duration.fast })
  }

  return (
    <Animated.View style={animatedStyle}>
      <Pressable
        onPressIn={handlePressIn}
        onPressOut={handlePressOut}
        onPress={onPress}
      >
        {children}
      </Pressable>
    </Animated.View>
  )
}
```

### Animation Guidelines

#### ✅ Do's
- Use consistent timing and easing across similar interactions
- Provide visual feedback for all user interactions
- Keep animations smooth and purposeful
- Test animations on slower devices

#### ❌ Don'ts
- Avoid overly long or complex animations that delay user tasks
- Don't animate too many elements simultaneously
- Never sacrifice usability for visual appeal

---

## ♿ Accessibility

### Touch Targets

#### Minimum Sizes
```typescript
const touchTargets = {
  minimum: 44,      // iOS guideline minimum
  comfortable: 48,  // Recommended comfortable size
  large: 56        // Large/primary actions
}
```

#### Implementation
```jsx
const AccessibleButton = ({ onPress, children }) => (
  <Pressable
    onPress={onPress}
    style={{
      minHeight: touchTargets.minimum,
      minWidth: touchTargets.minimum,
      alignItems: 'center',
      justifyContent: 'center',
      padding: spacing.sm
    }}
    accessibilityRole="button"
    accessible={true}
  >
    {children}
  </Pressable>
)
```

### Screen Reader Support

#### Semantic Labels
```jsx
// Good accessibility implementation
<Pressable
  onPress={addToFavorites}
  accessibilityRole="button"
  accessibilityLabel={`Add ${movieTitle} to favorites`}
  accessibilityHint="Double tap to add this movie to your favorites list"
>
  <Ionicons name="heart-outline" size={24} />
</Pressable>

// Navigation landmarks
<View accessibilityRole="navigation">
  <Text accessibilityRole="header">Main Navigation</Text>
  {navigationItems}
</View>
```

#### Focus Management
```jsx
const ScreenHeader = ({ title, onBack }) => (
  <View style={{ flexDirection: 'row', alignItems: 'center' }}>
    <Pressable
      onPress={onBack}
      accessibilityRole="button"
      accessibilityLabel="Go back"
      accessible={true}
    >
      <Ionicons name="arrow-back" size={24} />
    </Pressable>

    <Text
      style={typography.headline2}
      accessibilityRole="header"
      accessible={true}
    >
      {title}
    </Text>
  </View>
)
```

### Color Contrast

#### WCAG AA Compliance
- **Normal text**: 4.5:1 contrast ratio minimum
- **Large text**: 3:1 contrast ratio minimum
- **Interactive elements**: 3:1 against adjacent colors

#### Testing Tools
```javascript
// Example contrast checking
const checkContrast = (foreground, background) => {
  // Implementation would use color-contrast library
  const ratio = getContrastRatio(foreground, background)
  return {
    AA: ratio >= 4.5,
    AAA: ratio >= 7,
    large: ratio >= 3
  }
}
```

---

## 📱 Responsive Design

### Breakpoints

```typescript
const breakpoints = {
  mobile: 0,      // 0px and up
  tablet: 768,    // 768px and up
  desktop: 1024,  // 1024px and up
  wide: 1200     // 1200px and up
}
```

### Responsive Utilities

```jsx
import { useWindowDimensions } from 'react-native'

const useResponsive = () => {
  const { width } = useWindowDimensions()

  return {
    isMobile: width < breakpoints.tablet,
    isTablet: width >= breakpoints.tablet && width < breakpoints.desktop,
    isDesktop: width >= breakpoints.desktop,
    width
  }
}

// Usage in components
const ResponsiveGrid = ({ children }) => {
  const { isMobile, isTablet } = useResponsive()

  const numColumns = isMobile ? 2 : isTablet ? 3 : 4

  return (
    <FlatList
      data={children}
      numColumns={numColumns}
      key={numColumns} // Force re-render on column change
      // ... other props
    />
  )
}
```

### Layout Adaptations

#### Navigation
```jsx
// Mobile: Bottom tab navigation
// Tablet/Desktop: Side navigation or top navigation

const Navigation = () => {
  const { isMobile } = useResponsive()

  return isMobile ? <BottomTabNavigation /> : <SideNavigation />
}
```

#### Content Layout
```jsx
const MovieDetailScreen = ({ movie }) => {
  const { isMobile } = useResponsive()

  if (isMobile) {
    return (
      <ScrollView>
        <MoviePoster movie={movie} />
        <MovieInfo movie={movie} />
        <MovieActions movie={movie} />
      </ScrollView>
    )
  }

  return (
    <View style={{ flexDirection: 'row' }}>
      <View style={{ flex: 1 }}>
        <MoviePoster movie={movie} />
      </View>
      <View style={{ flex: 2 }}>
        <MovieInfo movie={movie} />
        <MovieActions movie={movie} />
      </View>
    </View>
  )
}
```

---

## 🌙 Dark Mode

### Theme Implementation

```jsx
import { useColorScheme } from 'react-native'

const ThemeProvider = ({ children }) => {
  const systemColorScheme = useColorScheme()
  const [themeMode, setThemeMode] = useState('auto') // 'light', 'dark', 'auto'

  const theme = useMemo(() => {
    const effectiveMode = themeMode === 'auto' ? systemColorScheme : themeMode
    return effectiveMode === 'dark' ? darkTheme : lightTheme
  }, [themeMode, systemColorScheme])

  return (
    <ThemeContext.Provider value={{ theme, themeMode, setThemeMode }}>
      {children}
    </ThemeContext.Provider>
  )
}

// Usage in components
const MovieCard = ({ movie }) => {
  const { theme } = useTheme()

  return (
    <View style={{
      backgroundColor: theme.surface,
      borderColor: theme.border
    }}>
      <Text style={{ color: theme.text }}>
        {movie.title}
      </Text>
    </View>
  )
}
```

### Dark Mode Considerations

#### Image Handling
```jsx
// Adjust image opacity in dark mode for better integration
const ThemedImage = ({ source, style }) => {
  const { theme } = useTheme()

  return (
    <ExpoImage
      source={source}
      style={[
        style,
        theme.isDark && { opacity: 0.9 }
      ]}
    />
  )
}
```

#### Status Bar
```jsx
import { StatusBar } from 'expo-status-bar'

const App = () => {
  const { theme } = useTheme()

  return (
    <>
      <StatusBar
        style={theme.isDark ? 'light' : 'dark'}
        backgroundColor={theme.background}
      />
      {/* App content */}
    </>
  )
}
```

### Theme Testing

```jsx
// Theme preview component for testing
const ThemePreview = () => {
  const themes = ['light', 'dark']

  return (
    <View style={{ flexDirection: 'row' }}>
      {themes.map(mode => (
        <View key={mode} style={{ flex: 1 }}>
          <Text>{mode} Theme</Text>
          <ThemeProvider forcedTheme={mode}>
            <MovieCard movie={sampleMovie} />
            <PrimaryButton title="Sample Button" />
          </ThemeProvider>
        </View>
      ))}
    </View>
  )
}
```

---

<div align="center">

**[⬆ Back to Top](#-ui-guidelines--design-system)**

*This design system evolves with the product while maintaining consistency and accessibility.*

</div>
