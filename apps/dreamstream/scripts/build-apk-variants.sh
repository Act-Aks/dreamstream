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

# Initialize counters
UNIVERSAL_FOUND=false
ARM64_FOUND=false
ARM32_FOUND=false
X86_64_FOUND=false
X86_FOUND=false

# Process each APK file found
for apk in $(find . -name "*.apk" -type f); do
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
ls -la DreamStream-*-v${VERSION}.apk