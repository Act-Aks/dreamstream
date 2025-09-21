export const fontSize = {
    "2xl": 24,
    "3xl": 30,
    "4xl": 36,
    "5xl": 48,
    "6xl": 60,
    base: 16,
    lg: 18,
    sm: 14,
    xl: 20,
    xs: 12,
} as const;

export const lineHeight = {
    "2xl": 32,
    "3xl": 36,
    "4xl": 40,
    "5xl": 1,
    "6xl": 1,
    base: 24,
    lg: 28,
    sm: 20,
    xl: 28,
    xs: 16,
} as const;

export const fontWeight = {
    bold: "700",
    medium: "500",
    normal: "400",
    semiBold: "600",
} as const;

export const fontFamily = {
    bold: "System",
    medium: "System",
    mono: "Courier New", // For code/debug display
    regular: "System",
    semiBold: "System",
} as const;

// Text variants for semantic usage
export const textVariants = {
    body: {
        fontFamily: fontFamily.regular,
        fontSize: fontSize.base,
        fontWeight: fontWeight.normal,
        lineHeight: lineHeight.base,
    },
    bodyLarge: {
        fontFamily: fontFamily.regular,
        fontSize: fontSize.lg,
        fontWeight: fontWeight.normal,
        lineHeight: lineHeight.lg,
    },
    button: {
        fontFamily: fontFamily.semiBold,
        fontSize: fontSize.base,
        fontWeight: fontWeight.semiBold,
        lineHeight: lineHeight.base,
    },
    caption: {
        fontFamily: fontFamily.regular,
        fontSize: fontSize.sm,
        fontWeight: fontWeight.normal,
        lineHeight: lineHeight.sm,
    },
    display: {
        fontFamily: fontFamily.bold,
        fontSize: fontSize["4xl"],
        fontWeight: fontWeight.bold,
        lineHeight: lineHeight["4xl"],
    },
    heading1: {
        fontFamily: fontFamily.bold,
        fontSize: fontSize["3xl"],
        fontWeight: fontWeight.bold,
        lineHeight: lineHeight["3xl"],
    },
    heading2: {
        fontFamily: fontFamily.semiBold,
        fontSize: fontSize["2xl"],
        fontWeight: fontWeight.semiBold,
        lineHeight: lineHeight["2xl"],
    },
    heading3: {
        fontFamily: fontFamily.semiBold,
        fontSize: fontSize.xl,
        fontWeight: fontWeight.semiBold,
        lineHeight: lineHeight.xl,
    },
    label: {
        fontFamily: fontFamily.medium,
        fontSize: fontSize.sm,
        fontWeight: fontWeight.medium,
        lineHeight: lineHeight.sm,
    },
    overline: {
        fontFamily: fontFamily.medium,
        fontSize: fontSize.xs,
        fontWeight: fontWeight.medium,
        letterSpacing: 1.5,
        lineHeight: lineHeight.xs,
        textTransform: "uppercase" as const,
    },
} as const;

export const typography = {
    fontFamily,
    fontSize,
    fontWeight,
    lineHeight,
    variants: textVariants,
} as const;

export type FontSize = typeof fontSize;
export type LineHeight = typeof lineHeight;
export type FontWeight = typeof fontWeight;
export type FontFamily = typeof fontFamily;
export type TextVariants = typeof textVariants;
export type Typography = typeof typography;
