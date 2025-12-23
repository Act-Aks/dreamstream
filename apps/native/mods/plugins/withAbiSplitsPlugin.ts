import { type ConfigPlugin, withAppBuildGradle } from "expo/config-plugins";

// Regex patterns for gradle file manipulation
const SPLITS_REGEX = /splits\s*\{[\s\S]*?\}/;
const ANDROID_BLOCK_REGEX = /android\s*\{/;

type AbiSplitType = "arm64-v8a" | "armeabi-v7a" | "x86" | "x86_64";
interface AbiSplitProps {
    abiFilters?: AbiSplitType[];
}

const withAbiSplits: ConfigPlugin<AbiSplitProps> = (
    config,
    { abiFilters = ["arm64-v8a", "armeabi-v7a", "x86", "x86_64"] } = {}
) =>
    withAppBuildGradle(config, (configGradle) => {
        const filters = abiFilters.map((abi) => `"${abi}"`).join(", ");
        const splitsBlock = `
    splits {
        abi {
            enable true
            reset()
            include ${filters}
            universalApk true
        }
    }`;

        const gradle = configGradle.modResults.contents;
        const hasSplits = SPLITS_REGEX.test(gradle);

        configGradle.modResults.contents = hasSplits
            ? gradle.replace(SPLITS_REGEX, splitsBlock)
            : gradle.replace(ANDROID_BLOCK_REGEX, (match) => `${match}\n${splitsBlock}\n`);

        return configGradle;
    });

export default withAbiSplits;
