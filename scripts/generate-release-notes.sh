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
required_vars=("VERSION" "REPOSITORY" "APP_NAME" "QR_BASE64" "CONTRIBUTORS" "CHANGELOG" "COMMIT_COUNT")
for var in "${required_vars[@]}"; do
    if [ -z "${!var}" ]; then
        echo "❌ Required environment variable $var is not set"
        exit 1
    fi
done

# Optional variables (may not be set if APKs weren't built)
optional_vars=("APK_SIZES" "TOTAL_SIZE" "UNIVERSAL_SIZE" "ARM64_SIZE" "ARM32_SIZE" "X86_64_SIZE" "X86_SIZE")
for var in "${optional_vars[@]}"; do
    if [ -z "${!var}" ]; then
        echo "⚠️ Optional environment variable $var is not set"
    fi
done

# Get additional variables
export PREVIOUS_TAG=$(git describe --tags --abbrev=0 HEAD~1 2>/dev/null || echo "HEAD~10")
export BUILD_DATE=$(date -u +"%Y-%m-%d %H:%M:%S UTC")

echo "📝 Processing template with Python..."
echo "🔍 Debug info:"
echo "  VERSION: $VERSION"
echo "  REPOSITORY: $REPOSITORY"
echo "  APP_NAME: $APP_NAME"
echo "  APK_SIZES: $APK_SIZES"
echo "  TOTAL_SIZE: $TOTAL_SIZE"
echo "  UNIVERSAL_SIZE: $UNIVERSAL_SIZE"
echo "  ARM64_SIZE: $ARM64_SIZE"
echo "  ARM32_SIZE: $ARM32_SIZE"
echo "  X86_64_SIZE: $X86_64_SIZE"
echo "  X86_SIZE: $X86_SIZE"
echo "  COMMIT_COUNT: $COMMIT_COUNT"
echo "  QR_BASE64 length: ${#QR_BASE64}"
echo "  CONTRIBUTORS lines: $(echo "$CONTRIBUTORS" | wc -l)"
echo "  CHANGELOG lines: $(echo "$CHANGELOG" | wc -l)"

# Use Python for robust text replacement
python3 << 'EOF'
import os
import sys

# Read template
try:
    with open('.github/release-template.md', 'r', encoding='utf-8') as f:
        template = f.read()
except Exception as e:
    print(f"❌ Error reading template: {e}")
    sys.exit(1)

# Get environment variables
replacements = {
    '{{CURRENT_BODY}}': os.getenv('CURRENT_BODY', ''),
    '{{QR_BASE64}}': os.getenv('QR_BASE64', ''),
    '{{APP_NAME}}': os.getenv('APP_NAME', ''),
    '{{VERSION}}': os.getenv('VERSION', ''),
    '{{REPOSITORY}}': os.getenv('REPOSITORY', ''),
    '{{APK_SIZES}}': os.getenv('APK_SIZES', ''),
    '{{TOTAL_SIZE}}': os.getenv('TOTAL_SIZE', 'N/A'),
    '{{UNIVERSAL_SIZE}}': os.getenv('UNIVERSAL_SIZE', 'N/A'),
    '{{ARM64_SIZE}}': os.getenv('ARM64_SIZE', 'N/A'),
    '{{ARM32_SIZE}}': os.getenv('ARM32_SIZE', 'N/A'),
    '{{X86_64_SIZE}}': os.getenv('X86_64_SIZE', 'N/A'),
    '{{X86_SIZE}}': os.getenv('X86_SIZE', 'N/A'),
    '{{CHANGELOG}}': os.getenv('CHANGELOG', ''),
    '{{CONTRIBUTORS}}': os.getenv('CONTRIBUTORS', ''),
    '{{PREVIOUS_TAG}}': os.getenv('PREVIOUS_TAG', ''),
    '{{BUILD_DATE}}': os.getenv('BUILD_DATE', ''),
    '{{COMMIT_COUNT}}': os.getenv('COMMIT_COUNT', ''),
}

# Replace all placeholders
content = template
for placeholder, value in replacements.items():
    if value is None:
        value = ''
    content = content.replace(placeholder, str(value))

# Write output
try:
    with open('release_notes.md', 'w', encoding='utf-8') as f:
        f.write(content)
    print("✅ Release notes generated successfully!")
except Exception as e:
    print(f"❌ Error writing output: {e}")
    sys.exit(1)
EOF

echo "📄 Output file: release_notes.md"