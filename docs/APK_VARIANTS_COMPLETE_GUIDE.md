# 📱 DreamStream APK Variants - Complete Guide

## 🎯 Overview

DreamStream generates **5 optimized APK variants** automatically, reducing download sizes by 40-60% for most users while maintaining compatibility across all Android devices and emulators.

## 📦 APK Variants Generated

When running `bun run build:android:release`, the following 5 variants are created:

| Variant | Architecture | File Pattern | Target Devices | Size Reduction |
|---------|-------------|--------------|----------------|----------------|
| **🌍 Universal** | All | `app-universal-release.apk` | Any Android device | 0% (baseline) |
| **🚀 ARM64** | arm64-v8a | `app-arm64-v8a-release.apk` | Modern phones (2017+) | ~40-60% smaller |
| **⚡ ARM32** | armeabi-v7a | `app-armeabi-v7a-release.apk` | Older phones (pre-2017) | ~40-60% smaller |
| **💻 x86_64** | x86_64 | `app-x86_64-release.apk` | Emulators, Chromebooks | ~40-60% smaller |
| **🖥️ x86** | x86 | `app-x86-release.apk` | Legacy emulators | ~40-60% smaller |

## 🚀 Quick Commands

```bash
cd apps/dreamstream

# Build all 5 variants
bun run build:android:release

# Test configuration
bun run test-apk-build

# Check device architecture
bun run check-device-arch

# Local development commands
VERSION=1.0.0 bun run build-variants
VERSION=1.0.0 REPOSITORY=owner/repo bun run generate-qr
VERSION=1.0.0 bun run calc-sizes
```

## ⚙️ Configuration

### 1. App Configuration (`apps/dreamstream/app.json`)

```json
{
  "expo": {
    "plugins": [
      ["expo-build-properties", {
        "android": {
          "reactNativeArchitectures": ["arm64-v8a", "armeabi-v7a", "x86_64", "x86"]
        }
      }]
    ]
  }
}
```

### 2. Dependencies (`apps/dreamstream/package.json`)

```json
{
  "dependencies": {
    "expo-build-properties": "~1.0.9"
  },
  "scripts": {
    "build:android:release": "bun eas build --platform android --profile production --local",
    "build-variants": "bash scripts/build-apk-variants.sh",
    "generate-qr": "bash scripts/generate-qr-codes.sh",
    "calc-sizes": "bash scripts/calculate-apk-sizes.sh",
    "check-device-arch": "node scripts/check-device-arch.js",
    "test-apk-build": "bash scripts/test-apk-build.sh"
  }
}
```

### 3. EAS Configuration (`apps/dreamstream/eas.json`)

```json
{
  "build": {
    "production": {
      "autoIncrement": true,
      "android": {
        "buildType": "apk"
      }
    }
  }
}
```

## 🔄 Build Process

### Automated Scripts

#### 1. Build Variants (`scripts/build-apk-variants.sh`)
- Executes EAS build command
- Detects all 5 generated APK files
- Organizes files with consistent naming:
  - `DreamStream-universal-v{VERSION}.apk`
  - `DreamStream-arm64-v{VERSION}.apk`
  - `DreamStream-arm32-v{VERSION}.apk`
  - `DreamStream-x86_64-v{VERSION}.apk`
  - `DreamStream-x86-v{VERSION}.apk`
- Validates at least one APK was created

#### 2. Generate QR Codes (`scripts/generate-qr-codes.sh`)
- Creates QR codes for each of the 5 APK variants
- Generates main release page QR code
- Exports base64 for release notes embedding

#### 3. Calculate Sizes (`scripts/calculate-apk-sizes.sh`)
- Measures individual APK file sizes for all 5 variants
- Calculates total size across variants
- Exports environment variables for templates

## 🚀 CI/CD Integration

### GitHub Actions Workflow

The release workflow automatically:

1. **Builds All Variants**: Single EAS command generates all 5 APKs
2. **Organizes Files**: Renames APKs with version numbers
3. **Generates QR Codes**: Creates download QR codes for each variant
4. **Calculates Metadata**: Sizes, contributors, changelog
5. **Uploads Assets**: All 5 APKs and QR codes to GitHub release
6. **Updates Release Notes**: Professional template with variant table

### Expected Output Files

After successful build:
```
DreamStream-universal-v1.3.1.apk   # Universal variant (~65MB)
DreamStream-arm64-v1.3.1.apk       # ARM64 variant (~25MB)
DreamStream-arm32-v1.3.1.apk       # ARM32 variant (~27MB)
DreamStream-x86_64-v1.3.1.apk      # x86_64 variant (~25MB)
DreamStream-x86-v1.3.1.apk         # x86 variant (~27MB)

DreamStream-universal-qr.png       # QR codes for each variant
DreamStream-arm64-qr.png
DreamStream-arm32-qr.png
DreamStream-x86_64-qr.png
DreamStream-x86-qr.png
DreamStream-release-qr.png         # Main release page QR
```

## 👥 User Experience

### Release Page Display

Users see a professional table with all 5 variants:

