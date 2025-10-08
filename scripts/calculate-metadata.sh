#!/bin/bash

# Script to calculate APK metadata and set environment variables
# Usage: ./scripts/calculate-metadata.sh <version>

set -e

VERSION="$1"

if [ -z "$VERSION" ]; then
    echo "❌ Version not provided"
    exit 1
fi

# Export VERSION so child processes can access it
export VERSION

cd apps/dreamstream

echo "📊 Calculating APK metadata for version $VERSION..."

# Get app metadata
APP_NAME=$(node -e "console.log(require('./app.json').expo.name)")
echo "APP_NAME=$APP_NAME" >> $GITHUB_ENV

# Set build date
BUILD_DATE=$(date -u '+%Y-%m-%d %H:%M:%S UTC')
echo "BUILD_DATE=$BUILD_DATE" >> $GITHUB_ENV

# Calculate APK sizes
echo "📏 Calculating APK sizes..."
bun run calc-sizes

# Get commit info and changelog
PREVIOUS_TAG=$(gh release list --limit 2 --json tagName --jq '.[1].tagName // ""' 2>/dev/null || echo "")
echo "PREVIOUS_TAG=$PREVIOUS_TAG" >> $GITHUB_ENV

COMMIT_RANGE="${PREVIOUS_TAG:+$PREVIOUS_TAG..}HEAD"
COMMIT_COUNT=$(git rev-list --count ${COMMIT_RANGE:-HEAD~20..HEAD})
CHANGELOG=$(git log ${COMMIT_RANGE:-HEAD~20..HEAD} --oneline --pretty=format:"- %s" | head -10 || echo "- Initial release")

echo "COMMIT_COUNT=$COMMIT_COUNT" >> $GITHUB_ENV
echo "CHANGELOG<<EOF" >> $GITHUB_ENV
echo "$CHANGELOG" >> $GITHUB_ENV
echo "EOF" >> $GITHUB_ENV

echo "✅ Metadata calculation completed"
echo "📱 App: $APP_NAME"
echo "🔢 Version: $VERSION"
echo "📅 Build Date: $BUILD_DATE"
echo "🔄 Commits: $COMMIT_COUNT"