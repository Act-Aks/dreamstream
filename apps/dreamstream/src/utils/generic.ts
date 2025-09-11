/**
 * Exhaustiveness check for unions
 * @example
 * type MyUnion = 'a' | 'b' | 'c';
 * const myValue: MyUnion = 'a';
 * if (myValue === 'a') {
 *     // Do something
 * }
 * exhaustivenessCheck(myValue);
 * @param _
 */
export function exhaustivenessCheck(_: never): never {
    throw new Error("Exhaustiveness check failed");
}