| Architecture | Download | Size | Best For | QR Code |
|-------------|----------|------|----------|---------|
| **🌍 Universal** | DreamStream-universal-v1.3.1.apk | 65MB | Any device (recommended if unsure) | 📱 |
| **🚀 ARM64** | DreamStream-arm64-v1.3.1.apk | 25MB | Modern Android (2017+) | 📱 |
| **⚡ ARM32** | DreamStream-arm32-v1.3.1.apk | 27MB | Older Android (pre-2017) | 📱 |
| **💻 x86_64** | DreamStream-x86_64-v1.3.1.apk | 25MB | Emulators, Chromebooks | 📱 |
| **🖥️ x86** | DreamStream-x86-v1.3.1.apk | 27MB | Legacy emulators | 📱 |

### Architecture Guide for Users

**💡 Which APK should I download?**

- **🌍 Universal APK**: Choose this if you're unsure - works on any Android device
- **🚀 ARM64 APK**: Best for modern Android phones (2017+) - smaller download, better performance
- **⚡ ARM32 APK**: For older Android phones (pre-2017) or specific compatibility needs
- **💻 x86_64 APK**: For Intel-based devices, Android emulators, and some Chromebooks
- **🖥️ x86 APK**: For legacy Intel-based devices and older emulators

### Device Compatibility Checker

```bash
# Help users choose the right variant
bun run check-device-arch
```

Output example:
```
🔍 Device Architecture Checker

Platform: android
Architecture: arm64

📱 Recommended APK variant:
✅ ARM64 - DreamStream-arm64-v{VERSION}.apk
   Most modern Android devices (2017+)

💡 Tips:
- ARM64 APK is ~40-60% smaller than Universal (recommended)
- If unsure, try ARM64 first (most modern devices)
- ARM32 for older devices (pre-2017)
- x86_64 for emulators and Chromebooks
- Universal APK works on all devices but is larger
```

## 🧪 Testing & Debugging

### Local Testing

```bash
cd apps/dreamstream

# Test expo-build-properties configuration
bun run test-apk-build

# Build variants locally
VERSION=1.0.0 bun run build-variants

# Generate QR codes locally
VERSION=1.0.0 REPOSITORY=owner/repo bun run generate-qr

# Calculate sizes locally
VERSION=1.0.0 bun run calc-sizes
```

### Common Issues

#### APK Not Found
```bash
# Check if expo-build-properties is configured
grep -A 5 "expo-build-properties" apps/dreamstream/app.json

# Verify prebuild generated correct configuration
bun expo prebuild --clean
grep -A 10 "splits" apps/dreamstream/android/app/build.gradle
```

#### Size Calculation Errors
```bash
# Check if APK files exist with expected naming
ls -la apps/dreamstream/DreamStream-*-v*.apk

# Manually calculate sizes
du -h apps/dreamstream/DreamStream-*-v*.apk
```

## 📊 Performance Metrics

### Expected Results

**File Sizes (Estimated):**
- Universal APK: ~50-80MB (baseline)
- ARM64 APK: ~20-35MB (60-70% reduction)
- ARM32 APK: ~20-35MB (60-70% reduction)
- x86_64 APK: ~20-35MB (60-70% reduction)
- x86 APK: ~20-35MB (60-70% reduction)

**User Benefits:**
- Faster downloads (smaller files)
- Reduced bandwidth usage
- Faster installation times
- Better device performance
- Less storage usage

**Developer Benefits:**
- Single command builds all 5 variants
- Automated CI/CD process
- Professional release pages
- Easy maintenance and updates

## 🔧 Script Locations

All build scripts are located in `apps/dreamstream/scripts/`:

- `build-apk-variants.sh` - Build and organize all 5 APKs
- `generate-qr-codes.sh` - Generate QR codes for all variants
- `calculate-apk-sizes.sh` - Calculate sizes and export env vars
- `test-apk-build.sh` - Test configuration
- `check-device-arch.js` - Help users choose the right variant

## 🎉 Release Workflow Summary

1. **Push to main** → Semantic release triggers
2. **EAS build** → Generates all 5 APK variants automatically
3. **CI detects** → Finds and organizes all APKs
4. **Sizes calculated** → Individual and total sizes for all variants
5. **QR codes generated** → For each of the 5 variants + release page
6. **Files uploaded** → All 5 APKs and QR codes to GitHub
7. **Release updated** → Professional template with complete variant table
8. **Users download** → Smaller, optimized APKs for their specific devices

## ✅ Success Criteria

The implementation is successful when:

✅ **Single EAS command** generates all 5 APK variants  
✅ **40-60% size reduction** for architecture-specific variants  
✅ **Automated CI/CD** builds and uploads all variants  
✅ **Professional release pages** with complete variant table  
✅ **QR codes** for easy mobile downloads of all variants  
✅ **Robust error handling** prevents build failures  
✅ **Easy maintenance** through bun scripts  
✅ **Universal compatibility** with fallback options  

## 🚨 Troubleshooting

**No APKs generated?**
```bash
bun run test-apk-build  # Check configuration
```

**Wrong number of variants?**
```bash
ls -la build/outputs/apk/release/  # Should show 5 APK files
```

**CI/CD failing?**
- Check GitHub Actions logs
- Verify EXPO_TOKEN secret is set
- Ensure expo-build-properties is in dependencies
- Check that all 5 variants are being detected and uploaded

**Missing QR codes?**
```bash
ls -la DreamStream-*-qr.png  # Should show 6 QR files (5 variants + release page)
```

---

*This complete implementation provides a production-ready APK variants system with all 5 architecture variants, significantly improving user experience while maintaining developer productivity and universal compatibility.*