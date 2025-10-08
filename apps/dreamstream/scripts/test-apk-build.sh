#!/bin/bash

# Test script to verify APK variants are being generated correctly
# Usage: ./scripts/test-apk-build.sh

set -e

echo "🧪 Testing APK variant generation..."

cd "$(dirname "$0")/.."

echo "📋 Current configuration:"
echo "expo-build-properties in app.json:"
node -e "
const config = require('./app.json');
const buildProps = config.expo.plugins.find(p => Array.isArray(p) && p[0] === 'expo-build-properties');
if (buildProps) {
  console.log(JSON.stringify(buildProps[1], null, 2));
} else {
  console.log('expo-build-properties not found in plugins');
}
"

echo ""
echo "📦 Dependencies:"
echo "expo-build-properties version:"
node -e "
try {
  const pkg = require('./node_modules/expo-build-properties/package.json');
  console.log(pkg.version);
} catch (e) {
  console.log('Not installed');
}
"

echo ""
echo "🏗️ Running prebuild to check configuration..."
bun expo prebuild --clean

echo ""
echo "📱 Checking generated Android configuration..."
if [ -f "android/app/build.gradle" ]; then
  echo "✅ Android build.gradle exists"
  
  # Check if splits are configured
  if grep -q "splits" android/app/build.gradle; then
    echo "✅ APK splits configuration found in build.gradle"
    echo "Splits configuration:"
    grep -A 10 "splits" android/app/build.gradle || echo "Could not extract splits config"
  else
    echo "⚠️ No splits configuration found in build.gradle"
  fi
  
  # Check ABI filters
  if grep -q "abiFilters" android/app/build.gradle; then
    echo "✅ ABI filters found in build.gradle"
    echo "ABI filters:"
    grep -A 5 "abiFilters" android/app/build.gradle || echo "Could not extract ABI filters"
  else
    echo "⚠️ No ABI filters found in build.gradle"
  fi
else
  echo "❌ Android build.gradle not found"
fi

echo ""
echo "🎯 Test completed. If you see splits and ABI filters configured, expo-build-properties is working correctly."
echo "To build APK variants, run: bun run build:android:release"