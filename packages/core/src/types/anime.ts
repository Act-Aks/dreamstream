import type { MediaFormat, MediaStatus } from '@/constants/media'
import type { Title, Trailer, WithOtherFields } from './base'

export interface AnimeEpisode {
    description?: string
    duration?: number
    id: string
    image?: string
    imageHash?: string
    isAdult?: boolean
    isDubbed: boolean
    isFiller?: boolean
    isHD?: boolean
    isSubbed: boolean
    number?: number
    releaseDate?: string
    seasonNumber?: number
    title: string
    url?: string
}

export interface AnimeInfo extends AnimeResult {
    color?: string
    /**
     * two letter representation of coutnry -> IN for India
     */
    countryOfOrigin?: string
    cover?: string
    description?: string
    endDate?: FuzzyDate
    episodes?: AnimeEpisode[]
    externalLinks?: ExternalLink[]
    genres?: string[]
    hasDub?: boolean
    hasSub?: boolean
    isAdult?: boolean
    isLicensed?: boolean
    malId?: number | string
    recommendations?: AnimeResult[]
    relations?: AnimeResult[]
    season?: Season
    startDate?: FuzzyDate
    status?: MediaStatus
    studios?: string[]
    synonyms?: string[]
    totalEpisodes?: number
    trailer?: Trailer
}

export type AnimeResult = WithOtherFields<{
    cover?: string
    coverHash?: string
    id: string
    image?: string
    imageHash?: string
    rating?: number
    relationType?: string
    releaseDate?: string
    status?: MediaStatus
    title: string | Title
    type?: MediaFormat
    url?: string
}>

export interface ExternalLink {
    id?: string
    sourceName?: string
    url?: string
}

export interface FuzzyDate {
    day?: number
    month?: number
    year?: number
}

export type Season = 'FALL' | 'WINTER' | 'SPRING' | 'SUMMER'
