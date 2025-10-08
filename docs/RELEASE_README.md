# Release Scripts

This directory contains scripts to manage the release process for DreamStream. These scripts help keep the CI workflow clean by extracting complex logic into reusable, testable scripts.

## Scripts Overview

### Core Release Scripts

- **`release-build.sh`** - Main orchestration script that runs the entire release build process
- **`check-release.sh`** - Checks if semantic-release created a new release and sets GitHub outputs
- **`verify-version.sh`** - Verifies version consistency between app.json and the expected release version
- **`build-and-package.sh`** - Builds APK variants and generates QR codes
- **`get-contributors.sh`** - Fetches contributors for the current release with GitHub profile links
- **`calculate-metadata.sh`** - Calculates APK sizes, commit counts, and other release metadata
- **`upload-release-assets.sh`** - Uploads APK variants and QR codes to the GitHub release
- **`release-summary.sh`** - Displays a comprehensive release summary
- **`generate-release-notes.sh`** - Generates enhanced release notes from template

### Utility Scripts

- **`update-readme-version.sh`** - Updates version references in README files

## Usage

### Individual Scripts

Each script can be run independently:

```bash
# Check if a release was created
./scripts/check-release.sh "1.3.0"

# Verify version consistency
./scripts/verify-version.sh "1.3.0"

# Build and package APKs
./scripts/build-and-package.sh "1.3.0"

# Get contributors for release
./scripts/get-contributors.sh

# Calculate metadata
./scripts/calculate-metadata.sh "1.3.0"

# Upload release assets
./scripts/upload-release-assets.sh "1.3.0"

# Generate release notes
./scripts/generate-release-notes.sh

# Display release summary
./scripts/release-summary.sh "1.3.0" "owner/repo"
```

### Full Release Build

Run the complete release build process:

```bash
./scripts/release-build.sh "1.3.0" "owner/dreamstream"
```

## Environment Variables

Some scripts require specific environment variables to be set:

### Required for most scripts:
- `GITHUB_TOKEN` or `GH_TOKEN` - GitHub token for API access
- `VERSION` - Release version number

### Required for release notes generation:
- `APP_NAME` - Application name from app.json
- `REPOSITORY` - GitHub repository in format "owner/repo"
- `CONTRIBUTORS` - List of contributors (set by get-contributors.sh)
- `CHANGELOG` - Commit changelog (set by calculate-metadata.sh)
- `COMMIT_COUNT` - Number of commits (set by calculate-metadata.sh)
- `APK_SIZES` - APK size information (set by calculate-metadata.sh)
- `TOTAL_SIZE` - Total size of all APKs (set by calculate-metadata.sh)
- `BUILD_DATE` - Build timestamp (set by calculate-metadata.sh)

## CI Integration

The GitHub Actions workflow uses these scripts to keep the workflow file clean and maintainable:

```yaml
- name: Check release
  run: ./scripts/check-release.sh "${{ steps.semantic-release.outputs.next-release-version }}"

- name: Build and package APKs
  run: ./scripts/build-and-package.sh "${{ needs.release.outputs.version }}"

- name: Upload release assets
  run: ./scripts/upload-release-assets.sh "${{ needs.release.outputs.version }}"
```

## Benefits

1. **Maintainability** - Complex logic is extracted from CI YAML into versioned scripts
2. **Testability** - Scripts can be tested locally before pushing to CI
3. **Reusability** - Scripts can be used in different contexts (CI, local development, etc.)
4. **Debugging** - Easier to debug issues when logic is in shell scripts vs YAML
5. **Documentation** - Each script has clear purpose and usage instructions

## Development

When modifying scripts:

1. Test locally before committing
2. Ensure scripts are executable (`chmod +x`)
3. Follow the existing error handling patterns (`set -e`)
4. Add appropriate logging and status messages
5. Update this README if adding new scripts