import '@tanstack/react-query'

export type QueryKeys = ['home' | 'search' | 'download' | 'media', ...(readonly unknown[])]

declare module '@tanstack/react-query' {
    interface Register {
        queryKey: QueryKeys
    }
}
