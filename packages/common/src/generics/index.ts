/** biome-ignore-all lint/style/useUnifiedTypeSignatures: This is created for checking various condition */

/**
 * Function to assert conditions
 * @param condition
 * @param msg
 */
export function assertCondition(condition: unknown, msg?: string): asserts condition;
export function assertCondition(condition: unknown, errorFactory?: () => Error): asserts condition;
export function assertCondition(condition: unknown, msgOrErrorFactory?: string | (() => Error)): asserts condition {
    if (!condition) {
        if (typeof msgOrErrorFactory === "string" || msgOrErrorFactory === undefined) {
            throw new Error(msgOrErrorFactory);
        }
        throw msgOrErrorFactory();
    }
}

// biome-ignore lint/complexity/noBannedTypes: This is used to define a generic function type
export const isFunction = (value: unknown): value is Function => !!(value && typeof value === "function");

/**
 * This is really a _best guess_ promise checking.
 * You should probably use Promise.resolve(value) to be 100%
 * sure you're handling it correctly.
 */
export const isPromise = (value: unknown): value is Promise<unknown> => {
    if (!value || typeof value !== "object") {
        return false;
    }
    if (!("then" in value)) {
        return false;
    }
    return isFunction(value.then);
};

/**
 * A helper to try an async function without forking
 * the control flow. Returns an error first callback _like_
 * array response as [Error, result]
 */
export const tryit =
    <Args extends unknown[], Return>(func: (...args: Args) => Return) =>
    (
        ...args: Args
    ): Return extends Promise<unknown>
        ? Promise<[Error, undefined] | [undefined, Awaited<Return>]>
        : [Error, undefined] | [undefined, Return] => {
        try {
            const result = func(...args);
            if (isPromise(result)) {
                return result
                    .then((value) => [undefined, value])
                    .catch((err) => [err, undefined]) as Return extends Promise<unknown>
                    ? Promise<[Error, undefined] | [undefined, Awaited<Return>]>
                    : [Error, undefined] | [undefined, Return];
            }
            return [undefined, result] as Return extends Promise<unknown>
                ? Promise<[Error, undefined] | [undefined, Awaited<Return>]>
                : [Error, undefined] | [undefined, Return];
        } catch (err) {
            return [err as unknown, undefined] as Return extends Promise<unknown>
                ? Promise<[Error, undefined] | [undefined, Awaited<Return>]>
                : [Error, undefined] | [undefined, Return];
        }
    };

/**
 * Function to get an element from the array, by default returns the first element
 * @param array
 * @param index
 */
export function getArrayElement<T>(array: readonly T[], index = 0) {
    assertCondition(array[index] !== undefined);
    return array[index];
}

/**
 * Returns an array of key-value pairs from an object with proper TypeScript typing.
 * This is a typed version of Object.entries() that maintains the correct types for keys and values.
 *
 * @template T - The type of the input object
 * @param {T} obj - The object to get entries from
 * @returns {[keyof T, T[keyof T]][]} An array of tuples containing the object's keys and values with proper typing
 *
 * @example
 * const obj = { name: 'John', age: 30 };
 * const pairs = entries(obj); // [['name', 'John'], ['age', 30]] with proper typing
 */
export function entries<T extends object>(obj: T): [keyof T, T[keyof T]][] {
    return Object.entries(obj) as [keyof T, T[keyof T]][];
}

/**
 * Returns an array of typed keys from an object.
 * This is a typed version of Object.keys() that maintains the correct key types.
 *
 * @template T - The type of the input object
 * @param {T} obj - The object to get keys from
 * @returns {(keyof T)[]} An array of the object's keys with proper typing
 *
 * @example
 * const obj = { name: 'John', age: 30 };
 * const objKeys = keys(obj); // ['name', 'age'] with proper typing
 */
export function keys<T extends object>(obj: T): (keyof T)[] {
    return Object.keys(obj) as (keyof T)[];
}

/**
 * Returns an array of typed values from an object.
 * This is a typed version of Object.values() that maintains the correct value types.
 *
 * @template T - The type of the input object
 * @param {T} obj - The object to get values from
 * @returns {T[keyof T][]} An array of the object's values with proper typing
 *
 * @example
 * const obj = { name: 'John', age: 30 };
 * const objValues = values(obj); // ['John', 30] with proper typing
 */
