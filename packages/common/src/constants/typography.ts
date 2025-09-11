export const fontSize = {
    xs: 12,
    sm: 14,
    base: 16,
    lg: 18,
    xl: 20,
    "2xl": 24,
    "3xl": 30,
    "4xl": 36,
    "5xl": 48,
    "6xl": 60,
} as const;

export const lineHeight = {
    xs: 16,
    sm: 20,
    base: 24,
    lg: 28,
    xl: 28,
    "2xl": 32,
    "3xl": 36,
    "4xl": 40,
    "5xl": 1,
    "6xl": 1,
} as const;

export const fontWeight = {
    normal: "400",
    medium: "500",
    semiBold: "600",
    bold: "700",
} as const;

export const fontFamily = {
    regular: "System",
    medium: "System",
    semiBold: "System",
    bold: "System",
} as const;

export const typography = {
    fontSize,
    lineHeight,
    fontWeight,
    fontFamily,
} as const;

export type FontSize = typeof fontSize;
export type LineHeight = typeof lineHeight;
export type FontWeight = typeof fontWeight;
export type FontFamily = typeof fontFamily;
export type Typography = typeof typography;
