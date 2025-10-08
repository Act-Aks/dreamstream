#!/bin/bash

# Script to check if a release was created and extract version info
# Usage: ./scripts/check-release.sh <semantic-release-output>

set -e

RELEASE_VERSION="$1"

echo "🔍 Checking release status..."

if [ -n "$RELEASE_VERSION" ]; then
    echo "released=true" >> $GITHUB_OUTPUT
    echo "version=$RELEASE_VERSION" >> $GITHUB_OUTPUT
    echo "✅ New version $RELEASE_VERSION was released"
else
    echo "released=false" >> $GITHUB_OUTPUT
    echo "version=" >> $GITHUB_OUTPUT
    echo "ℹ️ No new release was created"
fi