import type { Config } from "jest";

const config: Config = {
    cacheDirectory: ".jest/cache",
    clearMocks: true,
    collectCoverage: true,
    collectCoverageFrom: [
        "./src/**",
        "!**/coverage/**",
        "!**/node_modules/**",
        "!**/babel.config.js",
        "!**/expo-env.d.ts",
        "!**/.expo/**",
        "!**/__snapshots__/**",
    ],
    moduleFileExtensions: ["ts", "tsx", "js", "jsx", "node"],
    preset: "jest-expo",
    setupFilesAfterEnv: [
        "<rootDir>/src/testing/setupTests.tsx",
        "./node_modules/react-native-gesture-handler/jestSetup.js",
    ],
    testPathIgnorePatterns: ["/node_modules/", "/android/", "/ios/"],
    testRegex: "(/__tests__/.*|(\\.|/)(test|spec))\\.(js|ts)x?$",
    transformIgnorePatterns: [
        "node_modules/(?!(jest-)?react-native|@react-native|@react-navigation|@expo|expo-constants)",
    ],
    verbose: true,
};

export default config;
