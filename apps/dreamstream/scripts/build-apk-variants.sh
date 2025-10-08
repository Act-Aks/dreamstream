#!/bin/bash

# Build and organize APK variants
# Usage: VERSION=1.0.0 bun run build-variants

set -e

if [ -z "$VERSION" ]; then
    echo "❌ VERSION environment variable required"
    exit 1
fi

echo "🏗️ Building APK variants with expo-build-properties..."
bun run build:android:release

echo "📦 Organizing APK variants..."

# Debug: Show current directory and look for build artifacts
echo "🔍 Current directory: $(pwd)"
echo "🔍 Looking for build artifacts..."

# First, look for tar.gz build artifacts from EAS
BUILD_ARCHIVE=$(find . -name "build-*.tar.gz" -type f | head -1)

if [ -n "$BUILD_ARCHIVE" ]; then
    echo "📦 Found build archive: $BUILD_ARCHIVE"
    echo "📂 Extracting build artifacts..."
    
    # Create extraction directory
    EXTRACT_DIR="extracted_build"
    rm -rf "$EXTRACT_DIR"
    mkdir -p "$EXTRACT_DIR"
    
    # Extract the archive
    tar -xzf "$BUILD_ARCHIVE" -C "$EXTRACT_DIR"
    
    # Look for APKs in the extracted content
    APK_SEARCH_PATH="$EXTRACT_DIR"
    echo "🔍 Searching for APKs in extracted archive: $APK_SEARCH_PATH"
else
    # Fallback: look in standard Android build directory
    APK_DIR="android/app/build/outputs/apk/release"
    if [ -d "$APK_DIR" ]; then
        APK_SEARCH_PATH="$APK_DIR"
        echo "🔍 Searching for APKs in build directory: $APK_SEARCH_PATH"
    else
        # Final fallback: current directory
        APK_SEARCH_PATH="."
        echo "🔍 Searching for APKs in current directory: $APK_SEARCH_PATH"
    fi
fi

# Debug: Show what APK files we found
echo "🔍 APK files found:"
find "$APK_SEARCH_PATH" -name "*.apk" -type f | head -10 | while read apk; do
    echo "  Found: $apk ($(du -h "$apk" | cut -f1))"
done

# Initialize counters
UNIVERSAL_FOUND=false
ARM64_FOUND=false
ARM32_FOUND=false
X86_64_FOUND=false
X86_FOUND=false

for apk in $(find "$APK_SEARCH_PATH" -name "*.apk" -type f); do
    basename_apk=$(basename "$apk")
    
    if [[ "$basename_apk" == *"universal"* ]]; then
        cp "$apk" "DreamStream-universal-v${VERSION}.apk"
        echo "✅ Universal APK organized"
        UNIVERSAL_FOUND=true
    elif [[ "$basename_apk" == *"arm64-v8a"* ]]; then
        cp "$apk" "DreamStream-arm64-v${VERSION}.apk"
        echo "✅ ARM64 APK organized"
        ARM64_FOUND=true
    elif [[ "$basename_apk" == *"armeabi-v7a"* ]]; then
        cp "$apk" "DreamStream-arm32-v${VERSION}.apk"
        echo "✅ ARM32 APK organized"
        ARM32_FOUND=true
    elif [[ "$basename_apk" == *"x86_64"* ]]; then
        cp "$apk" "DreamStream-x86_64-v${VERSION}.apk"
        echo "✅ x86_64 APK organized"
        X86_64_FOUND=true
    elif [[ "$basename_apk" == *"x86"* ]] && [[ "$basename_apk" != *"x86_64"* ]]; then
        cp "$apk" "DreamStream-x86-v${VERSION}.apk"
        echo "✅ x86 APK organized"
        X86_FOUND=true
    elif [ "$UNIVERSAL_FOUND" = false ]; then
        cp "$apk" "DreamStream-universal-v${VERSION}.apk"
        echo "✅ Unknown APK treated as Universal"
        UNIVERSAL_FOUND=true
    fi
done

# Verify we have at least one APK
if [ "$UNIVERSAL_FOUND" = false ] && [ "$ARM64_FOUND" = false ] && [ "$ARM32_FOUND" = false ] && [ "$X86_64_FOUND" = false ] && [ "$X86_FOUND" = false ]; then
    echo "❌ No APK variants were successfully processed"
    exit 1
fi

echo "📋 Variants: Universal=$UNIVERSAL_FOUND, ARM64=$ARM64_FOUND, ARM32=$ARM32_FOUND, x86_64=$X86_64_FOUND, x86=$X86_FOUND"

# Clean up extracted directory if it exists
if [ -d "extracted_build" ]; then
    echo "🧹 Cleaning up extracted build directory..."
    rm -rf "extracted_build"
fi

echo "📱 Final APK variants:"
ls -la DreamStream-*-v${VERSION}.apk