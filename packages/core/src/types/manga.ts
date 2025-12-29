import type { MediaStatus } from '@/constants/media'
import type { Title, WithOtherFields } from './base'

type LanguageTitles = [lang: string][]

export interface ComicRes {
    containers: GetComicsComics[]
    hasNextPage: boolean
}

export interface GetComicsComics {
    category: string
    description: string
    download: string
    excerpt: string
    image: string
    mediafire: string
    mega: string
    readOnline: string
    size: string
    title: string
    ufile: string
    year: string
    zippyshare: string
}

export type MangaChapter = WithOtherFields<{
    id: string
    pages?: number
    releaseDate?: string
    title: string
    volume?: number
}>

export type MangaChapterPage = WithOtherFields<{
    img: string
    page: number
}>

export interface MangaInfo extends MangaResult {
    authors?: string[]
    chapters?: MangaChapter[]
    characters?: unknown[]
    genres?: string[]
    links?: string[]
    malId?: number | string
    recommendations?: MangaResult[]
}

export type MangaResult = WithOtherFields<{
    altTitles?: string | string[] | LanguageTitles
    description?: string | LanguageTitles | { [lang: string]: string }
    id: string
    image?: string
    releaseDate?: number | string
    status?: MediaStatus
    title: string | LanguageTitles | Title
}>
