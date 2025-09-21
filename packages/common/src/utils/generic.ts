function isArray<T>(input: T | T[] | readonly T[]): input is T[] | readonly T[] {
    return Array.isArray(input);
}

export function ensureArray<T>(input: T | T[] | null | undefined): T[];
export function ensureArray<T>(input: T | readonly T[] | null | undefined): readonly T[];
export function ensureArray<T>(input: T | T[] | readonly T[] | null | undefined): T[] | readonly T[];
/**
 * Makes sure that value ends up being array if not already an array
 * @param input
 */
export function ensureArray<T>(input: T | T[] | readonly T[] | null | undefined): T[] | readonly T[] {
    if (input === null || input === undefined) {
        return [];
    }

    return isArray(input) ? input : [input];
}

/**
 * Exhaustiveness check for unions
 * @param _
 */
export function exhaustivenessCheck(_: never): never {
    throw new Error("Exhaustiveness check failed");
}

/**
 * Version of Object.keys that returns the expected key type
 * @param object
 * @returns typed array of keys or an empty array if the object was null or undefined
 */
export function keys<T>(object: T | null | undefined) {
    return (object ? Object.keys(object) : []) as (keyof T)[];
}

/**
 * Version of Object.entries that returns the expected key value pair type
 * @param object
 * @returns typed array of entries or an empty array if the object was null or undefined
 */
export function entries<T>(object: T | null | undefined) {
    return (object ? Object.entries(object) : []) as [keyof T, T[keyof T]][];
}

/**
 * Version of Object.values that returns the expected value type
 * @param object
 * @returns typed array of values or an empty array if the object was null or undefined
 */
export function values<T>(object: T | null | undefined) {
    return (object ? Object.values(object) : []) as T[keyof T][];
}

/**
 * Convert an array to a dictionary by mapping each item
 * into a dictionary key & value
 */
export function objectify<T, Key extends string | number | symbol, Value = T>(
    array: readonly T[],
    getKey: (item: T) => Key,
    getValue: (item: T) => Value = (item) => item as unknown as Value
): Record<Key, Value> {
    return array.reduce<Record<Key, Value>>(
        (acc, item) => {
            acc[getKey(item)] = getValue(item);
            return acc;
        },
        {} as Record<Key, Value>
    );
}

/**
 * Returns the string in lower case.
 * Each character of the string will be lowercased.
 *
 * @example
 * ```ts
 * toLowerCase('FOo') // returns foo
 * ```
 * @param value
 */
export function toLowerCase<T extends string>(value: T): Lowercase<T> {
    return value.toLowerCase() as Lowercase<T>;
}

/**
 * Returns the string in upper case.
 * Each character of the string will be uppercased.
 *
 * @example
 * ```ts
 * toUpperCase('foo') // returns FOO
 * toUpperCase('FOo') // returns FOO
 * ```
 * @param value
 */
export function toUpperCase<T extends string>(value: T): Uppercase<T> {
    return value.toUpperCase() as Uppercase<T>;
}

/**
 * Converts the first character of a given string to uppercase.
 *
 * @example
 * ```ts
 * capitalize('bar') // returns Bar
 * capitalize('bAR') // returns BAR
 * ```
 * @param value
 */
export function capitalize<T extends string>(value: T) {
    return (value.substring(0, 1).toUpperCase() + value.substring(1)) as Capitalize<T>;
}
