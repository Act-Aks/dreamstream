import { type UseQueryOptions, useSuspenseQuery } from '@tanstack/react-query'
import type { QueryKeys } from '@/types/query'

export const useProviderData = <T>(
    key: QueryKeys,
    fetcher: () => Promise<T>,
    options?: Omit<UseQueryOptions<T>, 'queryKey' | 'queryFn'>
) => {
    return useSuspenseQuery({ queryFn: fetcher, queryKey: key, ...options })
}
