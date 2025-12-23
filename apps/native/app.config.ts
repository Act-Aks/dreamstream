import "tsx/cjs";
import type { ExpoConfig } from "expo/config";
import { withPlugins } from "expo/config-plugins";

const baseConfig = {
    android: {
        adaptiveIcon: { backgroundColor: "#E6F4FE", foregroundImage: "./assets/images/icon.png" },
        edgeToEdgeEnabled: true,
        package: "com.actaks.dreamstream",
        predictiveBackGestureEnabled: false,
        versionCode: 1,
    },
    experiments: { reactCompiler: true, typedRoutes: true },
    extra: { eas: { projectId: "8f9b2549-8352-42ef-a108-78c49aa6fdf7" }, router: {} },
    icon: "./assets/images/icon.png",
    ios: { bundleIdentifier: "com.actaks.dreamstream", supportsTablet: true },
    name: "DreamStream",
    newArchEnabled: true,
    orientation: "default",
    owner: "act-aks",
    scheme: "dreamstream",
    slug: "DreamStream",
    userInterfaceStyle: "automatic",
    version: "1.0.0",
} satisfies ExpoConfig;

const appConfig = ({ config }: { config: ExpoConfig }): ExpoConfig => {
    return withPlugins({ ...config, ...baseConfig }, [
        "expo-router",
        [
            "expo-splash-screen",
            {
                backgroundColor: "#ffffff",
                dark: {
                    backgroundColor: "#000000",
                },
                image: "./assets/images/splash-icon.png",
                imageWidth: 200,
                resizeMode: "contain",
            },
        ],
        [
            "expo-video",
            {
                supportsBackgroundPlayback: true,
                supportsPictureInPicture: true,
            },
        ],
        [
            "expo-build-properties",
            {
                android: {
                    reactNativeArchitectures: ["arm64-v8a", "armeabi-v7a", "x86", "x86_64"],
                },
            },
        ],
        [
            "./mods/plugins/withAbiSplitsPlugin.ts",
            {
                abiFilters: ["arm64-v8a", "armeabi-v7a", "x86", "x86_64"],
            },
        ],
    ]);
};

// Export the base config with plugins applied
module.exports = appConfig;
