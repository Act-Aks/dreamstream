import type { AnimeInfo } from '@/types/anime'
import type { EpisodeServer, Source } from '@/types/base'
import { BaseParser } from './baseParser'

export abstract class AnimeParser extends BaseParser {
    /**
     * If the provider has dub and it's avialable seperatly from sub set this to `true`
     */
    protected readonly isDubAvailableSeparately: boolean = false
    /**
     * Takes anime id -> returns anime info (including episodes)
     */
    abstract fetchAnimeInfo(animeId: string, ...args: unknown[]): Promise<AnimeInfo>
    /**
     * Takes episode id -> returns episode sources (video links)
     */
    abstract fetchEpisodeSources(episodeId: string, ...args: unknown[]): Promise<Source>
    /**
     * Takes episode id -> returns episode servers (video links) available
     */
    abstract fetchEpisodeServers(episodeId: string, ...args: unknown[]): Promise<EpisodeServer[]>
}
