<div align="center">

# 🌊 DreamStream

![Version](https://img.shields.io/github/v/release/your-username/dreamstream?style=for-the-badge&color=blue)
![License](https://img.shields.io/badge/license-Proprietary-red?style=for-the-badge)
![Node](https://img.shields.io/badge/node-%E2%89%A524-green?style=for-the-badge)
![TypeScript](https://img.shields.io/badge/typescript-5.9.3-blue?style=for-the-badge)

*A modern, type-safe mobile streaming application built with cutting-edge technologies*

**React Native • Expo • TypeScript • TailwindCSS • Bun**

[Getting Started](#-getting-started) • [Tech Stack](#-tech-stack) • [Development](#-development) • [License](#-license)

</div>

---

## ✨ Features

- 🎯 **Type Safety First** - Full TypeScript support with strict type checking
- 📱 **Cross-Platform Mobile** - React Native with Expo for iOS and Android
- 🎨 **Modern UI/UX** - TailwindCSS with HeroUI Native components
- ⚡ **Lightning Fast** - Bun package manager and runtime optimization
- 🎬 **Rich Media** - Video playback capabilities with expo-video
- 💾 **Efficient Storage** - Fast key-value storage with MMKV
- 🎭 **Smooth Animations** - 60fps animations with Reanimated
- 🛡️ **Code Quality** - Ultracite (Biome) for automated linting and formatting

## � Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- **Node.js** >= 24.0.0 ([Download](https://nodejs.org/))
- **Bun** >= 1.3.5 ([Install Guide](https://bun.sh/docs/installation))
- **iOS Simulator** (for iOS development) - Xcode required on macOS
- **Android Studio** (for Android development) - [Setup Guide](https://docs.expo.dev/workflow/android-studio-emulator/)

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
   This will install all workspace dependencies using Bun's fast package manager.

3. **Start the development server**
   ```bash
   bun run dev:native
   ```
   This starts the Expo development server with hot reload enabled.

4. **Run on your preferred platform**
   ```bash
   # iOS Simulator (macOS only)
   bun run ios
   
   # Android Emulator
   bun run android
   
   # Or scan QR code with Expo Go app on your device
   ```

### Available Scripts

```bash
# Development
bun run dev              # Start all applications
bun run dev:native       # Start React Native app only

# Code Quality
bun x ultracite check    # Check for linting issues
bun x ultracite fix      # Auto-fix formatting and linting
bun run check-types      # TypeScript type checking

# Building
bun run build           # Build for production
```

## 🛠️ Tech Stack

### Core Technologies
- **[React Native](https://reactnative.dev/)** `0.83.1` - Cross-platform mobile framework
- **[Expo](https://expo.dev/)** `54.0.30` - Development platform and build tools
- **[TypeScript](https://www.typescriptlang.org/)** `5.9.3` - Type-safe JavaScript superset
- **[Bun](https://bun.sh/)** `1.3.5` - Fast JavaScript runtime and package manager

### UI & Styling
- **[TailwindCSS](https://tailwindcss.com/)** `4.1.18` - Utility-first CSS framework
- **[HeroUI Native](https://heroui.com/)** - Beautiful React Native component library
- **[React Native SVG](https://github.com/software-mansion/react-native-svg)** - SVG support for React Native

### Navigation & Animations
- **[Expo Router](https://docs.expo.dev/router/)** `6.0.21` - File-based routing system
- **[React Native Reanimated](https://docs.swmansion.com/react-native-reanimated/)** `4.2.1` - High-performance animations
- **[React Native Gesture Handler](https://docs.swmansion.com/react-native-gesture-handler/)** `2.30.0` - Touch gesture handling

### State Management & Storage
- **[Zustand](https://zustand-demo.pmnd.rs/)** `5.0.9` - Lightweight state management
- **[React Native MMKV](https://github.com/mrousavy/react-native-mmkv)** `4.1.0` - Fast key-value storage
- **[Zod](https://zod.dev/)** `4.2.1` - TypeScript-first schema validation

### Development Tools
- **[Ultracite](https://github.com/biomejs/biome)** `6.5.0` - Fast Rust-based linting and formatting
- **[Husky](https://typicode.github.io/husky/)** `9.1.7` - Git hooks for code quality
- **[Lint Staged](https://github.com/okonet/lint-staged)** `16.2.7` - Pre-commit linting

## 🔧 Development

### Project Structure

```
dreamstream/
├── 📱 apps/
│   └── native/          # React Native mobile app
│       ├── app/         # Expo Router pages
│       ├── components/  # Reusable UI components
│       ├── hooks/       # Custom React hooks
│       └── lib/         # Utilities and configurations
├── 📦 packages/
│   └── config/          # Shared configurations
└── 🔧 Configuration Files
    ├── biome.jsonc      # Code quality configuration
    ├── .husky/          # Git hooks
    └── package.json     # Workspace configuration
```

### Code Quality Standards

This project uses **Ultracite** (powered by Biome) for maintaining high code quality:

- **Automatic formatting** - Rust-based formatter for consistent code style
- **Strict linting** - TypeScript, React, and accessibility rules
- **Pre-commit hooks** - Automatic code quality checks before commits
- **Type safety** - Strict TypeScript configuration with explicit types

### Development Workflow

1. **Make changes** to your code
2. **Run quality checks**: `bun x ultracite check`
3. **Auto-fix issues**: `bun x ultracite fix`
4. **Type check**: `bun run check-types`
5. **Test on device**: `bun run ios` or `bun run android`
6. **Commit changes** - Pre-commit hooks will run automatically

## ⚠️ Important Legal Notice

### Content Disclaimer

**This application does not own, host, or distribute any media content.** DreamStream serves as a client interface that aggregates publicly available content from external third-party websites.

**Key Points:**
- 🔗 **Content Sourcing**: All media is linked from external sources
- 🚫 **No Content Ownership**: We do not store or distribute any copyrighted material
- 📺 **Ad-Free Experience**: Provides streamlined access by bypassing advertisements
- ⚖️ **User Responsibility**: Users assume full legal responsibility for their usage
- 🌍 **Geographic Restrictions**: May not be legal in all jurisdictions

### Risk Acknowledgment

By using this application, you acknowledge and agree that:
- You use this software entirely at your own risk
- You are responsible for ensuring compliance with local copyright laws
- You understand the potential legal implications of streaming copyrighted content
- You will not hold the developers liable for any legal consequences

## 📄 License

This project is **proprietary software**. All rights reserved.

**Important:** Please read the [LICENSE](LICENSE) file carefully before using this software. The license contains detailed terms, conditions, and important legal disclaimers regarding:
- Software usage rights and restrictions
- Content aggregation and user responsibilities  
- Liability limitations and risk acknowledgments
- Geographic usage restrictions and compliance requirements

For questions about licensing or legal compliance, please review the complete license terms in the [LICENSE](LICENSE) file.

---

<div align="center">

**Built with ❤️ using modern technologies**

*DreamStream - Where innovation meets performance*

</div>
