# 🌊 DreamStream

<div align="center">

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Node](https://img.shields.io/badge/node-%3E%3D24-green.svg)
![Bun](https://img.shields.io/badge/bun-1.3.5-orange.svg)
![TypeScript](https://img.shields.io/badge/typescript-5.9.3-blue.svg)
![React](https://img.shields.io/badge/react-19.2.3-61dafb.svg)
![Expo](https://img.shields.io/badge/expo-54.0.30-000020.svg)
![License](https://img.shields.io/badge/license-Proprietary-red.svg)

*A modern, type-safe mobile application built with cutting-edge technologies*

[Getting Started](#-getting-started) • [Features](#-features) • [Tech Stack](#-tech-stack) • [Project Structure](#-project-structure) • [Scripts](#-available-scripts)

</div>

---

## ✨ Features

- 🎯 **Type Safety First** - Full TypeScript support with strict type checking
- 📱 **Cross-Platform Mobile** - React Native with Expo for iOS and Android
- 🎨 **Modern UI/UX** - TailwindCSS with HeroUI Native components
- ⚡ **Lightning Fast** - Bun package manager and runtime
- 🔧 **Developer Experience** - Hot reload, debugging tools, and more
- 📦 **Monorepo Architecture** - Organized workspace with shared packages
- 🛡️ **Code Quality** - Ultracite (Biome) for linting and formatting
- 🎬 **Rich Media** - Video playback capabilities with expo-video
- 💾 **Local Storage** - Fast key-value storage with MMKV
- 🎭 **Smooth Animations** - Reanimated for 60fps animations

## 🚀 Getting Started

### Prerequisites

- **Node.js** >= 24.0.0
- **Bun** >= 1.3.5
- **iOS Simulator** (for iOS development)
- **Android Studio** (for Android development)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd dreamstream
   ```

2. **Install dependencies**
   ```bash
   bun install
   ```

3. **Start the development server**
   ```bash
   bun run dev:native
   ```

4. **Run on device/simulator**
   ```bash
   # iOS
   bun run ios
   
   # Android
   bun run android
   
   # Or use Expo Go app with QR code
   ```

## 🛠️ Tech Stack

### Core Technologies
- **[React](https://react.dev/)** `19.2.3` - UI library with latest features
- **[React Native](https://reactnative.dev/)** `0.83.1` - Cross-platform mobile framework
- **[Expo](https://expo.dev/)** `54.0.30` - Development platform and tools
- **[TypeScript](https://www.typescriptlang.org/)** `5.9.3` - Type-safe JavaScript

### UI & Styling
- **[TailwindCSS](https://tailwindcss.com/)** `4.1.18` - Utility-first CSS framework
- **[HeroUI Native](https://heroui.com/)** `1.0.0-beta.9` - Beautiful React Native components
- **[Tailwind Variants](https://www.tailwind-variants.org/)** `3.2.2` - Component variants
- **[React Native SVG](https://github.com/software-mansion/react-native-svg)** `15.15.1` - SVG support

### Navigation & Routing
- **[Expo Router](https://docs.expo.dev/router/introduction/)** `6.0.21` - File-based routing
- **[React Navigation](https://reactnavigation.org/)** `7.1.26` - Navigation library

### Animations & Gestures
- **[React Native Reanimated](https://docs.swmansion.com/react-native-reanimated/)** `4.2.1` - High-performance animations
- **[React Native Gesture Handler](https://docs.swmansion.com/react-native-gesture-handler/)** `2.30.0` - Touch gestures
- **[React Native Worklets](https://github.com/margelo/react-native-worklets-core)** `0.7.1` - JavaScript worklets

### State Management & Storage
- **[Zustand](https://zustand-demo.pmnd.rs/)** `5.0.9` - Lightweight state management
- **[React Native MMKV](https://github.com/mrousavy/react-native-mmkv)** `4.1.0` - Fast key-value storage
- **[Zod](https://zod.dev/)** `4.2.1` - Schema validation

### Development Tools
- **[Bun](https://bun.sh/)** `1.3.5` - Fast package manager and runtime
- **[Ultracite](https://github.com/biomejs/biome)** `6.5.0` - Code formatting and linting
- **[Husky](https://typicode.github.io/husky/)** `9.1.7` - Git hooks
- **[Lint Staged](https://github.com/okonet/lint-staged)** `16.2.7` - Pre-commit linting

### UI Components & Libraries
- **[Bottom Sheet](https://github.com/gorhom/react-native-bottom-sheet)** `5.2.8` - Bottom sheet component
- **[Keyboard Controller](https://github.com/kirillzyusko/react-native-keyboard-controller)** `1.20.2` - Keyboard handling
- **[Safe Area Context](https://github.com/th3rdwave/react-native-safe-area-context)** `5.6.2` - Safe area handling

## 📁 Project Structure

```
dreamstream/
├── 📱 apps/
│   └── native/                 # React Native mobile app
│       ├── app/               # Expo Router pages
│       ├── components/        # Reusable UI components
│       ├── hooks/            # Custom React hooks
│       ├── lib/              # Utilities and configurations
│       ├── assets/           # Images, fonts, etc.
│       └── package.json      # App-specific dependencies
├── 📦 packages/
│   └── config/               # Shared configurations
├── 🔧 Configuration Files
│   ├── biome.jsonc          # Biome (linting/formatting) config
│   ├── bunfig.toml          # Bun configuration
│   ├── .lintstagedrc.json   # Lint-staged configuration
│   └── .husky/              # Git hooks
└── 📄 Root Files
    ├── package.json         # Workspace configuration
    ├── bun.lock            # Dependency lock file
    └── README.md           # This file
```

## 🎯 Available Scripts

### Development
```bash
bun run dev              # Start all applications in development mode
bun run dev:native       # Start React Native/Expo development server
```

### Building
```bash
bun run build           # Build all applications for production
bun run prebuild        # Generate native code (iOS/Android)
```

### Platform Specific
```bash
bun run ios             # Run on iOS simulator
bun run android         # Run on Android emulator
bun run start           # Start Expo development server
```

### Code Quality
```bash
bun run check-types     # Type check all applications
bun x ultracite check   # Check code quality issues
bun x ultracite fix     # Auto-fix code quality issues
```

### Maintenance
```bash
bun run up:latest       # Update dependencies to latest versions
bun run fix:version     # Fix Expo SDK version conflicts
```

## 🔧 Development

### Code Quality Standards

This project uses **Ultracite** for maintaining high code quality standards:

- **Automatic formatting** with Biome (Rust-based, extremely fast)
- **Strict linting rules** for TypeScript, React, and accessibility
- **Pre-commit hooks** to ensure code quality
- **Type safety** with strict TypeScript configuration

### Key Development Principles

- 🎯 **Type Safety First** - Explicit types for better developer experience
- ♿ **Accessibility** - ARIA attributes and semantic HTML
- ⚡ **Performance** - Optimized components and efficient state management
- 🧪 **Testing Ready** - Structure supports easy testing implementation
- 📱 **Mobile First** - Optimized for mobile user experience

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## ⚠️ Important Disclaimers

### Content & Legal Notice

**This application does not own any content.** DreamStream serves as a client interface that aggregates publicly available content from external third-party websites. Key points:

- 🔗 **Content Sourcing**: All media is scraped/linked from external sources
- 🚫 **No Content Ownership**: We do not host, store, or distribute any media content
- 📺 **Ad-Free Experience**: Provides ad-free streaming by bypassing source advertisements
- ⚖️ **User Responsibility**: Users assume full legal responsibility for their usage
- 🌍 **Geographic Restrictions**: May not be legal in all jurisdictions

### Risk Acknowledgment

By using this application, you acknowledge that:
- You use this software entirely at your own risk
- You are responsible for ensuring compliance with local laws
- You understand potential copyright implications
- You will not hold developers liable for any legal consequences

For detailed terms and conditions, please see the [LICENSE](LICENSE) file.

## 📄 License

This project is proprietary software. See the [LICENSE](LICENSE) file for detailed terms, conditions, and important legal disclaimers regarding content usage and user responsibilities.

---

<div align="center">

**Built with ❤️ using modern technologies**

*DreamStream - Where innovation meets performance*

</div>
