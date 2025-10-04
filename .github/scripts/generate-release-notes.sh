#!/bin/bash

# Script to generate release notes from template
# Usage: ./generate-release-notes.sh

set -e

echo "🔄 Generating release notes from template..."

# Check if template exists
if [ ! -f ".github/release-template.md" ]; then
    echo "❌ Release template not found at .github/release-template.md"
    exit 1
fi

# Check required environment variables
required_vars=("VERSION" "REPOSITORY" "APP_NAME" "APK_SIZE" "QR_BASE64" "CONTRIBUTORS" "CHANGELOG" "COMMIT_COUNT")
for var in "${required_vars[@]}"; do
    if [ -z "${!var}" ]; then
        echo "❌ Required environment variable $var is not set"
        exit 1
    fi
done

# Get additional variables
PREVIOUS_TAG=$(git describe --tags --abbrev=0 HEAD~1 2>/dev/null || echo "HEAD~10")
BUILD_DATE=$(date -u +"%Y-%m-%d %H:%M:%S UTC")

# Create temporary file for processing
cp .github/release-template.md /tmp/release_notes.md

# Function to safely replace text in file
safe_replace() {
    local placeholder="$1"
    local value="$2"
    local file="$3"
    
    # Escape special characters for sed
    escaped_value=$(printf '%s\n' "$value" | sed 's/[[\.*^$()+?{|]/\\&/g')
    
    # Use a different delimiter to avoid conflicts
    sed -i "s|$placeholder|$escaped_value|g" "$file"
}

echo "📝 Replacing template variables..."

# Replace all template variables
safe_replace "{{CURRENT_BODY}}" "$CURRENT_BODY" "/tmp/release_notes.md"
safe_replace "{{QR_BASE64}}" "$QR_BASE64" "/tmp/release_notes.md"
safe_replace "{{APP_NAME}}" "$APP_NAME" "/tmp/release_notes.md"
safe_replace "{{VERSION}}" "$VERSION" "/tmp/release_notes.md"
safe_replace "{{REPOSITORY}}" "$REPOSITORY" "/tmp/release_notes.md"
safe_replace "{{APK_SIZE}}" "$APK_SIZE" "/tmp/release_notes.md"
safe_replace "{{CHANGELOG}}" "$CHANGELOG" "/tmp/release_notes.md"
safe_replace "{{CONTRIBUTORS}}" "$CONTRIBUTORS" "/tmp/release_notes.md"
safe_replace "{{PREVIOUS_TAG}}" "$PREVIOUS_TAG" "/tmp/release_notes.md"
safe_replace "{{BUILD_DATE}}" "$BUILD_DATE" "/tmp/release_notes.md"
safe_replace "{{COMMIT_COUNT}}" "$COMMIT_COUNT" "/tmp/release_notes.md"

# Copy final result
cp /tmp/release_notes.md release_notes.md

echo "✅ Release notes generated successfully!"
echo "📄 Output file: release_notes.md"