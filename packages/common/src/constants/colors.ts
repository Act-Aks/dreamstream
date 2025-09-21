export type ColorPalette = {
    accent: string;
    background: string;
    backgroundSecondary: string;
    border: string;
    borderLight: string;
    disabled: string;
    error: string;
    highlight: string;
    info: string;
    overlay: string;
    placeholder: string;
    primary: string;
    primaryDark: string;
    primaryLight: string;
    secondary: string;
    secondaryDark: string;
    secondaryLight: string;
    shadow: string;
    success: string;
    surface: string;
    surfaceSecondary: string;
    text: string;
    textInverse: string;
    textMuted: string;
    textSecondary: string;
    transparent: string;
    warning: string;
};
export type GradientColors = typeof GRADIENT_COLORS;
export type ThemeColors = {
    light: ColorPalette;
    dark: ColorPalette;
    gradients: GradientColors;
};

export const GRADIENT_COLORS = {
    error: ["#ff9a9e", "#fecfef"],
    forest: ["#134e5e", "#71b280"],
    ocean: ["#667eea", "#764ba2", "#667eea"],
    primary: ["#667eea", "#764ba2"],
    secondary: ["#f093fb", "#f5576c"],
    success: ["#4facfe", "#00f2fe"],
    sunset: ["#fa709a", "#fee140", "#ffecd2"],
    warning: ["#fa709a", "#fee140"],
} as const;

export const THEME_OPTIONS = {
    amber: {
        name: "Golden Amber",
        primary: "#D97706",
        primaryDark: "#B45309",
        primaryLight: "#F59E0B",
    },
    aqua: {
        name: "Aqua Marine",
        primary: "#0891B2",
        primaryDark: "#0E7490",
        primaryLight: "#06B6D4",
    },
    aurora: {
        name: "Aurora Purple",
        primary: "#7C3AED",
        primaryDark: "#5B21B6",
        primaryLight: "#8B5CF6",
    },
    crimson: {
        name: "Crimson Fire",
        primary: "#DC2626",
        primaryDark: "#B91C1C",
        primaryLight: "#EF4444",
    },
    emerald: {
        name: "Emerald Dream",
        primary: "#059669",
        primaryDark: "#047857",
        primaryLight: "#10B981",
    },
    midnight: {
        name: "Midnight Blue",
        primary: "#1E40AF",
        primaryDark: "#1E3A8A",
        primaryLight: "#3B82F6",
    },
    navy: {
        name: "Deep Navy",
        primary: "#1E3A8A",
        primaryDark: "#1E40AF",
        primaryLight: "#3B82F6",
    },
    plum: {
        name: "Royal Plum",
        primary: "#A21CAF",
        primaryDark: "#86198F",
        primaryLight: "#C026D3",
    },
    rose: {
        name: "Rose Garden",
        primary: "#E11D48",
        primaryDark: "#BE123C",
        primaryLight: "#F43F5E",
    },
    sage: {
        name: "Sage Green",
        primary: "#16A34A",
        primaryDark: "#15803D",
        primaryLight: "#22C55E",
    },
    sunset: {
        name: "Sunset Coral",
        primary: "#F97316",
        primaryDark: "#EA580C",
        primaryLight: "#FB923C",
    },
    violet: {
        name: "Deep Violet",
        primary: "#9333EA",
        primaryDark: "#7E22CE",
        primaryLight: "#A855F7",
    },
} as const;

export const SEMANTIC_COLORS = {
    accent: {
        default: "#F1F5F9",
        foreground: "#0F172A",
    },
    card: {
        default: "#FFFFFF",
        foreground: "#0F172A",
    },
    constructive: {
        default: "#059669",
        foreground: "#FFFFFF",
    },
    destructive: {
        default: "#DC2626",
        foreground: "#FFFFFF",
    },
    muted: {
        default: "#F1F5F9",
        foreground: "#64748B",
    },
    popover: {
        default: "#FFFFFF",
        foreground: "#0F172A",
    },
} as const;

