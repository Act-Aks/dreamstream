export const mediaFormat = {
    COMIC: 'COMIC',
    MANGA: 'MANGA',
    MOVIE: 'MOVIE',
    MUSIC: 'MUSIC',
    NOVEL: 'NOVEL',
    ONA: 'ONA',
    ONE_SHOT: 'ONE_SHOT',
    OVA: 'OVA',
    PV: 'PV',
    SPECIAL: 'SPECIAL',
    TV: 'TV',
    TV_SHORT: 'TV_SHORT',
    TV_SPECIAL: 'TV_SPECIAL',
} as const
export type MediaFormat = (typeof mediaFormat)[keyof typeof mediaFormat]

export const mediaStatus = {
    CANCELLED: 'Cancelled',
    COMPLETED: 'Completed',
    HIATUS: 'Hiatus',
    NOT_YET_AIRED: 'Not yet aired',
    ONGOING: 'Ongoing',
    UNKNOWN: 'Unknown',
} as const
export type MediaStatus = (typeof mediaStatus)[keyof typeof mediaStatus]

export const watchListType = {
    COMPLETED: 'completed',
    DROPPED: 'dropped',
    NONE: 'none',
    ONHOLD: 'on-hold',
    PLAN_TO_WATCH: 'plan to watch',
    WATCHING: 'watching',
} as const
export type WatchListType = (typeof watchListType)[keyof typeof watchListType]

export const subOrDub = {
    BOTH: 'both',
    DUB: 'dub',
    SUB: 'sub',
} as const
export type SubOrDub = (typeof subOrDub)[keyof typeof subOrDub]

/**
 * Used **only** for movie or tvshow providers
 */
export const tvType = {
    ANIME: 'Anime',
    MOVIE: 'Movie',
    PEOPLE: 'People',
    TVSERIES: 'TV Series',
} as const
export type TvType = (typeof tvType)[keyof typeof tvType]
