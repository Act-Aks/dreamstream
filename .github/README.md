# GitHub Workflows & Templates

This directory contains GitHub Actions workflows and release templates for the DreamStream project.

## 📁 Structure

```
.github/
├── workflows/
│   └── release.yml          # Main release workflow
├── scripts/
│   └── generate-release-notes.sh  # Release notes generator script
├── release-template.md      # Release notes template
└── README.md               # This file
```

## 🚀 Release Workflow

The release workflow (`workflows/release.yml`) automatically:

1. **Detects new releases** using semantic-release
2. **Builds Android APK** with proper versioning
3. **Generates QR code** for direct APK download
4. **Collects contributors** with GitHub profiles and avatars
5. **Creates changelog** from git commits
6. **Generates release notes** using the template
7. **Uploads assets** and updates the GitHub release

## 📝 Release Template

The `release-template.md` file contains a reusable template for release notes with placeholders:

### Available Placeholders

| Placeholder | Description | Example |
|-------------|-------------|---------|
| `{{CURRENT_BODY}}` | Existing release notes from semantic-release | Auto-generated changelog |
| `{{QR_BASE64}}` | Base64 encoded QR code image | `iVBORw0KGgoAAAANSUhEUgAA...` |
| `{{APP_NAME}}` | Application name from app.json | `DreamStream` |
| `{{VERSION}}` | Release version | `1.2.0` |
| `{{REPOSITORY}}` | GitHub repository path | `username/dreamstream` |
| `{{APK_SIZE}}` | APK file size | `25M` |
| `{{CHANGELOG}}` | Recent commits formatted as list | `- feat: add new feature` |
| `{{CONTRIBUTORS}}` | Contributors with GitHub profiles | `- **[@username](...)** (Name)` |
| `{{PREVIOUS_TAG}}` | Previous release tag | `v1.1.0` |
| `{{BUILD_DATE}}` | Build timestamp | `2024-01-15 10:30:00 UTC` |
| `{{COMMIT_COUNT}}` | Number of commits since last release | `15` |

## 🔧 Customizing the Template

To modify the release notes format:

1. **Edit** `.github/release-template.md`
2. **Use placeholders** from the table above
3. **Test locally** by running the script manually
4. **Commit changes** - next release will use the updated template

### Example Customization

```markdown
## 🎉 What's New in {{VERSION}}

{{CHANGELOG}}

## 📱 Download

Scan this QR code with your phone:

<img src="data:image/png;base64,{{QR_BASE64}}" width="150">

Or download directly: [APK ({{APK_SIZE}})](https://github.com/{{REPOSITORY}}/releases/download/v{{VERSION}}/DreamStream-v{{VERSION}}.apk)
```

## 🛠️ Manual Testing

To test the release notes generation locally:

```bash
# Set required environment variables
export VERSION="1.0.0"
export REPOSITORY="your-username/your-repo"
export APP_NAME="DreamStream"
export APK_SIZE="25M"
export QR_BASE64="base64-string-here"
export CONTRIBUTORS="- **John Doe**"
export CHANGELOG="- Initial release"
export COMMIT_COUNT="10"
export CURRENT_BODY="Initial release notes"

# Run the script
chmod +x .github/scripts/generate-release-notes.sh
./.github/scripts/generate-release-notes.sh

# Check the output
cat release_notes.md
```

## 🔒 Security Notes

- The workflow uses `secrets.FULL_TOKEN` for GitHub API access
- APKs are built in isolated CI environment
- All assets are signed and verified before upload
- QR codes link directly to official GitHub releases

## 📚 Further Reading

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Semantic Release](https://semantic-release.gitbook.io/)
- [Expo Build Process](https://docs.expo.dev/build/introduction/)