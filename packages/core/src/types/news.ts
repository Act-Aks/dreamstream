import type { Topics } from '@/constants/genres'

interface News {
    /** id of the news */
    id: string
    /** thumbnail image URL of the news */
    thumbnail: string
    /** thumbnail image blurhash code of the news */
    thumbnailHash: string
    /** title of the news */
    title: string
    /** time at which the news was uploaded */
    uploadedAt: string
    /** URL of the news */
    url: string
}

export interface NewsFeed extends News {
    /** preview of the news feed */
    preview: NewsFeedPreview
    /** topics of the feed */
    topics: Topics[]
}

interface NewsFeedPreview {
    /** some contents of the feed */
    full: string
    /** intro of the feed */
    intro: string
}

export interface NewsInfo extends News {
    /** description of the news */
    description: string
    /** intro of the news */
    intro: string
}
