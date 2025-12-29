import type { AxiosAdapter } from 'axios'
import { AxiosClient } from '@/axiosClient'
import { assertCondition } from '@/generics'
import { isNotNullish } from '@/typeguards/null'
import type { ProxyConfig } from '@/types/base'

const validUrlRegex = /^https?:\/\/.+/u

interface RotatingProxyConfig extends Omit<ProxyConfig, 'url'> {
    urls: string[]
    rotateInterval?: number
}

interface SingleProxyConfig extends Omit<ProxyConfig, 'url'> {
    url: string
}

export class ClientProxy {
    protected readonly client: AxiosClient
    protected proxyConfig?: SingleProxyConfig | RotatingProxyConfig
    private readonly validUrl = validUrlRegex
    private rotationInterval?: NodeJS.Timeout | undefined

    /**
     * @param baseUrl - Base URL for the API
     * @param proxyConfig - Optional proxy configuration
     * @param adapter - Optional axios adapter
     */
    constructor(baseUrl: string, proxyConfig?: ProxyConfig, adapter?: AxiosAdapter) {
        this.client = new AxiosClient({ baseUrl })

        if (proxyConfig) {
            this.setProxy(proxyConfig)
        }
        if (adapter) {
            this.setAxiosAdapter(adapter)
        }
    }

    /**
     * Set or update proxy configuration (single URL or rotating list)
     */
    setProxy(config: ProxyConfig): asserts this is { proxyConfig: SingleProxyConfig | RotatingProxyConfig } {
        const urls = Array.isArray(config.url) ? config.url : [config.url].filter(isNotNullish)

        if (!urls?.length) {
            return
        }

        for (const [i, url] of urls.entries()) {
            if (typeof url !== 'string' || !this.validUrl.test(url)) {
                throw new Error(`Invalid proxy URL at index ${i}: ${url}`)
            }
        }

        // Stop existing rotation
        this.stopProxyRotation()

        assertCondition(config.key !== undefined, 'Proxy key not provided')
        if (urls.length === 1) {
            assertCondition(!!urls[0], 'Proxy url not provided')
            this.setupSingleProxy(urls[0], config.key)
            this.proxyConfig = { key: config.key, url: urls[0] }
        } else {
            this.setupRotatingProxy(urls, config)
            assertCondition(!!config.rotateInterval, 'Rotate interval not provided')
            this.proxyConfig = { key: config.key, rotateInterval: config.rotateInterval, urls }
        }
    }

    /**
     * Update axios adapter
     */
    setAxiosAdapter(adapter: AxiosAdapter): void {
        this.client.raw.defaults.adapter = adapter
    }

    /**
     * Stop proxy rotation (call before destroying instance)
     */
    stopProxyRotation(): void {
        if (this.rotationInterval) {
            clearInterval(this.rotationInterval)
            this.rotationInterval = undefined
        }
    }

    private setupSingleProxy(proxyUrl: string, apiKey?: string): void {
        this.client.raw.interceptors.request.use((config) => {
            if (proxyUrl) {
                config.headers.set('x-api-key', apiKey ?? '')
                config.url = `${proxyUrl}${config.url ?? ''}`
            }

            if (config.url?.includes('anify')) {
                config.headers.set('User-Agent', 'consumet')
            }

            return config
        })
    }

    private setupRotatingProxy(urls: string[], config: Omit<ProxyConfig, 'url'>): void {
        let currentIndex = 0

        this.rotationInterval = setInterval(() => {
            const currentUrl = urls[currentIndex]
            assertCondition(!!currentUrl, 'Proxy URL must exist')
            assertCondition(!!config.key, 'Proxy key must exist')

            this.setupSingleProxy(currentUrl, config.key)
            currentIndex = (currentIndex + 1) % urls.length
        }, config.rotateInterval ?? 5000)
    }

    // Cleanup on destroy
    dispose(): void {
        this.stopProxyRotation()
    }
}
