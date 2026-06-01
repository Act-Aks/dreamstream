package com.dreamstream.core.domain.extensions

// ============================================================================
// String Transformation Utilities
// ============================================================================

/**
 * Converts this string into a URL-friendly slug.
 *
 * Transformations applied:
 * 1. Converts to lowercase
 * 2. Removes all characters except lowercase letters, digits, spaces, and hyphens
 * 3. Replaces one or more whitespace characters with a single hyphen
 * 4. Trims leading/trailing hyphens
 *
 * ### Examples
 * ```kotlin
 * "Hello World!".toSlug()           // "hello-world"
 * "Dream Stream 2024".toSlug()      // "dream-stream-2024"
 * "Special @#$ Characters".toSlug() // "special-characters"
 * ```
 *
 * @return A normalized slug suitable for URLs, identifiers, or file names
 */
fun String.toSlug(): String =
    this.lowercase()
        .replace(Regex("[^a-z0-9\\s-]"), "")
        .replace(Regex("\\s+"), "-")
        .trim('-')

/**
 * Capitalizes the first letter of each word in this string.
 *
 * Words are separated by spaces. Only the first character of each word is uppercase;
 * the rest remain unchanged.
 *
 * ### Examples
 * ```kotlin
 * "hello world".capitalizeWords()           // "Hello World"
 * "the quick brown fox".capitalizeWords()  // "The Quick Brown Fox"
 * "already Capitalized".capitalizeWords()  // "Already Capitalized"
 * ```
 *
 * @return A string with each word's first letter capitalized
 */
fun String.capitalizeWords(): String = split(" ").joinToString(" ") { word ->
    word.replaceFirstChar { if (it.isLowerCase()) it.uppercaseChar() else it }
}

/**
 * Truncates this string to fit within [maxLength] characters, appending [ellipsis] if truncated.
 *
 * The total length of the returned string (including the ellipsis) will not exceed [maxLength].
 * If this string is already within the limit, it is returned unchanged.
 *
 * ### Examples
 * ```kotlin
 * "Hello World".truncate(8)                    // "Hello..."
 * "Hi".truncate(8)                             // "Hi"
 * "Exact".truncate(6)                          // "Exact"
 * "Short".truncate(10, ellipsis = "[...]")     // "Short"
 * ```
 *
 * @param maxLength The maximum allowed length of the returned string (including ellipsis)
 * @param ellipsis The suffix to append when truncating (default: "...")
 * @return The truncated string, or the original if no truncation is needed
 */
fun String.truncate(maxLength: Int, ellipsis: String = "..."): String =
    if (length <= maxLength) this
    else take(maxLength - ellipsis.length) + ellipsis

// ============================================================================
// URL & Domain Utilities
// ============================================================================

/**
 * Checks if this string is a valid HTTP or HTTPS URL.
 *
 * This is a simple check that only verifies the string starts with `http://` or `https://`.
 * It does not validate the full URL structure, domain, or path.
 *
 * ### Examples
 * ```kotlin
 * "https://example.com".isUrl()     // true
 * "http://localhost:8080".isUrl()   // true
 * "ftp://files.com".isUrl()         // false
 * "not-a-url".isUrl()               // false
 * ```
 *
 * @return `true` if the string starts with `http://` or `https://`; `false` otherwise
 */
fun String.isUrl(): Boolean = startsWith("http://") || startsWith("https://")

/**
 * Extracts the domain name from this URL string.
 *
 * Steps performed:
 * 1. Removes `https://` or `http://` protocol prefix
 * 2. Extracts everything before the first `/` (path)
 * 3. Extracts everything before the first `?` (query parameters)
 *
 * ### Examples
 * ```kotlin
 * "https://example.com/path".extractDomain()        // "example.com"
 * "http://sub.domain.com?query=1".extractDomain()   // "sub.domain.com"
 * "not-a-url".extractDomain()                        // "not-a-url"
 * ```
 *
 * @return The extracted domain name, or `null` if extraction fails
 */
