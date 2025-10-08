#!/bin/bash

# Main release build script that orchestrates the entire build process
# Usage: ./scripts/release-build.sh <version> <repository>

set -e

VERSION="$1"
REPOSITORY="$2"

if [ -z "$VERSION" ] || [ -z "$REPOSITORY" ]; then
    echo "❌ Version and repository must be provided"
    echo "Usage: $0 <version> <repository>"
    exit 1
fi

echo "🚀 Starting release build process for version $VERSION"

# Step 1: Verify version consistency
echo "📋 Step 1: Verifying version consistency..."
./scripts/verify-version.sh "$VERSION"

# Step 2: Build and package APKs
echo "📋 Step 2: Building and packaging APKs..."
./scripts/build-and-package.sh "$VERSION"

# Step 3: Get contributors
echo "📋 Step 3: Getting contributors..."
./scripts/get-contributors.sh

# Step 4: Calculate metadata
echo "📋 Step 4: Calculating metadata..."
./scripts/calculate-metadata.sh "$VERSION"

# Step 5: Upload release assets
echo "📋 Step 5: Uploading release assets..."
./scripts/upload-release-assets.sh "$VERSION"

# Step 6: Generate release notes
echo "📋 Step 6: Generating release notes..."
export VERSION="$VERSION"
export REPOSITORY="$REPOSITORY"
export CURRENT_BODY=$(gh release view "v$VERSION" --json body --jq .body 2>/dev/null || echo "")
./scripts/generate-release-notes.sh

# Update the release with enhanced notes
gh release edit "v$VERSION" --notes-file release_notes.md

# Step 7: Display summary
echo "📋 Step 7: Displaying release summary..."
./scripts/release-summary.sh "$VERSION" "$REPOSITORY"

echo "✅ Release build process completed successfully!"