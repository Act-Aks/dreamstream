#!/bin/bash

# Generate QR codes for APK variants
# Usage: VERSION=1.0.0 REPOSITORY=owner/repo bun run generate-qr

set -e

if [ -z "$VERSION" ] || [ -z "$REPOSITORY" ]; then
    echo "❌ VERSION and REPOSITORY environment variables required"
    exit 1
fi

echo "🔗 Generating QR codes for APK variants..."

# Install qrencode if not available
if ! command -v qrencode &> /dev/null; then
    sudo apt-get update && sudo apt-get install -y qrencode
fi

# Generate QR codes for each APK variant that exists
for variant in "universal" "arm64" "arm32" "x86_64" "x86"; do
    apk_file="DreamStream-${variant}-v${VERSION}.apk"
    if [ -f "$apk_file" ]; then
        APK_URL="https://github.com/${REPOSITORY}/releases/download/v${VERSION}/DreamStream-${variant}-v${VERSION}.apk"
        qrencode -s 8 -m 2 -l M -o "DreamStream-${variant}-qr.png" "$APK_URL"
        echo "✅ QR code generated for ${variant}"
    fi
done

# Generate main release page QR code
RELEASE_URL="https://github.com/${REPOSITORY}/releases/tag/v${VERSION}"
qrencode -s 10 -m 4 -l M -o "DreamStream-release-qr.png" "$RELEASE_URL"
echo "✅ Main release QR code generated"

# Convert main QR code to base64 and export
if [ -f "DreamStream-release-qr.png" ]; then
    QR_BASE64=$(base64 -w 0 DreamStream-release-qr.png)
    echo "QR_BASE64=$QR_BASE64" >> $GITHUB_ENV
    echo "✅ QR code base64 exported to environment"
else
    echo "❌ Release QR code not found"
    exit 1
fi