import { AxiosClient } from "@dreamstream/common/net";
import {
    BaseProvider,
    type DetailsResult,
    type MediaType,
    type SearchResult,
    type StreamLink,
} from "@dreamstream/common/types";

type InvidiousSearchResult = {
    videoId: string;
    title: string;
};

type InvidiousVideoDetails = {
    videoId: string;
    title: string;
    description: string;
};

const axiosClient = new AxiosClient();

export class InvidiousProvider extends BaseProvider {
    static override readonly id = "invidious";
    static override readonly name = "Invidious (YouTube)";
    static override readonly providerType = "other";
    static override readonly supportedMediaTypes: MediaType[] = ["other"];
    static override readonly description = "YouTube videos via Invidious privacy-friendly API";
    static override readonly icon = "https://iv.ggtyler.dev/favicon.ico";

    readonly mainUrl = "https://iv.ggtyler.dev";

    override async search(query: string, _mediaType?: MediaType): Promise<SearchResult<"other">[]> {
        const url = `${this.mainUrl}/api/v1/search?q=${encodeURIComponent(query)}&page=1&type=video&fields=videoId,title`;
        const { data } = await axiosClient.get<InvidiousSearchResult[]>(url);
        return data.map((entry) => ({
            id: entry.videoId,
            mediaType: "other",
            poster: `${this.mainUrl}/vi/${entry.videoId}/mqdefault.jpg`,
            title: entry.title,
        }));
    }

    override async getDetails(id: string, _mediaType: MediaType): Promise<DetailsResult<"other">> {
        const url = `${this.mainUrl}/api/v1/videos/${id}?fields=videoId,title,description`;
        const { data: entry } = await axiosClient.get<InvidiousVideoDetails>(url);
        return {
            description: entry.description,
            id: entry.videoId,
            mediaType: "other",
            poster: `${this.mainUrl}/vi/${entry.videoId}/hqdefault.jpg`,
            title: entry.title,
        };
    }

    override getStreams(id: string, _mediaType: MediaType): Promise<StreamLink[]> {
        return Promise.resolve([
            {
                language: "original",
                quality: "DASH",
                url: `${this.mainUrl}/api/manifest/dash/id/${id}`,
            },
        ]);
    }
}