fun String.extractDomain(): String? = runCatching {
    val withoutProtocol = removePrefix("https://").removePrefix("http://")
    withoutProtocol.substringBefore("/").substringBefore("?")
}.getOrNull()

// ============================================================================
// Base64 Encoding/Decoding Utilities
// ============================================================================

/**
 * Standard Base64 character set used for encoding and decoding.
 *
 * Contains 64 characters: A-Z, a-z, 0-9, +, /
 * @see toBase64String
 * @see fromBase64
 */
private const val BASE64_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

/**
 * Encodes this string into a Base64 representation.
 *
 * Uses UTF-8 encoding to convert the string to bytes before Base64 encoding.
 *
 * ### Examples
 * ```kotlin
 * "Hello".toBase64()           // "SGVsbG8="
 * "".toBase64()                // ""
 * "😀".toBase64()              // "8J+Yhg=="
 * ```
 *
 * @return The Base64-encoded string
 * @see fromBase64 for decoding
 */
fun String.toBase64(): String = encodeToByteArray().toBase64String()

/**
 * Encodes this byte array into a Base64 string representation.
 *
 * Implements the standard Base64 encoding algorithm with `=` padding for alignment.
 * Uses the standard Base64 character set: A-Z, a-z, 0-9, +, /.
 *
 * ### Examples
 * ```kotlin
 * "Hello".encodeToByteArray().toBase64String()  // "SGVsbG8="
 * byteArrayOf().toBase64String()                // ""
 * ```
 *
 * @return The Base64-encoded string
 * @see fromBase64 for decoding
 */
fun ByteArray.toBase64String(): String {
    val result = StringBuilder()
    var i = 0
    while (i < size) {
        val b0 = get(i++).toInt() and 0xFF
        val b1 = if (i < size) get(i++).toInt() and 0xFF else 0
        val b2 = if (i < size) get(i++).toInt() and 0xFF else 0
        result.append(BASE64_CHARS[(b0 shr 2) and 0x3F])
        result.append(BASE64_CHARS[((b0 shl 4) or (b1 shr 4)) and 0x3F])
        result.append(BASE64_CHARS[((b1 shl 2) or (b2 shr 6)) and 0x3F])
        result.append(BASE64_CHARS[b2 and 0x3F])
    }
    val padding = (3 - size % 3) % 3
    repeat(padding) { result[result.length - 1 - it] = '=' }
    return result.toString()
}

/**
 * Decodes a Base64-encoded string into its original byte array representation.
 *
 * Supports standard Base64 characters (A-Z, a-z, 0-9, +, /) and padding with `=`.
 *
 * ### Examples
 * ```kotlin
 * "SGVsbG8=".fromBase64()      // byte array for "Hello"
 * "======".fromBase64()        // empty byte array (invalid but handled)
 * ```
 *
 * @return The decoded byte array
 * @see toBase64 for encoding
 */
fun String.fromBase64(): ByteArray {
    val cleaned = replace("=", "")
    val bytes = mutableListOf<Byte>()
    var i = 0
    while (i < cleaned.length) {
        val b0 = BASE64_CHARS.indexOf(cleaned[i++])
        val b1 = if (i < cleaned.length) BASE64_CHARS.indexOf(cleaned[i++]) else 0
        val b2 = if (i < cleaned.length) BASE64_CHARS.indexOf(cleaned[i++]) else 0
        val b3 = if (i < cleaned.length) BASE64_CHARS.indexOf(cleaned[i++]) else 0
        bytes.add(((b0 shl 2) or (b1 shr 4)).toByte())
        if (b2 != 0) bytes.add(((b1 shl 4) or (b2 shr 2)).toByte())
        if (b3 != 0) bytes.add(((b2 shl 6) or b3).toByte())
    }
    return bytes.toByteArray()
}
