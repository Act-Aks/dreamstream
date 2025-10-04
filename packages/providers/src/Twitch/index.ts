import { AxiosClient } from "@dreamstream/common/net";
import {
    BaseProvider,
    type DetailsResult,
    type MediaType,
    type ProviderType,
    type SearchResult,
    type StreamLink,
} from "@dreamstream/common/types";

type TwitchProviderOptions = {
    clientId: string;
    oauthToken: string;
};

type TwitchChannel = {
    broadcaster_login: string;
    display_name: string;
    title: string;
    thumbnail_url: string;
};

type TwitchSearchResponse = {
    data: TwitchChannel[];
};

type TwitchUser = {
    login: string;
    display_name: string;
    description: string;
    profile_image_url: string;
};

type TwitchUsersResponse = {
    data: TwitchUser[];
};

export class TwitchProvider extends BaseProvider {
    static override readonly id = "twitch";
    static override readonly name = "Twitch";
    static override readonly providerType: ProviderType = "live_tv";
    static override readonly supportedMediaTypes: MediaType[] = ["tv_channel"];
    static override readonly description = "Twitch live channels and streams (Helix API)";
    static override readonly icon = "https://static.twitchcdn.net/assets/favicon-32-e29e246c157142c94346.png";

    private readonly axiosClient: AxiosClient;

    constructor(options: TwitchProviderOptions) {
        super();
        if (!(options.clientId && options.oauthToken)) {
            throw new Error("TwitchProvider requires clientId and oauthToken in the constructor options.");
        }
        this.axiosClient = new AxiosClient({
            baseURL: "https://api.twitch.tv/helix",
            headers: {
                Authorization: `Bearer ${options.oauthToken}`,
                "Client-ID": options.clientId,
            },
        });
    }

    override async search(query: string, _mediaType?: MediaType): Promise<SearchResult<"tv_channel">[]> {
        const { data } = await this.axiosClient.get<TwitchSearchResponse>("/search/channels", {
            params: {
                first: 10,
                live_only: true,
                query,
            },
        });

        return data.data.map((item) => ({
            description: item.title,
            id: `https://www.twitch.tv/${item.broadcaster_login}`,
            mediaType: "tv_channel",
            poster: item.thumbnail_url,
            title: item.display_name,
        }));
    }

    override async getDetails(id: string, _mediaType: MediaType): Promise<DetailsResult<"tv_channel">> {
        const username = id.split("/").pop();
        const { data } = await this.axiosClient.get<TwitchUsersResponse>("/users", { params: { login: username } });
        const entry = data.data[0];

        if (!entry) {
            throw new Error(`Twitch user not found: ${username}`);
        }

        return {
            description: entry.description,
            id: `https://www.twitch.tv/${entry.login}`,
            mediaType: "tv_channel",
            poster: entry.profile_image_url,
            title: entry.display_name,
        };
    }

    override getStreams(id: string, _mediaType: MediaType): Promise<StreamLink[]> {
        return Promise.resolve([
            {
                language: "original",
                quality: "auto",
                subtitles: undefined,
                url: id,
            },
        ]);
    }
}
