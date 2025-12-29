import type { MediaStatus } from '@/constants/media'
import type { Title, WithOtherFields } from './base'

export interface LightNovelChapter {
    id: string
    title: string
    url?: string
    volume?: number | string
}

export interface LightNovelChapterContent {
    chapterTitle: string
    novelTitle: string
    text: string
}

export interface LightNovelInfo extends LightNovelResult {
    authors?: string[]
    chapters?: LightNovelChapter[]
    description?: string
    genres?: string[]
    rating?: number
    status?: MediaStatus
    views?: number
}

export type LightNovelResult = WithOtherFields<{
    id: string
    image?: string
    title: string | Title
    url: string
}>