export function values<T extends object>(obj: T): T[keyof T][] {
    return Object.values(obj) as T[keyof T][];
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
export function toLowerCase<S extends string>(value: S): Lowercase<S> {
    return value.toLowerCase() as Lowercase<S>;
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
export function toUpperCase<S extends string>(value: S): Uppercase<S> {
    return value.toUpperCase() as Uppercase<S>;
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
export function capitalize<S extends string>(value: S) {
    return (value.substring(0, 1).toUpperCase() + value.substring(1)) as Capitalize<S>;
}

/**
 * Converts the first character of a given string to lowercase.
 *
 * @example
 * ```ts
 * uncapitalize('Bar') // returns bar
 * uncapitalize('BAR') // returns bAR
 *
 * ```
 * @param value
 */
export function uncapitalize<T extends string>(value: T): Uncapitalize<T> {
    return (value.substring(0, 1).toLowerCase() + value.substring(1)) as Uncapitalize<T>;
}

type Delimiters = ["_", "-", " ", "."];
type StringCase = "CamelCase" | "PascalCase" | "SnakeCase" | "TitleCase";
type ToCamelCase<
    Head extends string,
    Tail extends string,
    Delimiter extends string,
> = `${Head}${ProcessDelimiter<Capitalize<Tail>, Delimiter, "CamelCase">}`;
type ToPascalCase<
    Head extends string,
    Tail extends string,
    Delimiter extends string,
> = `${Capitalize<Head>}${ProcessDelimiter<Capitalize<Tail>, Delimiter, "PascalCase">}`;
type ToSnakeCase<
    Head extends string,
    Tail extends string,
    Delimiter extends string,
> = `${Uncapitalize<Head>}${Head extends "" ? "" : "_"}${ProcessDelimiter<Uncapitalize<Tail>, Delimiter, "SnakeCase">}`;
type ToTitleCase<
    Head extends string,
    Tail extends string,
    Delimiter extends string,
> = `${Capitalize<Head>}${Head extends "" ? "" : " "}${ProcessDelimiter<Capitalize<Tail>, Delimiter, "TitleCase">}`;

type ProcessDelimiter<
    S extends string,
    D extends string,
    C extends StringCase,
> = S extends `${infer Head}${D}${infer Tail}`
    ? C extends "CamelCase"
        ? ToCamelCase<Head, Tail, D>
        : C extends "PascalCase"
          ? ToPascalCase<Head, Tail, D>
          : C extends "SnakeCase"
            ? ToSnakeCase<Head, Tail, D>
            : C extends "TitleCase"
              ? ToTitleCase<Head, Tail, D>
              : S
    : S;

type ProcessDelimiters<
    S extends string,
    DelimiterList extends readonly string[],
    ToCase extends StringCase,
> = DelimiterList extends readonly [infer First extends string, ...infer Rest extends readonly string[]]
    ? ProcessDelimiters<ProcessDelimiter<S, First, ToCase>, Rest, ToCase>
    : S;

export type CamelCase<S extends string> = ProcessDelimiters<S, Delimiters, "CamelCase">;
export type PascalCase<S extends string> = ProcessDelimiters<S, Delimiters, "PascalCase">;
export type SnakeCase<S extends string> = ProcessDelimiters<S, Delimiters, "SnakeCase">;
export type TitleCase<S extends string> = Capitalize<Lowercase<ProcessDelimiters<S, Delimiters, "TitleCase">>>;

const camelCaseRegex = /[-_\s]([a-z])|^./g;

/**
 * Formats the given string in camel case fashion
 *
 * camel('hello world') -> 'helloWorld'
 * camel('aks in-ROOM') -> 'AksInRoom'
 * camel('helloWorld') -> 'helloWorld'
 */
export function toCamelCase<S extends string>(value: S): CamelCase<S> {
    return value.toLowerCase().replace(camelCaseRegex, (m, c) => (c ? toUpperCase(c) : toUpperCase(m))) as CamelCase<S>;
}

/**
 * Formats the given string in title case fashion
 *
 * camel('hello world')   -> 'Hello world'
 * camel('va va-VOOM') -> 'Va va voom'
 * camel('helloWorld') -> 'HelloWorld'
 */
export function toTitleCase<S extends string>(value: S): TitleCase<S> {
    return value.toLowerCase().replace(camelCaseRegex, (m, c) => (c ? capitalize(c) : capitalize(m))) as TitleCase<S>;
}