export const createThemeColors = (primaryTheme: keyof typeof THEME_OPTIONS): ThemeColors => {
    const selectedTheme = THEME_OPTIONS[primaryTheme];

    return {
        dark: {
            accent: selectedTheme.primaryLight,
            background: "#0F172A",
            backgroundSecondary: "#1E293B",
            border: "#334155",
            borderLight: "#475569",
            disabled: "#334155",
            error: "#EF4444",
            highlight: "#451A03",
            info: "#3B82F6",
            overlay: "rgba(0, 0, 0, 0.7)",
            placeholder: "#64748B",
            primary: selectedTheme.primary,
            primaryDark: selectedTheme.primaryDark,
            primaryLight: selectedTheme.primaryLight,
            secondary: "#94A3B8",
            secondaryDark: "#64748B",
            secondaryLight: "#CBD5E1",
            shadow: "rgba(0, 0, 0, 0.25)",
            success: "#10B981",
            surface: "#1E293B",
            surfaceSecondary: "#334155",
            text: "#F8FAFC",
            textInverse: "#0F172A",
            textMuted: "#94A3B8",
            textSecondary: "#CBD5E1",
            transparent: "transparent",
            warning: "#F59E0B",
        },
        gradients: GRADIENT_COLORS,
        light: {
            accent: selectedTheme.primaryLight,
            background: "#FFFFFF",
            backgroundSecondary: "#F8FAFC",
            border: "#E2E8F0",
            borderLight: "#F1F5F9",
            disabled: "#E2E8F0",
            error: "#DC2626",
            highlight: "#FEF3C7",
            info: "#0284C7",
            overlay: "rgba(15, 23, 42, 0.5)",
            placeholder: "#94A3B8",
            primary: selectedTheme.primary,
            primaryDark: selectedTheme.primaryDark,
            primaryLight: selectedTheme.primaryLight,
            secondary: "#64748B",
            secondaryDark: "#475569",
            secondaryLight: "#94A3B8",
            shadow: "rgba(15, 23, 42, 0.08)",
            success: "#059669",
            surface: "#FFFFFF",
            surfaceSecondary: "#F1F5F9",
            text: "#0F172A",
            textInverse: "#FFFFFF",
            textMuted: "#94A3B8",
            textSecondary: "#64748B",
            transparent: "transparent",
            warning: "#D97706",
        },
    };
};

// Color utility constants
const HEX_BASE = 16;
const RGB_START_INDEX = 1;
const RGB_RED_END = 3;
const RGB_GREEN_START = 3;
const RGB_GREEN_END = 5;
const RGB_BLUE_START = 5;
const RGB_BLUE_END = 7;
const PERCENT_TO_RGB_MULTIPLIER = 2.55;
const RGB_MAX_VALUE = 255;
const RGB_MIN_VALUE = 0;
const HEX_STRING_BASE = 16;
const COLOR_CHANNEL_SIZE = 256;

export const colorPresets = {
    darken: (color: string, percent: number): string => colorPresets.lighten(color, -percent),
    hexToRgba: (hex: string, opacity: number): string => {
        const r = Number.parseInt(hex.slice(RGB_START_INDEX, RGB_RED_END), HEX_BASE);
        const g = Number.parseInt(hex.slice(RGB_GREEN_START, RGB_GREEN_END), HEX_BASE);
        const b = Number.parseInt(hex.slice(RGB_BLUE_START, RGB_BLUE_END), HEX_BASE);
        return `rgba(${r}, ${g}, ${b}, ${opacity})`;
    },

    lighten: (color: string, percent: number): string => {
        const hex = color.replace("#", "");
        const num = Number.parseInt(hex, HEX_BASE);
        const amt = Math.round(PERCENT_TO_RGB_MULTIPLIER * percent);

        // Extract RGB components using division and modulo instead of bitwise operations
        const r = Math.floor(num / (COLOR_CHANNEL_SIZE * COLOR_CHANNEL_SIZE)) + amt;
        const g = Math.floor((num % (COLOR_CHANNEL_SIZE * COLOR_CHANNEL_SIZE)) / COLOR_CHANNEL_SIZE) + amt;
        const b = (num % COLOR_CHANNEL_SIZE) + amt;

        // Clamp values between 0 and 255
        const clampedR = Math.max(RGB_MIN_VALUE, Math.min(RGB_MAX_VALUE, r));
        const clampedG = Math.max(RGB_MIN_VALUE, Math.min(RGB_MAX_VALUE, g));
        const clampedB = Math.max(RGB_MIN_VALUE, Math.min(RGB_MAX_VALUE, b));

        // Convert back to hex
        const HexPaddingLength = 2;
        const HexPaddingChar = "0";
        const hexR = clampedR.toString(HEX_STRING_BASE).padStart(HexPaddingLength, HexPaddingChar);
        const hexG = clampedG.toString(HEX_STRING_BASE).padStart(HexPaddingLength, HexPaddingChar);
        const hexB = clampedB.toString(HEX_STRING_BASE).padStart(HexPaddingLength, HexPaddingChar);

        return `#${hexR}${hexG}${hexB}`;
    },
};
