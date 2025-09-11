export type Spacing = {
    xs: number;
    sm: number;
    md: number;
    base: number;
    lg: number;
    xl: number;
    "2xl": number;
    "3xl": number;
    "4xl": number;
};

export const spacing: Spacing = {
    xs: 4,
    sm: 8,
    md: 12,
    base: 16,
    lg: 24,
    xl: 32,
    "2xl": 48,
    "3xl": 64,
    "4xl": 96,
};

export type BorderRadius = {
    none: number;
    sm: number;
    md: number;
    base: number;
    lg: number;
    xl: number;
    full: number;
};

export const borderRadius: BorderRadius = {
    none: 0,
    sm: 4,
    md: 8,
    lg: 12,
    base: 16,
    xl: 20,
    full: 9999,
};

type Elevation = {
    shadowOpacity: number;
    shadowRadius: number;
    shadowOffset: { width: number; height: number };
    elevation: number;
};

export const shadowElevation: Record<"none" | "sm" | "md" | "base" | "lg" | "xl" | "2xl", Elevation> = {
    none: {
        shadowOpacity: 0,
        shadowRadius: 0,
        shadowOffset: { width: 0, height: 0 },
        elevation: 0,
    },
    sm: {
        shadowOpacity: 0.1,
        shadowRadius: 1,
        shadowOffset: { width: 0, height: 1 },
        elevation: 1,
    },
    md: {
        shadowOpacity: 0.15,
        shadowRadius: 3,
        shadowOffset: { width: 0, height: 2 },
        elevation: 3,
    },
    base: {
        shadowOpacity: 0.2,
        shadowRadius: 5,
        shadowOffset: { width: 0, height: 2 },
        elevation: 3,
    },
    lg: {
        shadowOpacity: 0.25,
        shadowRadius: 9,
        shadowOffset: { width: 0, height: 4 },
        elevation: 6,
    },
    xl: {
        shadowOpacity: 0.3,
        shadowRadius: 14,
        shadowOffset: { width: 0, height: 8 },
        elevation: 10,
    },
    "2xl": {
        shadowOpacity: 0.35,
        shadowRadius: 20,
        shadowOffset: { width: 0, height: 12 },
        elevation: 15,
    },
};

// Animation Duration Constants
export type AnimationDuration = {
    fastest: number;
    fast: number;
    normal: number;
    slow: number;
    slowest: number;
};

export const animationDuration: AnimationDuration = {
    fastest: 100,
    fast: 200,
    normal: 300,
    slow: 500,
    slowest: 800,
};

// Z-Index Constants
export type ZIndex = {
    behind: number;
    base: number;
    overlay: number;
    modal: number;
    popover: number;
    tooltip: number;
    toast: number;
    loading: number;
    max: number;
};

export const zIndex: ZIndex = {
    behind: -1,
    base: 0,
    overlay: 1000,
    modal: 1100,
    popover: 1200,
    tooltip: 1300,
    toast: 1400,
    loading: 1500,
    max: 9999,
};

export const opacity = {
    transparent: 0,
    low: 0.25,
    medium: 0.5,
    high: 0.75,
    opaque: 1,
    disabled: 0.4,
    overlay: 0.6,
    backdrop: 0.8,
    pressed: 0.7,
    loading: 0.6,
};

// Utility functions for React Native
export const spacingUtils = {
    // Get spacing value by key
    get: (key: keyof Spacing) => spacing[key],

    // Get horizontal padding/margin
    horizontal: (key: keyof Spacing) => ({
        paddingHorizontal: spacing[key],
    }),

    // Get vertical padding/margin
    vertical: (key: keyof Spacing) => ({
        paddingVertical: spacing[key],
    }),

    // Get all-around padding
    padding: (key: keyof Spacing) => ({
        padding: spacing[key],
    }),

    // Get all-around margin
    margin: (key: keyof Spacing) => ({
        margin: spacing[key],
    }),

    // Get shadow style
    shadow: (key: keyof typeof shadowElevation) => shadowElevation[key],

    // Get border radius
    radius: (key: keyof BorderRadius) => ({
        borderRadius: borderRadius[key],
    }),
};
