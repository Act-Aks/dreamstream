#!/bin/bash

# Script to update README version references with latest git tag
# This script is mostly for reference since we now use dynamic badges

set -e

# Get the latest git tag
LATEST_TAG=$(git tag --sort=-version:refname | head -1)

if [ -z "$LATEST_TAG" ]; then
    echo "❌ No git tags found"
    exit 1
fi

echo "🏷️  Latest tag: $LATEST_TAG"

# Extract version without 'v' prefix
VERSION=${LATEST_TAG#v}

echo "📝 Version: $VERSION"

# Note: We now use dynamic badges, so manual updates are not needed
echo "✅ README uses dynamic badges that automatically show latest version"
echo "🔗 Version badge: https://img.shields.io/github/v/release/Act-Aks/dreamstream"

# If you need to update any hardcoded versions, uncomment and modify:
# sed -i.bak "s/version-[0-9]\+\.[0-9]\+\.[0-9]\+/version-$VERSION/g" README.md
# echo "✅ Updated README.md with version $VERSION"

echo "🎉 Done!"