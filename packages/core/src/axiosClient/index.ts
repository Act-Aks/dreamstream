import axios, {
    type AxiosError,
    type AxiosInstance,
    type AxiosRequestConfig,
    type InternalAxiosRequestConfig,
} from 'axios'

interface AxiosClientOptions {
    /**
     * Base URL for all requests.
     */
    baseUrl?: string
    /**
     * Extra headers that should always be sent.
     */
    defaultHeaders?: Record<string, string>
    /**
     * Function that returns the current auth token.
     * If provided, it will be injected as Authorization header on each request.
     */
    getToken?: () => string | undefined
    /**
     * Optional hook for global error handling (e.g., log out on 401).
     */
    onError?: (error: AxiosError) => void
    /**
     * Timeout in milliseconds for all requests.
     * @default 15000
     */
    timeoutMs?: number
}

export class AxiosClient {
    private readonly instance: AxiosInstance

    constructor(options: AxiosClientOptions = {}) {
        const { baseUrl = '', timeoutMs = 15_000, getToken, onError, defaultHeaders } = options

        this.instance = axios.create({
            baseURL: baseUrl,
            headers: { 'Content-Type': 'application/json', ...defaultHeaders },
            timeout: timeoutMs,
        })

        this.instance.interceptors.request.use(
            (config: InternalAxiosRequestConfig) => {
                if (getToken) {
                    const token = getToken()
                    if (token) {
                        config.headers.Authorization = `Bearer ${token}`
                    }
                }
                return config
            },
            (error) => Promise.reject(error)
        )

        this.instance.interceptors.response.use(
            (response) => response,
            (error) => {
                if (onError) {
                    onError(error)
                }
                return Promise.reject(error)
            }
        )
    }

    /* Generic helpers – always return response.data with correct typing */

    async get<Data = unknown>(url: string, config?: AxiosRequestConfig): Promise<Data> {
        const res = await this.instance.get<Data>(url, config)
        return res.data
    }

    async post<Data = unknown, Body = unknown>(url: string, body?: Body, config?: AxiosRequestConfig): Promise<Data> {
        const res = await this.instance.post<Data>(url, body, config)
        return res.data
    }

    async put<Data = unknown, Body = unknown>(url: string, body?: Body, config?: AxiosRequestConfig): Promise<Data> {
        const res = await this.instance.put<Data>(url, body, config)
        return res.data
    }

    async patch<Data = unknown, Body = unknown>(url: string, body?: Body, config?: AxiosRequestConfig): Promise<Data> {
        const res = await this.instance.patch<Data>(url, body, config)
        return res.data
    }

    async delete<Data = unknown>(url: string, config?: AxiosRequestConfig): Promise<Data> {
        const res = await this.instance.delete<Data>(url, config)
        return res.data
    }

    /* Expose raw axios instance if you ever need it */
    get raw(): AxiosInstance {
        return this.instance
    }
}
