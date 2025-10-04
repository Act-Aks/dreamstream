import {
    BaseProvider,
    type DetailsResult,
    type MediaType,
    type ProviderType,
    type SearchResult,
} from "@dreamstream/common/types";
import axios from "axios";

export class DailymotionProvider extends BaseProvider {
    static readonly id = "dailymotion";
    static readonly name = "Dailymotion";
    static readonly providerType: ProviderType = "other";
    static readonly supportedMediaTypes: MediaType[] = ["other"];
    static readonly description = "Dailymotion official video API";
    static readonly icon = "https://www.dailymotion.com/favicon.ico";

    readonly apiUrl = "https://api.dailymotion.com";

    override async search(query: string, _mediaType?: MediaType): Promise<SearchResult<"other">[]> {
        const url = `${this.apiUrl}/videos?fields=id,title,thumbnail_360_url&limit=10&search=${encodeURIComponent(query)}`;
        const { data } = await axios.get(url, {
            headers: { "User-Agent": "DreamStream/1.0" },
        });
        return data.list.map((item) => ({
            id: `https://www.dailymotion.com/video/${item.id}`,
            mediaType: "other",
            poster: item.thumbnail_360_url,
            title: item.title,
        }));
    }

    override async getDetails(id: string, _mediaType: MediaType): Promise<DetailsResult<"other">> {
        // id is a URL like https://www.dailymotion.com/video/xyz
        const videoId = id.replace("https://www.dailymotion.com/video/", "");
        const url = `${this.apiUrl}/video/${videoId}?fields=id,title,description,thumbnail_720_url`;
        const { data: entry } = await axios.get(url, {
            headers: { "User-Agent": "DreamStream/1.0" },
        });
        return {
            description: entry.description,
            id,
            mediaType: "other",
            poster: entry.thumbnail_720_url,
            title: entry.title,
        };
    }
}
