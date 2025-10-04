# 🏷️ Version Management

This document explains how versioning works in the DreamStream monorepo and how to keep version references up-to-date.

---

## 📋 **Overview**

DreamStream uses a **dynamic versioning approach** where the README and documentation automatically reflect the latest released version through dynamic badges and git tags.

### 🎯 **Key Principles**

1. **Single Source of Truth**: Git tags are the authoritative version source
2. **Dynamic Badges**: README uses dynamic badges that auto-update
3. **App Version**: The main app version in `apps/dreamstream/package.json` drives releases
4. **Semantic Versioning**: All versions follow [semver](https://semver.org/) format

---

## 🔄 **Version Sources**

### Primary Version Sources

| Source | Purpose | Example | Auto-Updates |
|--------|---------|---------|--------------|
| **Git Tags** | Release versions | `v1.3.5` | ❌ Manual |
| **App package.json** | Development version | `1.3.0` | ❌ Manual |
| **README Badges** | Public display | Dynamic | ✅ Automatic |
| **Changelog** | Release history | `[1.3.5]` | ❌ Manual |

### Badge URLs

```markdown
<!-- Dynamic version badge (recommended) -->
![Version](https://img.shields.io/github/v/release/Act-Aks/dreamstream?style=for-the-badge&label=version&color=brightgreen)

<!-- Alternative: Latest tag -->
![Version](https://img.shields.io/github/v/tag/Act-Aks/dreamstream?style=for-the-badge&label=version&color=brightgreen)

<!-- Build status -->
![Build](https://img.shields.io/github/actions/workflow/status/Act-Aks/dreamstream/release.yml?style=flat-square&label=build)

<!-- Last commit -->
![Last Commit](https://img.shields.io/github/last-commit/Act-Aks/dreamstream?style=flat-square)
```

---

## 🚀 **Release Workflow**

### 1. Development Phase
```bash
# Work on features in apps/dreamstream/
cd apps/dreamstream
# Version in package.json: 1.3.0
```

### 2. Pre-Release
```bash
# Update app version
bun version:set 1.4.0

# Update changelog
# Add entry to docs/CHANGELOG.md
```

### 3. Release
```bash
# Create and push tag
git tag v1.4.0
git push origin v1.4.0

# Badges automatically update to show v1.4.0
```

### 4. Post-Release
- ✅ README badges show latest version automatically
- ✅ GitHub releases page updated
- ❌ Changelog needs manual update

---

## 🛠️ **Version Management Scripts**

### Available Commands

```bash
# Check current version status
bun version:sync

# Update app version (in apps/dreamstream/)
bun version:set 1.4.0

# Create release (automated via GitHub Actions)
git tag v1.4.0 && git push origin v1.4.0
```

### Manual Version Check

```bash
# Get latest git tag
git tag --sort=-version:refname | head -1

# Get app version
cat apps/dreamstream/package.json | grep '"version"'

# Check if they match
./scripts/update-readme-version.sh
```

---

## 📊 **Version Consistency**

### What Updates Automatically ✅

- **README version badge** - Shows latest git tag/release
- **Build status badge** - Shows latest workflow status  
- **Contributor badges** - Shows current GitHub stats
- **Last commit badge** - Shows latest commit date

### What Needs Manual Updates ❌

- **App package.json version** - Before creating tags
- **Changelog entries** - For each release
- **Release notes** - Via GitHub Actions or manual
- **Documentation version references** - If any exist

---

## 🔍 **Troubleshooting**

### Version Mismatch Issues

**Problem**: README shows old version
```bash
# Check if badges are dynamic
grep "img.shields.io/github/v/" README.md

# Should show dynamic badge URLs, not hardcoded versions
```

**Problem**: App version doesn't match tag
```bash
# Check versions
echo "Git tag: $(git tag --sort=-version:refname | head -1)"
echo "App version: $(cat apps/dreamstream/package.json | jq -r .version)"

# Update app version if needed
cd apps/dreamstream
npm version 1.4.0 --no-git-tag-version
```

**Problem**: Badges not updating
- Check repository name in badge URLs
- Ensure tags are pushed to GitHub: `git push origin --tags`
- Wait a few minutes for GitHub's cache to update

### Common Commands

```bash
# List all tags
git tag --sort=-version:refname

# Check what version badges show
curl -s "https://img.shields.io/github/v/release/Act-Aks/dreamstream.json" | jq .

# Force badge refresh (visit URL in browser)
https://img.shields.io/github/v/release/Act-Aks/dreamstream
```

---

## 📝 **Best Practices**

### For Contributors

1. **Don't hardcode versions** in documentation
2. **Use dynamic badges** for all version displays
3. **Update changelog** when adding features
4. **Test version scripts** before releases

### For Maintainers

1. **Keep app version in sync** with planned releases
2. **Create meaningful git tags** with proper semver
3. **Update changelog** before tagging releases
4. **Verify badges work** after repository changes

### Version Naming

```bash
# Good tag names
v1.4.0      # Major.minor.patch
v1.4.1      # Patch release
v2.0.0      # Major release

# Avoid
1.4.0       # Missing 'v' prefix
v1.4        # Missing patch version
latest      # Not semantic
```

---

## 🔗 **Related Documentation**

- **[Changelog](CHANGELOG.md)** - Version history and release notes
- **[GitHub Workflows](GITHUB_WORKFLOWS.md)** - Automated release process
- **[Contributing](CONTRIBUTING.md)** - How to contribute and version changes

---

<div align="center">

**🏷️ Keep versions in sync, keep badges dynamic! 🏷️**

*For questions about versioning, check the [GitHub Discussions](https://github.com/Act-Aks/dreamstream/discussions)*

</div>