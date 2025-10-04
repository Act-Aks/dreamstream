export type ProviderType = "anime" | "movie" | "series" | "live_tv" | "sports" | "other";
export type MediaType = "anime" | "movie" | "series" | "tv_channel" | "sports_event" | "other";
export type SearchResult<T extends MediaType = MediaType> = {
    id: string;
    title: string;
    poster: string;
    description?: string;
    year?: number;
    mediaType: T;
    extra?: unknown;
};
export type DetailsResult<T extends MediaType = MediaType> = {
    id: string;
    title: string;
    description?: string;
    poster: string;
    year?: number;
    mediaType: T;
    episodes?: SeriesEpisode[];
    streamLinks?: StreamLink[];
    extra?: unknown;
};
export type SeriesEpisode = {
    id: string;
    title: string;
    episodeNumber?: number;
    seasonNumber?: number;
    url: string;
};
export type StreamLink = {
    url: string;
    quality?: string | undefined;
    language?: string | undefined;
    subtitles?: SubtitleTrack[] | undefined;
};
export type SubtitleTrack = {
    url: string;
    lang: string;
};
