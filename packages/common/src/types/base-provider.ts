import type { DetailsResult, MediaType, ProviderType, SearchResult, StreamLink } from "./content";

export abstract class BaseProvider {
    /** Unique slug */
    static readonly id: string;
    /** Human-readable name */
    static readonly name: string;
    /** Provider description */
    static readonly description?: string;
    /** Provider favicon/logo URL */
    static readonly icon?: string;
    /** Main category of provider */
    static readonly providerType: ProviderType;
    /** Media types this provider supports */
    static readonly supportedMediaTypes: MediaType[];
    /** Search for content by query (anime, movie, etc.) */
    abstract search(query: string, mediaType?: MediaType): Promise<SearchResult[]>;
    /** Get content details (details, episodes, streams, etc.) */
    abstract getDetails(id: string, mediaType: MediaType): Promise<DetailsResult>;
    /** Get streams for a content item (episode/movie/channel, etc.) */
    abstract getStreams(id: string, mediaType: MediaType): Promise<StreamLink[]>;
}
