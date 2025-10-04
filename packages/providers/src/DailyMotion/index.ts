import { AxiosClient } from "@dreamstream/common/net";
import {
    BaseProvider,
    type DetailsResult,
    type MediaType,
    type ProviderType,
    type SearchResult,
    type StreamLink,
} from "@dreamstream/common/types";

type DailymotionVideoItem = {
    id: string;
    title: string;
    thumbnail_360_url: string;
};

type DailymotionSearchResponse = {
    list: DailymotionVideoItem[];
};

type DailymotionVideoDetails = {
    id: string;
    title: string;
    description: string;
    thumbnail_720_url: string;
};

const axiosClient = new AxiosClient();

export class DailymotionProvider extends BaseProvider {
    static override readonly id = "dailymotion";
    static override readonly name = "Dailymotion";
    static override readonly providerType: ProviderType = "other";
    static override readonly supportedMediaTypes: MediaType[] = ["other"];
    static override readonly description = "Dailymotion official video API";
    static override readonly icon = "https://www.dailymotion.com/favicon.ico";

    readonly apiUrl = "https://api.dailymotion.com";

    override async search(query: string, _mediaType?: MediaType): Promise<SearchResult<"other">[]> {
        const url = `${this.apiUrl}/videos?fields=id,title,thumbnail_360_url&limit=10&search=${encodeURIComponent(query)}`;
        const { data } = await axiosClient.get<DailymotionSearchResponse>(url);
        return data.list.map((item) => ({
            id: `https://www.dailymotion.com/video/${item.id}`,
            mediaType: "other",
            poster: item.thumbnail_360_url,
            title: item.title,
        }));
    }

    override async getDetails(id: string, _mediaType: MediaType): Promise<DetailsResult<"other">> {
        const videoId = id.replace("https://www.dailymotion.com/video/", "");
        const url = `${this.apiUrl}/video/${videoId}?fields=id,title,description,thumbnail_720_url`;
        const { data: entry } = await axiosClient.get<DailymotionVideoDetails>(url);
        return {
            description: entry.description,
            id,
            mediaType: "other",
            poster: entry.thumbnail_720_url,
            title: entry.title,
        };
    }

    override getStreams(id: string, _mediaType: MediaType): Promise<StreamLink[]> {
        const videoId = id.replace("https://www.dailymotion.com/video/", "");
        return Promise.resolve([
            {
                language: undefined,
                quality: undefined,
                subtitles: undefined,
                url: `https://www.dailymotion.com/embed/video/${videoId}`,
            },
        ]);
    }
}
