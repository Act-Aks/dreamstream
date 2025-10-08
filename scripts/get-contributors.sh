#!/bin/bash

# Script to get contributors for the current release
# Usage: ./scripts/get-contributors.sh

set -e

echo "👥 Getting contributors for this release..."

# Get the previous release tag
PREVIOUS_TAG=$(gh release list --limit 2 --json tagName --jq '.[1].tagName // ""' 2>/dev/null || echo "")

if [ -z "$PREVIOUS_TAG" ]; then
    echo "No previous release found, using last 20 commits"
    COMMIT_RANGE="HEAD~20..HEAD"
else
    echo "Previous tag: $PREVIOUS_TAG"
    COMMIT_RANGE="${PREVIOUS_TAG}..HEAD"
fi

# Create temp file for contributors
TEMP_FILE=$(mktemp)

# Get unique contributors
git log $COMMIT_RANGE --format='%ae|%an' | sort -u | while IFS='|' read -r email name; do
    if [ ! -z "$email" ] && [ "$email" != "noreply@github.com" ] && [ "$name" != "dependabot[bot]" ]; then
        # Clean email for search (remove special characters)
        CLEAN_EMAIL=$(echo "$email" | sed 's/[^a-zA-Z0-9@._-]//g')
        
        # Try to get GitHub username from email (with proper error handling)
        if [ ! -z "$CLEAN_EMAIL" ]; then
            GITHUB_USER=$(gh api "search/users?q=${CLEAN_EMAIL}+in:email" --jq '.items[0].login // empty' 2>/dev/null || echo "")
            
            if [ ! -z "$GITHUB_USER" ] && [ "$GITHUB_USER" != "null" ]; then
                AVATAR_URL=$(gh api "users/$GITHUB_USER" --jq '.avatar_url // empty' 2>/dev/null || echo "")
                if [ ! -z "$AVATAR_URL" ] && [ "$AVATAR_URL" != "null" ]; then
                    echo "- <img src=\"$AVATAR_URL\" width=\"20\" height=\"20\" style=\"border-radius: 50%; vertical-align: middle;\"> **[@$GITHUB_USER](https://github.com/$GITHUB_USER)** ($name)" >> "$TEMP_FILE"
                else
                    echo "- **[@$GITHUB_USER](https://github.com/$GITHUB_USER)** ($name)" >> "$TEMP_FILE"
                fi
            else
                echo "- **$name** ($email)" >> "$TEMP_FILE"
            fi
        else
            echo "- **$name**" >> "$TEMP_FILE"
        fi
    fi
done

# Read contributors from temp file
if [ -f "$TEMP_FILE" ] && [ -s "$TEMP_FILE" ]; then
    CONTRIBUTORS_JSON=$(cat "$TEMP_FILE")
else
    # Fallback if still empty
    CONTRIBUTORS_JSON=$(git log $COMMIT_RANGE --format='- **%an**' | sort -u | head -10)
    if [ -z "$CONTRIBUTORS_JSON" ]; then
        CONTRIBUTORS_JSON="- **No contributors found for this release**"
    fi
fi

# Save contributors to environment
echo "CONTRIBUTORS<<EOF" >> $GITHUB_ENV
echo "$CONTRIBUTORS_JSON" >> $GITHUB_ENV
echo "EOF" >> $GITHUB_ENV

# Clean up
rm -f "$TEMP_FILE"

echo "✅ Found contributors for release"