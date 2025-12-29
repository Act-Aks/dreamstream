/** biome-ignore-all lint/suspicious/noBitwiseOperators: Helper function */
import { kisskhKeyTables } from './kisskhKeyTables'

export interface KisskhKeyParams {
    /**
     * KissKH internal identifier for the resource.
     *
     * For videos this is usually the episode / video id you send to the
     * KissKH API in the `id` query parameter. For subtitles it is the same
     * id used by the corresponding subtitle request.
     */
    resourceId: string | number

    /**
     * Selects whether this key is generated for a subtitle (`'sub'`)
     * request or a video (`'vid'`) request.
     *
     * KissKH uses different GUIDs for video and subtitle flows; this flag
     * switches between them so the generated kisskh key matches what the
     * backend expects.
     */
    resourceType: 'subtitle' | 'video'

    /**
     * Short site‑specific hash string hard‑coded in KissKH’s client code.
     * It is embedded into the serialized payload and acts as part of the
     * shared secret between client and server.
     *
     * Do not change this unless the site updates their `common.js`.
     * Default taken from:
     * https://kisskh.co/common.js?v=9082123
     */
    clientHash?: string

    /**
     * API / app version string used by KissKH.
     *
     * This value is included in the payload to tie the key to a particular
     * client version. If the site bumps the version, this may need to be
     * updated to keep the key valid.
     */
    apiVersion?: string

    /**
     * Video GUID used when generating a key for video streams.
     *
     * This is a fixed identifier found in the original obfuscated code and
     * is only used when `resourceType === 'video'`.
     */
    videoGuid?: string

    /**
     * Subtitle GUID used when generating a key for subtitle streams.
     *
     * This is a fixed identifier found in the original obfuscated code and
     * is only used when `resourceType === 'subtitle'`.
     */
    subtitleGuid?: string

    /**
     * Encoded platform / build version string.
     *
     * Acts as another client fingerprint field in the payload. Normally
     * left at the default taken from the site’s JavaScript.
     */
    platformVersion?: string
}

/**
 * Generate the KissKH `kkey` token for video or subtitle requests.
 *
 * Steps:
 * 1. Build a payload array containing the resource id, client hash,
 *    API version, platform version, a video/subtitle GUID and several
 *    constant `"kisskh"` marker strings.
 * 2. Compute a simple 32‑bit hash of that payload string and insert it
 *    back into the array.
 * 3. Join with `|`, pad to 16 bytes, convert to 32‑bit words.
 * 4. Encrypt all blocks with a custom AES‑like cipher using
 *    `kisskhKeyTables` and CBC mode.
 * 5. Convert the encrypted words to an uppercase hex string.
 */
export function getKisskhKey({
    resourceId,
    resourceType,
    clientHash = 'mg3c3b04ba',
    apiVersion = '2.8.10',
    videoGuid = '62f176f3bb1b5b8e70e39932ad34a0c7',
    subtitleGuid = 'VgV52sWhwvBSf8BsM3BRY9weWiiCbtGp',
    platformVersion = '4830201',
}: KisskhKeyParams): string {
    const baseParts: (string | number | null)[] = [
        '',
        resourceId,
        null,
        clientHash,
        apiVersion,
        resourceType === 'subtitle' ? subtitleGuid : videoGuid,
        platformVersion,
        truncateTo48('kisskh'),
        truncateTo48('kisskh'.toLowerCase()),
        truncateTo48('kisskh'),
        'kisskh',
        'kisskh',
        'kisskh',
        '00',
        '',
    ]

    // Insert lightweight hash into the second position.
    baseParts.splice(1, 0, simpleStringHash(baseParts.join('|')))

    const payload = baseParts.join('|')
    const padded = pkcs7Pad16(payload)

    const [wordArray, originalLength] = stringToWordArray(padded)

    encryptAllBlocks(wordArray)

    return wordArrayToHex(Uint32Array.from(wordArray), originalLength).toUpperCase()
}

// === internal helpers ===

/**
 * Convert a UTF‑16 string into an array of 32‑bit words in big‑endian order.
 * Returns the word array and the original string length in characters.
 */
