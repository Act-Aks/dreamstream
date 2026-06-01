package com.dreamstream.core.domain.extensions

// ============================================================================
// List Transformation Utilities
// ============================================================================

/**
 * Chunks this list into lists of the given [size], returning an empty list if this list is empty.
 *
 * Unlike the standard [chunked], this function returns `emptyList()` instead of `listOf(emptyList())`
 * when the receiver is empty, which is often more convenient for handling edge cases.
 *
 * ### Examples
 * ```kotlin
 * listOf(1, 2, 3, 4, 5).chunkedOrEmpty(2)   // [,, ][1][2][3][4][5]
 * emptyList<Int>().chunkedOrEmpty(2)        // []
 * listOf(1).chunkedOrEmpty(2)               // [][1]
 * ```
 *
 * @param size The size of each chunk (must be positive)
 * @return A list of chunks, or an empty list if the receiver is empty
 */
fun <T> List<T>.chunkedOrEmpty(size: Int): List<List<T>> =
    if (isEmpty()) emptyList() else chunked(size)

/**
 * Returns a list containing only the first occurrence of each element, based on the key returned by [keySelector].
 *
 * Elements are processed in iteration order. The first element for each unique key is included,
 * and subsequent elements with the same key are excluded.
 *
 * ### Examples
 * ```kotlin
 * val users = listOf(
 *     Person("Alice", 25),
 *     Person("Bob", 30),
 *     Person("Alice", 35)
 * )
 * users.distinctByKey { it.name }
 * // [Person("Alice", 25), Person("Bob", 30)]
 *
 * listOf(1, 2, 2, 3, 3, 4).distinctByKey { it }
 * //[2][3][4][1]
 * ```
 *
 * @param keySelector Function to extract the comparison key from each element
 * @return A list of distinct elements based on their keys
 */
inline fun <T, K> List<T>.distinctByKey(keySelector: (T) -> K): List<T> {
    val seen = mutableSetOf<K>()
    return filter { seen.add(keySelector(it)) }
}

/**
 * Returns a view of this list between [fromIndex] (inclusive) and [toIndex] (exclusive),
 * with indices safely coerced to valid bounds.
 *
 * Unlike the standard [subList], this function never throws `IndexOutOfBoundsException`.
 * Instead, it coerces indices to valid ranges:
 * - [fromIndex] is coerced to at least `0`
 * - [toIndex] is coerced to at most `size`
 *
 * ### Examples
 * ```kotlin
 * listOf(1, 2, 3, 4, 5).safeSubList(1, 4)    //[3][4][2]
 * listOf(1, 2, 3).safeSubList(-1, 10)        //  (clamped to valid range)[2][3][1]
 * listOf(1, 2, 3).safeSubList(5, 10)         // [] (fromIndex > size)
 * ```
 *
 * @param fromIndex Start index (inclusive), coerced to `max(0, fromIndex)`
 * @param toIndex End index (exclusive), coerced to `min(size, toIndex)`
 * @return A sublist within the valid bounds
 */
fun <T> List<T>.safeSubList(fromIndex: Int, toIndex: Int): List<T> =
    subList(fromIndex.coerceAtLeast(0), toIndex.coerceAtMost(size))

/**
 * Returns the index of the first element matching the [predicate], or `null` if no element matches.
 *
 * Unlike the standard [indexOfFirst], which returns `-1` when no match is found,
 * this function returns `null`, which is often more convenient for nullable handling.
 *
 * ### Examples
 * ```kotlin
 * listOf(1, 2, 3, 4, 5).indexOfFirstOrNull { it > 3 }   // 3
 * listOf(1, 2, 3).indexOfFirstOrNull { it > 10 }        // null
 * listOf("a", "bb", "ccc").indexOfFirstOrNull { it.length == 2 }  // 1
 * ```
 *
 * @param predicate Function to test each element
 * @return The index of the first matching element, or `null` if none found
 */
inline fun <T> List<T>.indexOfFirstOrNull(predicate: (T) -> Boolean): Int? {
    val index = indexOfFirst(predicate)
    return if (index == -1) null else index
}
