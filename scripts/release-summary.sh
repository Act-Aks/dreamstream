#!/bin/bash

# Script to display release summary
# Usage: ./scripts/release-summary.sh <version> <repository>

set -e

VERSION="$1"
REPOSITORY="$2"

if [ -z "$VERSION" ] || [ -z "$REPOSITORY" ]; then
    echo "❌ Version and repository must be provided"
    exit 1
fi

echo "🎉 Release v$VERSION completed!"
echo ""
echo "📱 APK Variants Built:"
echo -e "$APK_SIZES"
echo ""
echo "📦 Total Size: $TOTAL_SIZE"
echo "🔄 Commits: $COMMIT_COUNT"
echo "👥 Contributors: $(echo "$CONTRIBUTORS" | wc -l)"
echo ""
echo "🔗 Release Page: https://github.com/$REPOSITORY/releases/tag/v$VERSION"
echo ""
echo "📥 Direct Downloads:"
echo "  • Universal: https://github.com/$REPOSITORY/releases/download/v$VERSION/DreamStream-universal-v$VERSION.apk"
echo "  • ARM64: https://github.com/$REPOSITORY/releases/download/v$VERSION/DreamStream-arm64-v$VERSION.apk"
echo "  • ARM32: https://github.com/$REPOSITORY/releases/download/v$VERSION/DreamStream-arm32-v$VERSION.apk"
echo "  • x86_64: https://github.com/$REPOSITORY/releases/download/v$VERSION/DreamStream-x86_64-v$VERSION.apk"
echo "  • x86: https://github.com/$REPOSITORY/releases/download/v$VERSION/DreamStream-x86-v$VERSION.apk"