#!/usr/bin/env tsx

/**
 * Script to update app version in app.json and package.json
 * Also increments Android versionCode automatically
 * Usage: tsx scripts/update-version.ts <version>
 */

import { readFileSync, writeFileSync } from "node:fs";
import { join } from "node:path";
import appJson from "../app.json" with { type: "json" };

const version = process.argv[2];

if (!version) {
    console.error("Usage: tsx scripts/update-version.ts <version>");
    process.exit(1);
}

// Validate version format (basic semver check)
const semverRegex = /^\d+\.\d+\.\d+(-.*)?$/;
if (!semverRegex.test(version)) {
    console.error("Invalid version format. Expected: x.y.z or x.y.z-suffix");
    process.exit(1);
}

try {
    // Update package.json version line only
    const packageJsonPath = join(__dirname, "..", "package.json");
    const packageJsonContent = readFileSync(packageJsonPath, "utf8");
    const updatedPackageJsonContent = packageJsonContent.replace(/"version":\s*"[^"]*"/, `"version": "${version}"`);
    writeFileSync(packageJsonPath, updatedPackageJsonContent);
    console.log(`✅ Updated package.json version to ${version}`);

    // Increment versionCode for Android
    const currentVersionCode = appJson.expo.android?.versionCode || 1;
    const newVersionCode = currentVersionCode + 1;

    // Update app.json version and versionCode lines only
    const appJsonPath = join(__dirname, "..", "app.json");
    const appJsonContent = readFileSync(appJsonPath, "utf8");
    let updatedAppJsonContent = appJsonContent.replace(/"version":\s*"[^"]*"/, `"version": "${version}"`);
    updatedAppJsonContent = updatedAppJsonContent.replace(/"versionCode":\s*\d+/, `"versionCode": ${newVersionCode}`);
    writeFileSync(appJsonPath, updatedAppJsonContent);
    console.log(`✅ Updated app.json version to ${version}`);
    console.log(`✅ Updated Android versionCode to ${newVersionCode}`);

    console.log(`🎉 Successfully updated version to ${version} and versionCode to ${newVersionCode}`);
} catch (error) {
    const errorMessage = error instanceof Error ? error.message : "Unknown error";
    console.error("❌ Error updating version:", errorMessage);
    process.exit(1);
}
