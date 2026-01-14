import type { Search } from '@dreamstream/core'
import { LegendList, type LegendListRenderItemProps } from '@legendapp/list'
import { router } from 'expo-router'
import { Card, SkeletonGroup } from 'heroui-native'
import { View } from 'react-native'
import { useInfiniteQuery } from '@/utils/hooks/queryWrapper/queryWrapper.hooks'
import { MediaCard, type MediaCardProps } from './MediaCard/MediaCard'

interface Media extends Omit<MediaCardProps, 'onPress'> {}
interface ScrollableMediaCardsProps<R extends Media> {
    title: string
    sectionKey: string
    fetchFn: (page?: number) => Promise<Search<R>>
}

export const ScrollableMediaCards = <R extends Media>({ title, sectionKey, fetchFn }: ScrollableMediaCardsProps<R>) => {
    const { data, isLoading, isFetchingNextPage, error, fetchNextPage, hasNextPage } = useInfiniteQuery({
        fetcher: (page) => fetchFn(page),
        key: ['home', sectionKey],
    })

    const results: R[] = data?.pages.flatMap((page) => page.results) ?? []

    function onPress(id: string) {
        router.push(`/(screens)/media/${id}`)
    }

    if (isLoading && !results.length) {
        return (
            <SkeletonGroup className='flex-1 gap-2' isLoading>
                <View className='flex-row items-center justify-between'>
                    <SkeletonGroup.Item className='h-6 w-32 rounded-md' />
                    <SkeletonGroup.Item className='h-5 w-5 rounded-full' />
                </View>
                <View className='flex-row gap-3'>
                    {Array.from({ length: 5 }).map((i) => (
                        <SkeletonGroup.Item
                            className='h-40 w-28 min-w-[112px] flex-1 rounded-lg'
                            key={`${i}-skeleton`}
                        />
                    ))}
                </View>
            </SkeletonGroup>
        )
    }

    if (error && !results.length) {
        return (
            <View className='flex-1 items-center justify-center gap-2 p-4'>
                <Card.Title className='font-semibold text-xl'>{title}</Card.Title>
            </View>
        )
    }

    if (!results.length) {
        return null
    }

    return (
        <View className='gap-2 bg-amber-300'>
            <Card.Title className='font-semibold text-xl'>{title}</Card.Title>
            <LegendList
                contentContainerStyle={{ gap: 12 }}
                data={results}
                estimatedItemSize={20}
                horizontal
                keyExtractor={(item) => item.id}
                ListFooterComponent={
                    isFetchingNextPage ? (
                        <SkeletonGroup className='flex-row gap-3 p-4' isLoading>
                            {Array.from({ length: 3 }).map((i) => (
                                <SkeletonGroup.Item
                                    className='h-40 w-28 min-w-[112px] flex-1 rounded-lg'
                                    key={`${i}-fetch-more-skeleton`}
                                />
                            ))}
                        </SkeletonGroup>
                    ) : null
                }
                onEndReached={() => {
                    if (hasNextPage && !isFetchingNextPage) {
                        fetchNextPage()
                    }
                }}
                onEndReachedThreshold={0.4}
                renderItem={({ item }: LegendListRenderItemProps<R>) => <MediaCard {...item} onPress={onPress} />}
            />
        </View>
    )
}
