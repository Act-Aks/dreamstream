import { QueryClient } from '@tanstack/react-query'

export const queryClient = new QueryClient({
    defaultOptions: {
        mutations: {
            networkMode: 'online',
            retry: 1, // Retry mutations once
            retryDelay: 1000,
            throwOnError: false,
        },
        queries: {
            gcTime: 30 * 60 * 1000, // 30 minutes - keep in cache for 30 minutes

            // Network mode - continue with cached data if offline
            networkMode: 'online',
            notifyOnChangeProps: 'all', // Only notify on tracked properties

            // Performance optimizations
            refetchInterval: false, // Disable automatic polling by default
            refetchOnMount: true, // Refetch when component mounts
            refetchOnReconnect: 'always', // Always refetch when reconnected

            // Network configuration
            refetchOnWindowFocus: false, // Don't refetch when app regains focus
            // Retry configuration
            retry: (failureCount, error) => {
                // Don't retry on abort errors or 4xx errors
                if (error.name === 'AbortError') {
                    return false
                }
                if (error.message?.includes('4')) {
                    return false // 4xx errors
                }

                // Retry up to 3 times for other errors with exponential backoff
                return failureCount < 3
            },
            retryDelay: (attemptIndex) => Math.min(1000 * 2 ** attemptIndex, 30_000),

            // Cache configuration
            staleTime: 5 * 60 * 1000, // 5 minutes - data is fresh for 5 minutes

            // Error handling
            throwOnError: false, // Don't throw errors, handle them in components
        },
    },
})

// Enhanced query client for development with additional logging
function createDevQueryClient() {
    const client = new QueryClient({
        defaultOptions: queryClient.getDefaultOptions(),
    })

    // Add development-only event listeners
    if (__DEV__) {
        client.getQueryCache().subscribe((event) => {
            console.info('Query cache event:', event.type, event.query.queryKey)
        })

        client.getMutationCache().subscribe((event) => {
            console.info('Mutation cache event:', event.type, event?.mutation?.options?.mutationKey)
        })
    }

    return client
}

// Performance monitoring utilities
export const queryClientUtils = {
    // Clear specific cache patterns
    clearCache: (patterns: string[]) => {
        for (const pattern of patterns) {
            queryClient.removeQueries({ queryKey: [pattern] })
        }
    },
    // Get cache statistics
    getCacheStats: () => {
        const queryCache = queryClient.getQueryCache()
        const queries = queryCache.getAll()

        return {
            errorQueries: queries.filter((q) => q.state.status === 'error').length,
            freshQueries: queries.filter((q) => q.isStale() === false).length,
            loadingQueries: queries.filter((q) => q.state.fetchStatus === 'fetching').length,
            staleQueries: queries.filter((q) => q.isStale() === true).length,
            totalQueries: queries.length,
        }
    },

    // Prefetch common queries
    prefetchCommonData: async (_providerValue: string) => {
        // Prefetch catalog data
        // await queryClient.prefetchQuery({
        //     queryFn: async () => {
        //         const { providerManager } = await import('./services/ProviderManager')
        //         return providerManager.getCatalog({ providerValue })
        //     },
        //     queryKey: ['catalog', providerValue],
        //     staleTime: 10 * 60 * 1000, // 10 minutes
        // })
    },

    // Optimistic updates helper
    setOptimisticData: <T>(queryKey: unknown[], updater: (old: T | undefined) => T) => {
        queryClient.setQueryData(queryKey, updater)
    },
}

// Export the appropriate client based on environment
export const appQueryClient = __DEV__ ? createDevQueryClient() : queryClient
