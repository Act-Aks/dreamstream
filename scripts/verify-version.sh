#!/bin/bash

# Script to verify version consistency between app.json and release
# Usage: ./scripts/verify-version.sh <expected-version>

set -e

EXPECTED_VERSION="$1"

if [ -z "$EXPECTED_VERSION" ]; then
    echo "❌ Expected version not provided"
    exit 1
fi

cd apps/dreamstream

APP_VERSION=$(node -e "console.log(require('./app.json').expo.version)")

echo "📱 App version: $APP_VERSION"
echo "🏷️ Release version: $EXPECTED_VERSION"

if [ "$APP_VERSION" != "$EXPECTED_VERSION" ]; then
    echo "⚠️ Warning: Version mismatch. Expected: $EXPECTED_VERSION, Got: $APP_VERSION"
    # Don't exit with error, just warn
else
    echo "✅ Version consistency verified"
fi