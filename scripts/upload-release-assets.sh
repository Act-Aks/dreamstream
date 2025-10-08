#!/bin/bash

# Script to upload APK variants and QR codes to GitHub release
# Usage: ./scripts/upload-release-assets.sh <version>

set -e

VERSION="$1"

if [ -z "$VERSION" ]; then
    echo "❌ Version not provided"
    exit 1
fi

cd apps/dreamstream

echo "📦 Uploading release assets for version $VERSION..."

# Collect all APK variants and QR codes
UPLOAD_FILES=""

# Add APK variants
for variant in "universal" "arm64" "arm32" "x86_64" "x86"; do
    apk_file="DreamStream-${variant}-v${VERSION}.apk"
    if [ -f "$apk_file" ]; then
        UPLOAD_FILES="$UPLOAD_FILES $apk_file"
        echo "📱 Found APK variant: $apk_file"
    fi
done

# Add QR codes
for qr_file in DreamStream-*-qr.png; do
    if [ -f "$qr_file" ]; then
        UPLOAD_FILES="$UPLOAD_FILES $qr_file"
        echo "🔗 Found QR code: $qr_file"
    fi
done

if [ -n "$UPLOAD_FILES" ]; then
    gh release upload "v${VERSION}" $UPLOAD_FILES --clobber
    echo "✅ Successfully uploaded all variants and QR codes"
    echo "📦 Files uploaded: $UPLOAD_FILES"
else
    echo "❌ No APK variants or QR codes found to upload"
    exit 1
fi