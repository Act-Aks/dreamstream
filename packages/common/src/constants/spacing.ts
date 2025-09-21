export const spacing = {
    "2xl": 48,
    "3xl": 64,
    "4xl": 96,
    base: 16,
    lg: 24,
    md: 12,
    sm: 8,
    xl: 32,
    xs: 4,
} as const;

export const borderRadius = {
    base: 16,
    full: 9999,
    lg: 12,
    md: 8,
    none: 0,
    sm: 4,
    xl: 20,
} as const;

type Elevation = {
    shadowOpacity: number;
    shadowRadius: number;
    shadowOffset: { width: number; height: number };
    elevation: number;
};
export type Shadow = Record<"none" | "sm" | "md" | "base" | "lg" | "xl" | "2xl", Elevation>;

export const shadow: Shadow = {
    "2xl": {
        elevation: 15,
        shadowOffset: { height: 12, width: 0 },
        shadowOpacity: 0.35,
        shadowRadius: 20,
    },
    base: {
        elevation: 3,
        shadowOffset: { height: 2, width: 0 },
        shadowOpacity: 0.2,
        shadowRadius: 5,
    },
    lg: {
        elevation: 6,
        shadowOffset: { height: 4, width: 0 },
        shadowOpacity: 0.25,
        shadowRadius: 9,
    },
    md: {
        elevation: 3,
        shadowOffset: { height: 2, width: 0 },
        shadowOpacity: 0.15,
        shadowRadius: 3,
    },
    none: {
        elevation: 0,
        shadowOffset: { height: 0, width: 0 },
        shadowOpacity: 0,
        shadowRadius: 0,
    },
    sm: {
        elevation: 1,
        shadowOffset: { height: 1, width: 0 },
        shadowOpacity: 0.1,
        shadowRadius: 1,
    },
    xl: {
        elevation: 10,
        shadowOffset: { height: 8, width: 0 },
        shadowOpacity: 0.3,
        shadowRadius: 14,
    },
};

export const animationDuration = {
    fast: 200,
    fastest: 100,
    normal: 300,
    slow: 500,
    slowest: 800,
} as const;

export const zIndex = {
    base: 0,
    behind: -1,
    loading: 1500,
    max: 9999,
    modal: 1100,
    overlay: 1000,
    popover: 1200,
    toast: 1400,
    tooltip: 1300,
} as const;

export const opacity = {
    backdrop: 0.8,
    disabled: 0.4,
    high: 0.75,
    loading: 0.6,
    low: 0.25,
    medium: 0.5,
    opaque: 1,
    overlay: 0.6,
    pressed: 0.7,
    transparent: 0,
} as const;

export type AnimationDuration = typeof animationDuration;
export type BorderRadius = typeof borderRadius;
export type Opacity = typeof opacity;
export type Spacing = typeof spacing;
export type Zindex = typeof zIndex;

export const spacingPresets = {
    get: (key: keyof Spacing) => spacing[key],
    horizontal: (key: keyof Spacing) => ({ paddingHorizontal: spacing[key] }),
    margin: (key: keyof Spacing) => ({ margin: spacing[key] }),
    padding: (key: keyof Spacing) => ({ padding: spacing[key] }),
    radius: (key: keyof BorderRadius) => ({ borderRadius: borderRadius[key] }),
    shadow: (key: keyof Shadow) => shadow[key],
    vertical: (key: keyof Spacing) => ({ paddingVertical: spacing[key] }),
};
