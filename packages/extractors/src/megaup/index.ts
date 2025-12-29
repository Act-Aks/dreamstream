import { getErrorMessage, type ProxyConfig, type Source, type Video, VideoExtractor } from '@dreamstream/core'
import type { AxiosAdapter } from 'axios'
import type {
    DecodedData,
    DecodedDataResponse,
    IFrameData,
    IFrameDataResponse,
    Token,
    TokenResponse,
} from './megaup.static'

export class MegaUp extends VideoExtractor {
    private readonly agent =
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36'
    protected serverName = 'MegaUp'
    protected sources: Video[] = []

    constructor(proxyConfig?: ProxyConfig, adapter?: AxiosAdapter) {
        super('https://enc-dec.app/api', proxyConfig, adapter)
    }

    async generateToken(text: string): Promise<Token> {
        try {
            const res = await this.client.get<TokenResponse>(`/enc-kai?text=${encodeURIComponent(text)}`)
            return res.result
        } catch (error) {
            throw new Error(getErrorMessage(error))
        }
    }

    async decodeIframeData(text: string): Promise<IFrameData> {
        try {
            const res = await this.client.post<IFrameDataResponse>('/dec-kai', { text })
            return res.result
        } catch (error) {
            throw new Error(getErrorMessage(error))
        }
    }

    async decode(text: string): Promise<DecodedData> {
        try {
            const res = await this.client.post<DecodedDataResponse>('/dec-mega', { agent: this.agent, text })
            return res.result
        } catch (error) {
            console.error(error)
            throw new Error(getErrorMessage(error))
        }
    }

    override async extract(videoUrl: URL): Promise<Source> {
        try {
            const url = videoUrl.href.replace('/e/', '/media/')
            const res = await this.client.get<{ result: string }>(url, {
                headers: { Connection: 'keep-alive', 'User-Agent': this.agent },
            })
            const decrypted = await this.decode(res.result)
            const data: Source = {
                download: decrypted.download,
                sources: decrypted.sources.map((source) => ({
                    isM3U8: source.file.includes('.m3u8') || source.file.endsWith('m3u8'),
                    url: source.file,
                })),
                subtitles: decrypted.tracks.map((track) => ({
                    kind: track.kind,
                    lang: track.label,
                    url: track.file,
                })),
            }
            return data
        } catch (error) {
            throw new Error(getErrorMessage(error))
        }
    }
}
