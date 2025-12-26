export type ProviderType = 'global' | 'english' | 'india' | 'italy' | 'anime' | 'drama'
export type ProviderState = 'available' | 'disabled' | 'installed' | 'installing' | 'un-installing' | 'updating'

export interface Provider {
    icon: string
    id: string
    installedAt?: number
    name: string
    state: ProviderState
    type: ProviderType
    updatedAt?: number
    value: string
    version: string
}

export interface ProviderModule {
    value: string
    version: string
    modules: {
        posts?: string
        meta?: string
        stream?: string
        catalog?: string
        episodes?: string
    }
    cachedAt: number
}
