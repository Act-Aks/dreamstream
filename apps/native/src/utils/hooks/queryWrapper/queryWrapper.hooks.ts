import type { Search } from '@dreamstream/core'
import {
    type InfiniteData,
    type UseInfiniteQueryOptions,
    type UseQueryOptions,
    useInfiniteQuery as useReactInfiniteQuery,
    useQuery as useReactQuery,
} from '@tanstack/react-query'
import type { QueryKeys } from '@/types/query'

interface BaseQueryProps {
    key: QueryKeys
}

interface UseQueryProps<Data = unknown> extends BaseQueryProps {
    fetcher: () => Promise<Data>
    options?: Omit<UseQueryOptions<Data>, 'queryKey' | 'queryFn'>
}

interface UseInfiniteQueryProps<Data = unknown> extends BaseQueryProps {
    fetcher: (page?: number) => Promise<Data>
    options?: Omit<
        UseInfiniteQueryOptions<Data, Error, InfiniteData<Data, unknown>>,
        'queryKey' | 'queryFn' | 'getNextPageParam' | 'initialPageParam'
    >
}

export function useQuery<Data = unknown>({ fetcher, key, options = {} }: UseQueryProps<Data>) {
    return useReactQuery({
        queryFn: fetcher,
        queryKey: key,
        ...options,
    })
}

export function useInfiniteQuery<Data = unknown>({ fetcher, key, options = {} }: UseInfiniteQueryProps<Search<Data>>) {
    return useReactInfiniteQuery<Search<Data>, Error>({
        getNextPageParam: (lastPage) =>
            lastPage.hasNextPage && lastPage.currentPage ? lastPage.currentPage + 1 : undefined,
        initialPageParam: 1,
        queryFn: ({ pageParam = 1 }) => fetcher(pageParam as number),
        queryKey: key,
        ...options,
    })
}
