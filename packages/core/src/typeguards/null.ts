export const isNotNullish = <T>(arg: T | null | undefined): arg is T => arg !== null && arg !== undefined
export const isNullish = (arg: unknown): arg is undefined | null => arg === null || arg === undefined
