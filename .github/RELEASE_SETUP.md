# Release Setup Guide

This guide explains how to set up semantic-release with GitHub Packages and branch protection rules.

## Personal Access Token (PAT) Setup

Since this repository uses branch protection rules that require PRs for all commits to `main`, semantic-release needs a Personal Access Token (PAT) with bypass permissions to push version updates directly.

### Step 1: Create a Personal Access Token

1. Go to **GitHub Settings** → **Developer settings** → **Personal access tokens** → **Tokens (classic)**
2. Click **Generate new token** → **Generate new token (classic)**
3. Give it a descriptive name: `DreamStream Release Bot`
4. Set expiration (recommended: 90 days or custom)
5. Select the following scopes:
   - ✅ **`repo`** (Full control of private repositories)
     - This includes: `repo:status`, `repo_deployment`, `public_repo`, `repo:invite`, `security_events`
   - ✅ **`write:packages`** (Upload packages to GitHub Package Registry)
   - ✅ **`read:packages`** (Download packages from GitHub Package Registry)
6. Click **Generate token**
7. **Copy the token immediately** (you won't be able to see it again!)

### Step 2: Add Token to Repository Secrets

1. Go to your repository on GitHub
2. Navigate to **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Name: `GH_RELEASE_PAT`
5. Value: Paste your PAT token
6. Click **Add secret**

### Step 3: Grant Bypass Permissions (Required for Branch Protection)

The PAT alone won't bypass branch protection. You need to ensure the token belongs to a user/account that has **admin or maintainer** permissions on the repository AND has bypass permissions enabled.

1. Go to **Settings** → **Branches** → Your branch protection rule for `main`
2. Under **"Restrict who can push to matching branches"**, ensure:
   - The account that owns the PAT is listed as an allowed bypasser, OR
   - The account has **admin** or **maintainer** role on the repository
3. If using an organization, ensure the account has appropriate permissions

### Step 4: Verify Setup

After pushing to `main`, the release workflow should:
- ✅ Build packages with Turborepo
- ✅ Run semantic-release for each package
- ✅ Update `package.json` version fields
- ✅ Commit and push version updates to `main` (bypassing branch protection)
- ✅ Create Git tags (e.g., `core-v1.0.1`)
- ✅ Publish packages to GitHub Packages
- ✅ Create GitHub Releases

## Troubleshooting

### Release workflow fails with "Resource not accessible by integration"

- **Cause**: The `GITHUB_TOKEN` doesn't have bypass permissions
- **Solution**: Ensure `GH_RELEASE_PAT` secret is set and the workflow uses it (already configured)

### Commits are blocked by branch protection

- **Cause**: The PAT account doesn't have bypass permissions
- **Solution**: Add the PAT owner as an admin/maintainer or explicitly allow them in branch protection settings

### Packages fail to publish

- **Cause**: Missing `write:packages` scope or incorrect `.npmrc` configuration
- **Solution**: Verify PAT has `write:packages` scope and `.npmrc` points to GitHub Packages

## Security Notes

- ⚠️ **Never commit the PAT token** to the repository
- 🔒 Store it only in GitHub Secrets
- 🔄 Rotate the token periodically (every 90 days recommended)
- 👥 Use a dedicated GitHub account/bot for releases if possible
- 📝 Monitor release workflow runs for any unauthorized access

## Alternative: Use GitHub App (Advanced)

For better security and granular permissions, consider creating a GitHub App instead of a PAT. This requires additional setup but provides:
- More granular permissions
- Better audit trails
- Easier rotation
- Organization-level management

See [GitHub Apps documentation](https://docs.github.com/en/apps) for more information.

