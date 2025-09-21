#!/usr/bin/env tsx

/**
 * Script to update app version in app.json and package.json
 * Usage: tsx scripts/update-version.ts <version>
 */

import { writeFileSync } from "node:fs";
import { join } from "node:path";
import appJson from "../app.json" with { type: "json" };
import packageJson from "../package.json" with { type: "json" };

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
    // Update package.json with proper typing
    const updatedPackageJson = {
        ...packageJson,
        version,
    };

    const packageJsonPath = join(__dirname, "..", "package.json");
    writeFileSync(packageJsonPath, `${JSON.stringify(updatedPackageJson, null, 2)}\n`);
    console.log(`✅ Updated package.json version to ${version}`);

    // Update app.json with proper typing
    const updatedAppJson = {
        ...appJson,
        expo: {
            ...appJson.expo,
            version,
        },
    };

    const appJsonPath = join(__dirname, "..", "app.json");
    writeFileSync(appJsonPath, `${JSON.stringify(updatedAppJson, null, 2)}\n`);
    console.log(`✅ Updated app.json version to ${version}`);

    console.log(`🎉 Successfully updated version to ${version}`);
} catch (error) {
    const errorMessage = error instanceof Error ? error.message : "Unknown error";
    console.error("❌ Error updating version:", errorMessage);
    process.exit(1);
}
