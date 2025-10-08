#!/bin/bash

# Script to build APK variants and generate QR codes
# Usage: ./scripts/build-and-package.sh <version>

set -e

VERSION="$1"

if [ -z "$VERSION" ]; then
    echo "❌ Version not provided"
    exit 1
fi

cd apps/dreamstream

echo "🔨 Building APK variants for version $VERSION..."

# Prebuild and Build APK Variants
echo "🏗️ Running prebuild..."
bun expo prebuild --clean

echo "📱 Building APK variants..."
bun run build-variants

echo "🔗 Generating QR codes..."
bun run generate-qr

echo "✅ Build and packaging completed for version $VERSION"