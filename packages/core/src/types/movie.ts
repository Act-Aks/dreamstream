import type { MediaStatus, TvType } from '@/constants/media'
import type { Title, Trailer, WithOtherFields } from './base'

export type MovieEpisode = WithOtherFields<{
    description?: string
    id: string
    image?: string
    number?: number
    releaseDate?: string
    season?: number
    title: string
    url?: string
}>

export interface MovieInfo extends MovieResult {
    casts?: string[]
    cover?: string
    description?: string
    duration?: string
    episodes?: MovieEpisode[]
    genres?: string[]
    production?: string
    rating?: number
    recommendations?: MovieResult[]
    seasons?: { season: number; image?: string; episodes: MovieEpisode[] }[]
    status?: MediaStatus
    tags?: string[]
    totalEpisodes?: number
    trailer?: Trailer
}

export type MovieResult = WithOtherFields<{
    id: string
    image?: string
    releaseDate?: string
    title: string | Title
    type?: TvType
    url?: string
}>

export type PeopleResult = WithOtherFields<{
    id: string
    image?: string
    movies: MovieResult[]
    name: string
    rating?: string
}>
