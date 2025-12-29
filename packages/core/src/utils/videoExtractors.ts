import type { Source, Video } from '@/types/base'
import { ClientProxy } from './clientProxy'

export abstract class VideoExtractor extends ClientProxy {
    /**
     * The server name of the video provider
     */
    protected abstract serverName: string

    /**
     * list of videos available
     */
    protected abstract sources: Video[]

    /**
     * takes video link
     *
     * returns video sources (video links) available
     */
    protected abstract extract(videoUrl: URL, ...args: unknown[]): Promise<Video[] | Source>
}