function stringToWordArray(input: string): [number[], number] {
    const words: number[] = []
    for (let i = 0; i < input.length; i++) {
        // @ts-expect-error Object is possibly 'undefined'
        words[i >>> 2] |= (input.charCodeAt(i) & 0xff) << (24 - (i % 4) * 8)
    }
    return [words, input.length]
}

/**
 * Convert a 32‑bit word array into a hex string, using only the first
 * `length` bytes (so padding bytes do not affect visible output length).
 */
function wordArrayToHex(words: Uint32Array, length: number): string {
    return Array.from({ length }, (_, i) =>
        // @ts-expect-error Object is possibly 'undefined'
        ((words[i >>> 2] >>> (24 - (i % 4) * 8)) & 0xff)
            .toString(16)
            .padStart(2, '0')
    ).join('')
}

/** Limit a string to at most 48 characters. */
const truncateTo48 = (s = '') => s.slice(0, 48)

/**
 * Lightweight 32‑bit hash function used by KissKH.
 * Not cryptographically secure; used only as a simple integer derivation.
 */
function simpleStringHash(input: string): number {
    return Array.from(input).reduce((hash, ch) => (hash << 5) - hash + ch.charCodeAt(0), 0)
}

/**
 * Apply PKCS#7‑style padding so the string length becomes a multiple
 * of 16 bytes.
 */
function pkcs7Pad16(input: string): string {
    const padLen = 16 - (input.length % 16)
    return input + String.fromCharCode(padLen).repeat(padLen)
}

/**
 * Encrypt every 16‑byte (4 word) block in CBC mode using the
 * custom AES‑like cipher implemented in `encryptBlock`.
 */
function encryptAllBlocks(words: number[]) {
    for (let offset = 0; offset < words.length; offset += 4) {
        encryptBlock(words, offset)
    }
}

/**
 * Encrypt a single 16‑byte block at `offset` in the `state` array.
 *
 * 128‑bit block, 10 rounds, CBC with fixed IV for first block.
 * Uses precomputed T‑boxes and S‑box from `kisskhKeyTables`.
 */
