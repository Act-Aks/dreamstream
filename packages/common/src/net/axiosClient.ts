import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse } from "axios";

export class AxiosClient {
    private readonly client: AxiosInstance;

    constructor(baseConfig?: AxiosRequestConfig) {
        this.client = axios.create({
            headers: { "User-Agent": "DreamStream/1.0" },
            timeout: 15_000,
            ...baseConfig,
        });
    }

    get<T = unknown>(url: string, config?: AxiosRequestConfig): Promise<AxiosResponse<T>> {
        return this.client.get<T>(url, config);
    }

    post<T = unknown>(
        url: string,
        data?: Record<string, unknown>,
        config?: AxiosRequestConfig
    ): Promise<AxiosResponse<T>> {
        return this.client.post<T>(url, data, config);
    }
}
