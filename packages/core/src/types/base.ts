export type EpisodeServer = WithOtherFields<{
    name: string
    url: string
}>

interface Image {
    extraLarge?: string
    large?: string
    medium?: string
}

/**
 * The start, and the end of the intro or opening in seconds.
 */
export interface Intro {
    end: number
    start: number
}

export interface ProviderStats {
    baseUrl: string
    classPath: string
    isNSFW: boolean
    isWorking: boolean
    lang: string[] | string
    logo: string
    name: string
}

export interface ProxyConfig {
    /**
     * X-API-Key header value (if any)
     */
    key?: string
    /**
     * The proxy rotation interval in milliseconds. (default: 5000)
     */
    rotateInterval?: number
    /**
     * The proxy URL
     * @example https://proxy.com
     */
    url: string | string[]
}

export interface Roles {
    color?: string
    id: string
    image: Image
    title: Title
    type?: string
}

export interface Search<Result> {
    currentPage?: number
    hasNextPage?: boolean
    results: Result[]
    totalPages?: number
    /**
     * `totalResults` must include results from all pages
     */
    totalResults?: number
}

export interface Source {
    download?: string | { url?: string; quality?: string }[]
    embedURL?: string
    headers?: Record<string, string>
    intro?: Intro
    outro?: Intro
    sources: Video[]
    subtitles?: Subtitle[]
}

export interface Staff {
    description?: string
    id: string
    image?: Image
    name: StaffName
    roles?: Roles[]
    siteUrl?: string
}

interface StaffName {
    first?: string
    full?: string
    last?: string
    native?: string
}

export interface Subtitle {
    /**
     * The id of the subtitle. **not** required
     */
    id?: string
    /**
     * The language of the subtitle
     */
    lang: string
    /**
     * The **url** that should take you to the subtitle **directly**.
     */
    url: string
}

export interface Title {
    english?: string
    native?: string
    romaji?: string
    userPreferred?: string
}

export interface Trailer {
    id: string
    site?: string
    thumbnail?: string
    thumbnailHash?: string | null
    url?: string
}

export type Video = WithOtherFields<{
    /**
     * set this to `true` if the video is dash (mpd)
     */
    isDASH?: boolean
    /**
     * make sure to set this to `true` if the video is hls
     */
    isM3U8?: boolean
    /**
     * The Quality of the video should include the `p` suffix
     */
    quality?: string
    /**
     * size of the video in **bytes**
     */
    size?: number
    /**
     * The **MAIN URL** of the video provider that should take you to the video
     */
    url: string
}>

export type WithOtherFields<T> = T & Record<string, unknown>