function encryptBlock(state: number[], offset: number) {
    const [roundKeySchedule, tBox0, tBox1, tBox2, tBox3, sBox] = kisskhKeyTables

    // CBC IV (first block) or previous ciphertext block
    const previousBlock =
        offset === 0 ? [22_039_283, 1_457_920_463, 776_125_350, -1_941_999_367] : state.slice(offset - 4, offset)

    // XOR with IV / previous block
    for (let i = 0; i < 4; i++) {
        // @ts-expect-error Object is possibly 'undefined'
        state[offset + i] ^= previousBlock[i]
    }

    const rounds = 10

    // @ts-expect-error Object is possibly 'undefined'
    let s0 = state[offset] ^ roundKeySchedule[0]
    // @ts-expect-error Object is possibly 'undefined'
    let s1 = state[offset + 1] ^ roundKeySchedule[1]
    // @ts-expect-error Object is possibly 'undefined'
    let s2 = state[offset + 2] ^ roundKeySchedule[2]
    // @ts-expect-error Object is possibly 'undefined'
    let s3 = state[offset + 3] ^ roundKeySchedule[3]

    let rkIndex = 4

    let t0: number
    let t1: number
    let t2: number

    // 9 main rounds using T‑boxes
    for (let round = 1; round < rounds; round++) {
        t0 =
            // @ts-expect-error Object is possibly 'undefined'
            tBox0[s0 >>> 24] ^
            // @ts-expect-error Object is possibly 'undefined'
            tBox1[(s1 >>> 16) & 0xff] ^
            // @ts-expect-error Object is possibly 'undefined'
            tBox2[(s2 >>> 8) & 0xff] ^
            // @ts-expect-error Object is possibly 'undefined'
            tBox3[s3 & 0xff] ^
            // @ts-expect-error Object is possibly 'undefined'
            roundKeySchedule[rkIndex++]

        t1 =
            // @ts-expect-error Object is possibly 'undefined'
            tBox0[s1 >>> 24] ^
            // @ts-expect-error Object is possibly 'undefined'
            tBox1[(s2 >>> 16) & 0xff] ^
            // @ts-expect-error Object is possibly 'undefined'
            tBox2[(s3 >>> 8) & 0xff] ^
            // @ts-expect-error Object is possibly 'undefined'
            tBox3[s0 & 0xff] ^
            // @ts-expect-error Object is possibly 'undefined'
            roundKeySchedule[rkIndex++]

        t2 =
            // @ts-expect-error Object is possibly 'undefined'
            tBox0[s2 >>> 24] ^
            // @ts-expect-error Object is possibly 'undefined'
            tBox1[(s3 >>> 16) & 0xff] ^
            // @ts-expect-error Object is possibly 'undefined'
            tBox2[(s0 >>> 8) & 0xff] ^
            // @ts-expect-error Object is possibly 'undefined'
            tBox3[s1 & 0xff] ^
            // @ts-expect-error Object is possibly 'undefined'
            roundKeySchedule[rkIndex++]

        s3 =
            // @ts-expect-error Object is possibly 'undefined'
            tBox0[s3 >>> 24] ^
            // @ts-expect-error Object is possibly 'undefined'
            tBox1[(s0 >>> 16) & 0xff] ^
            // @ts-expect-error Object is possibly 'undefined'
            tBox2[(s1 >>> 8) & 0xff] ^
            // @ts-expect-error Object is possibly 'undefined'
            tBox3[s2 & 0xff] ^
            // @ts-expect-error Object is possibly 'undefined'
            roundKeySchedule[rkIndex++]

        s0 = t0
        s1 = t1
        s2 = t2
    }

    // Final round: use S‑box directly instead of T‑boxes
    t0 =
        // @ts-expect-error Object is possibly 'undefined'
        ((sBox[s0 >>> 24] << 24) |
            // @ts-expect-error Object is possibly 'undefined'
            (sBox[(s1 >>> 16) & 0xff] << 16) |
            // @ts-expect-error Object is possibly 'undefined'
            (sBox[(s2 >>> 8) & 0xff] << 8) |
            // @ts-expect-error Object is possibly 'undefined'
            sBox[s3 & 0xff]) ^
        // @ts-expect-error Object is possibly 'undefined'
        roundKeySchedule[rkIndex++]

    t1 =
        // @ts-expect-error Object is possibly 'undefined'
        ((sBox[s1 >>> 24] << 24) |
            // @ts-expect-error Object is possibly 'undefined'
            (sBox[(s2 >>> 16) & 0xff] << 16) |
            // @ts-expect-error Object is possibly 'undefined'
            (sBox[(s3 >>> 8) & 0xff] << 8) |
            // @ts-expect-error Object is possibly 'undefined'
            sBox[s0 & 0xff]) ^
        // @ts-expect-error Object is possibly 'undefined'
        roundKeySchedule[rkIndex++]

    t2 =
        // @ts-expect-error Object is possibly 'undefined'
        ((sBox[s2 >>> 24] << 24) |
            // @ts-expect-error Object is possibly 'undefined'
            (sBox[(s3 >>> 16) & 0xff] << 16) |
            // @ts-expect-error Object is possibly 'undefined'
            (sBox[(s0 >>> 8) & 0xff] << 8) |
            // @ts-expect-error Object is possibly 'undefined'
            sBox[s1 & 0xff]) ^
        // @ts-expect-error Object is possibly 'undefined'
        roundKeySchedule[rkIndex++]

    s3 =
        // @ts-expect-error Object is possibly 'undefined'
        ((sBox[s3 >>> 24] << 24) |
            // @ts-expect-error Object is possibly 'undefined'
            (sBox[(s0 >>> 16) & 0xff] << 16) |
            // @ts-expect-error Object is possibly 'undefined'
            (sBox[(s1 >>> 8) & 0xff] << 8) |
            // @ts-expect-error Object is possibly 'undefined'
            sBox[s2 & 0xff]) ^
        // @ts-expect-error Object is possibly 'undefined'
        roundKeySchedule[rkIndex++]

    state[offset] = t0
    state[offset + 1] = t1
    state[offset + 2] = t2
    state[offset + 3] = s3
}
