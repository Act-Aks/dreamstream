#!/bin/bash

# Calculate APK sizes and export environment variables
# Usage: VERSION=1.0.0 bun run calc-sizes

set -e

if [ -z "$VERSION" ]; then
    echo "❌ VERSION environment variable required"
    exit 1
fi

echo "📊 Calculating APK sizes..."

# Initialize variables
APK_SIZES=""
TOTAL_SIZE=0
UNIVERSAL_SIZE="N/A"
ARM64_SIZE="N/A"
ARM32_SIZE="N/A"
X86_64_SIZE="N/A"
X86_SIZE="N/A"

# Calculate sizes for each variant
for variant in "universal" "arm64" "arm32" "x86_64" "x86"; do
    apk_file="DreamStream-${variant}-v${VERSION}.apk"
    if [ -f "$apk_file" ]; then
        SIZE=$(du -h "$apk_file" | cut -f1)
        SIZE_BYTES=$(du -b "$apk_file" | cut -f1)
        
        # Format variant name for display
        case $variant in
            "universal") DISPLAY_NAME="🌍 Universal APK" ;;
            "arm64") DISPLAY_NAME="🚀 ARM64 APK" ;;
            "arm32") DISPLAY_NAME="⚡ ARM32 APK" ;;
            "x86_64") DISPLAY_NAME="💻 x86_64 APK" ;;
            "x86") DISPLAY_NAME="🖥️ x86 APK" ;;
        esac
        
        APK_SIZES="${APK_SIZES}- **${DISPLAY_NAME}**: \`${SIZE}\`\n"
        TOTAL_SIZE=$((TOTAL_SIZE + SIZE_BYTES))
        
        case $variant in
            "universal") UNIVERSAL_SIZE="$SIZE" ;;
            "arm64") ARM64_SIZE="$SIZE" ;;
            "arm32") ARM32_SIZE="$SIZE" ;;
            "x86_64") X86_64_SIZE="$SIZE" ;;
            "x86") X86_SIZE="$SIZE" ;;
        esac
        
        echo "✅ ${variant}: ${SIZE}"
    else
        case $variant in
            "universal") DISPLAY_NAME="🌍 Universal APK" ;;
            "arm64") DISPLAY_NAME="🚀 ARM64 APK" ;;
            "arm32") DISPLAY_NAME="⚡ ARM32 APK" ;;
            "x86_64") DISPLAY_NAME="💻 x86_64 APK" ;;
            "x86") DISPLAY_NAME="🖥️ x86 APK" ;;
        esac
        APK_SIZES="${APK_SIZES}- **${DISPLAY_NAME}**: \`Not built\`\n"
        
        case $variant in
            "universal") UNIVERSAL_SIZE="Not built" ;;
            "arm64") ARM64_SIZE="Not built" ;;
            "arm32") ARM32_SIZE="Not built" ;;
            "x86_64") X86_64_SIZE="Not built" ;;
            "x86") X86_SIZE="Not built" ;;
        esac
    fi
done

# Convert total size to human readable
if [ $TOTAL_SIZE -gt 0 ]; then
    TOTAL_SIZE_HR=$(echo $TOTAL_SIZE | awk '{
        if ($1 >= 1073741824) printf "%.1fGB", $1/1073741824
        else if ($1 >= 1048576) printf "%.1fMB", $1/1048576
        else if ($1 >= 1024) printf "%.1fKB", $1/1024
        else printf "%dB", $1
    }')
else
    TOTAL_SIZE_HR="N/A"
fi

# Export environment variables
{
    echo "APK_SIZES<<EOF"
    echo -e "$APK_SIZES"
    echo "EOF"
    echo "TOTAL_SIZE=$TOTAL_SIZE_HR"
    echo "UNIVERSAL_SIZE=$UNIVERSAL_SIZE"
    echo "ARM64_SIZE=$ARM64_SIZE"
    echo "ARM32_SIZE=$ARM32_SIZE"
    echo "X86_64_SIZE=$X86_64_SIZE"
    echo "X86_SIZE=$X86_SIZE"
} >> $GITHUB_ENV

echo "📋 Total Size: $TOTAL_SIZE_HR"