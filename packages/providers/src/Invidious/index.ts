import {
    BaseProvider,
    type DetailsResult,
    type MediaType,
    type SearchResult,
    type SeriesEpisode,
    type StreamLink,
} from "@dreamstream/common/types";
import axios from "axios";
import { load } from "cheerio";

export class AnimeOnProvider extends BaseProvider {
    static readonly id = "animeon";
    static readonly name = "AnimeOn";
    static readonly providerType = "anime";
    static readonly supportedMediaTypes: MediaType[] = ["anime"];
    static readonly description = "AnimeOn anime streaming";
    static readonly icon = "https://animeon.club/favicon.ico";

    override async search(query: string, _mediaType?: MediaType): Promise<SearchResult<"anime">[]> {
        const url = `https://animeon.club/search?q=${encodeURIComponent(query)}`;
        const { data } = await axios.get(url, { headers: { "User-Agent": "DreamStream/1.0" } });
        const $ = load(data);

        const results: SearchResult<"anime">[] = [];
        $(".search-card").each((_, el) => {
            results.push({
                description: $(el).find(".search-description").text().trim() ?? "",
                id: $(el).find("a").attr("href") ?? "",
                mediaType: "anime",
                poster: $(el).find("img").attr("src") ?? "",
                title: $(el).find(".search-title").text().trim(),
            });
        });

        return results;
    }

    override async getDetails(id: string, _mediaType: MediaType): Promise<DetailsResult<"anime">> {
        const url = id;
        const { data } = await axios.get(url, { headers: { "User-Agent": "DreamStream/1.0" } });
        const $ = load(data);

        const episodes: SeriesEpisode[] = [];
        $(".episode-item").each((_, el) => {
            episodes.push({
                episodeNumber: Number.parseInt($(el).find(".episode-number").text().trim(), 10),
                id: $(el).find("a").attr("href") ?? "",
                title: $(el).find(".episode-title").text().trim(),
                url: $(el).find("a").attr("href") ?? "",
            });
        });

        return {
            description: $(".anime-description").text().trim(),
            episodes,
            id,
            mediaType: "anime",
            poster: $(".anime-poster img").attr("src") ?? "",
            title: $("h1.anime-title").text().trim(),
        };
    }

    override async getStreams(id: string, _mediaType: MediaType): Promise<StreamLink[]> {
        const url = id;
        const { data } = await axios.get(url, { headers: { "User-Agent": "DreamStream/1.0" } });
        const $ = load(data);

        const links: StreamLink[] = $(".video-player source")
            .map((_, el) => ({
                language: undefined,
                quality: $(el).attr("data-quality"),
                subtitles: undefined,
                url: $(el).attr("src") ?? "",
            }))
            .get();

        return links;
    }
}
