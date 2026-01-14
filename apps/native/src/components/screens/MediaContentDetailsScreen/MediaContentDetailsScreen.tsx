import { streamingServers, subOrDub } from '@dreamstream/core'
import { AnimeKai } from '@dreamstream/providers'
import { useQuery } from '@tanstack/react-query'
import { Stack, useLocalSearchParams } from 'expo-router'
import { useVideoPlayer, VideoView } from 'expo-video'
import { Button, Card, Chip, Divider, SkeletonGroup } from 'heroui-native'
import type React from 'react'
import { useEffect, useState } from 'react'
import { Image, ScrollView, StyleSheet, Text, View } from 'react-native'
import Animated, { FadeInUp, Layout } from 'react-native-reanimated'
import { Container } from '@/components/atoms/Container/Container'
import { ToastHooks } from '@/utils/hooks/toast/toast.hooks'

const animekai = new AnimeKai()
const HERO_ASPECT_RATIO = 16 / 9

export const MediaContentDetailScreen: React.FC = () => {
    const { id: mediaId } = useLocalSearchParams<{ id: string }>()
    const { toast } = ToastHooks.useAppToast()

    const [streamUrl, setStreamUrl] = useState<string | null>(null)
    const [isResolvingSource, setIsResolvingSource] = useState(false)

    const { data, isLoading, isFetching, error, refetch } = useQuery({
        queryFn: () => {
            const id = Array.isArray(mediaId) ? mediaId[0] : mediaId
            if (!id) {
                throw new Error('Invalid media id')
            }
            return animekai.fetchAnimeInfo(id)
        },
        queryKey: ['media', mediaId],
    })

    // Create a player once; start paused
    const player = useVideoPlayer(
        // initial placeholder – will be replaced when streamUrl is ready
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4',
        (p) => {
            p.loop = false
            p.pause()
        }
    )

    // When we have a real stream URL, swap the source
    useEffect(() => {
        if (player && streamUrl) {
            player.replace(streamUrl)
            player.pause() // let user press native Play
        }
    }, [player, streamUrl])

    // Resolve episode sources once details are loaded (you can also keep this on a button)
    const resolveStream = async () => {
        if (!data) {
            return
        }

        const firstPlayable = data?.episodes?.find((ep) => ep.isSubbed || ep.isDubbed)
        if (!firstPlayable) {
            toast('error')('No playable episodes yet')
            return
        }

        try {
            setIsResolvingSource(true)
            const sources = await animekai.fetchEpisodeSources(
                firstPlayable.id,
                streamingServers.MegaUp,
                firstPlayable.isDubbed ? subOrDub.DUB : subOrDub.SUB
            )

            const url = (Array.isArray(sources.sources) && sources.sources[0]?.url) || sources.embedURL

            if (!url) {
                toast('error')('No stream URL found')
                return
            }

            setStreamUrl(url)
            toast('success')(`Ready: ${data.title} • EP ${firstPlayable.number}`)
        } catch {
            toast('error')('Failed to load sources')
        } finally {
            setIsResolvingSource(false)
        }
    }

    if (isLoading) {
        return (
            <Container>
                <Stack.Screen options={{ title: 'Loading…' }} />
                <View className='p-4'>
                    <SkeletonGroup className='flex-row items-center gap-3' isLoading>
                        <SkeletonGroup.Item className='h-24 w-20 rounded-lg' />
                        <View className='flex-1 gap-1.5'>
                            <SkeletonGroup.Item className='h-5 w-full rounded-md' />
                            <SkeletonGroup.Item className='h-3 w-3/4 rounded-md' />
                            <SkeletonGroup.Item className='h-3 w-1/2 rounded-md' />
                        </View>
                    </SkeletonGroup>
                </View>
            </Container>
        )
    }

    if (error || !data) {
        return (
            <Container>
                <Stack.Screen options={{ title: 'Error' }} />
                <View className='flex-1 items-center justify-center px-6'>
                    <Text className='mb-2 font-semibold text-base'>Failed to load</Text>
                    <Text className='mb-4 text-foreground-500 text-xs' numberOfLines={2}>
                        Could not fetch media details. Check your connection and try again.
                    </Text>
                    <Button onPress={() => refetch()} variant='primary'>
                        Retry
                    </Button>
                </View>
            </Container>
        )
    }

    const {
        title,
        image,
        description,
        type,
        status,
        season,
        episodes,
        totalEpisodes,
        genres,
        hasSub,
        hasDub,
        recommendations,
        relations,
    } = data

    return (
        <Container>
            <Stack.Screen options={{ title: title.toString() }} />
            <Animated.View className='flex-1' entering={FadeInUp.springify()} layout={Layout.springify()}>
                <ScrollView contentContainerStyle={{ paddingBottom: 24 }} showsVerticalScrollIndicator={false}>
                    {/* Hero player – always visible, native controls */}
                    <View style={styles.heroWrapper}>
                        <View style={styles.hero}>
                            <VideoView contentFit='cover' nativeControls player={player} style={styles.video} />

                            {/* Poster visible until real stream is loaded */}
                            {!streamUrl && (
                                <Image resizeMode='cover' source={{ uri: image }} style={StyleSheet.absoluteFill} />
                            )}

                            {/* Top label */}
                            <View className='absolute inset-x-0 top-0 bg-gradient-to-b from-black/40 to-transparent p-3'>
                                <Text className='font-semibold text-sm text-white' numberOfLines={1}>
                                    {type} • {status}
                                </Text>
                            </View>

                            {/* Bottom title + chips */}
                            <View className='absolute inset-x-0 bottom-0 bg-gradient-to-t from-black/55 to-transparent p-3'>
                                <Text className='font-semibold text-lg text-white' numberOfLines={2}>
                                    {title.toString()}
                                </Text>
                                <View className='mt-2 flex-row flex-wrap items-center gap-2'>
                                    <Chip size='sm' variant='primary'>
                                        {type}
                                    </Chip>
                                    <Chip size='sm' variant='primary'>
                                        {status}
                                    </Chip>
                                    {season ? (
                                        <Chip size='sm' variant='primary'>
                                            {season}
                                        </Chip>
                                    ) : null}
                                </View>
                            </View>
                        </View>
                    </View>

                    {/* Action row – only to resolve the source; playback is via native controls */}
                    <View className='mt-3 flex-row items-center gap-3 px-4'>
                        <Button
                            className='flex-1'
                            isDisabled={isResolvingSource || !!streamUrl}
                            onPress={resolveStream}
                            variant='primary'
                        >
                            {(() => {
                                if (isResolvingSource) {
                                    return 'Loading…'
                                }
                                if (streamUrl) {
                                    return 'Ready'
                                }
                                return 'Load episode'
                            })()}
                        </Button>
                        <Button
                            className='w-28'
                            isDisabled={isFetching}
                            onPress={() => refetch()}
                            size='sm'
                            variant='ghost'
                        >
                            {isFetching ? 'Refreshing…' : 'Refresh'}
                        </Button>
                    </View>

                    {/* Description + meta */}
                    <View className='mt-3 mb-4 gap-2 px-4'>
                        <Text className='text-foreground-500 text-sm'>
                            {description || 'No description available.'}
                        </Text>

                        <View className='mt-2 flex-row flex-wrap gap-2'>
                            {genres?.map((g) => (
                                <Chip key={g} size='sm' variant='primary'>
                                    {g}
                                </Chip>
                            ))}
                        </View>

                        <View className='mt-3 gap-1'>
                            <Text className='text-foreground-500 text-xs'>
                                Episodes: {totalEpisodes ?? episodes?.length ?? 0}
                            </Text>
                            <Text className='text-foreground-500 text-xs'>
                                Sub: {hasSub ? 'Available' : 'No sub'} • Dub: {hasDub ? 'Available' : 'No dub'}
                            </Text>
                        </View>
                    </View>

                    <Divider className='mx-4 my-2' />

                    {/* Episodes */}
                    <View className='mb-4 px-4'>
                        <Text className='mb-2 font-semibold text-base'>Episodes</Text>
                        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
                            {episodes?.slice(0, 20).map((ep) => (
                                <Card className='mr-3 w-44' key={ep.id}>
                                    <Card.Body className='gap-1 p-3'>
                                        <Text className='font-semibold text-xs' numberOfLines={1}>
                                            EP {ep.number}
                                        </Text>
                                        {ep.title ? (
                                            <Text className='text-[11px] text-foreground-500' numberOfLines={2}>
                                                {ep.title}
                                            </Text>
                                        ) : null}
                                        <Text className='mt-1 text-[10px] text-foreground-500'>
                                            {ep.isDubbed ? 'Dub • ' : ''}
                                            {ep.isSubbed ? 'Sub' : ''}
                                            {ep.isFiller ? ' • Filler' : ''}
                                        </Text>
                                    </Card.Body>
                                </Card>
                            ))}
                        </ScrollView>
                    </View>

                    {/* Recommendations */}
                    {recommendations && recommendations.length > 0 && (
                        <>
                            <Divider className='mx-4 my-2' />
                            <View className='mb-4 px-4'>
                                <Text className='mb-2 font-semibold text-base'>You might also like</Text>
                                <ScrollView horizontal showsHorizontalScrollIndicator={false}>
                                    {recommendations.slice(0, 12).map((rec) => (
                                        <Card className='mr-3 w-40' key={rec.id}>
                                            <Image
                                                className='h-24 w-full'
                                                resizeMode='cover'
                                                source={{ uri: rec.image }}
                                            />
                                            <Card.Body className='p-2'>
                                                <Text className='font-semibold text-[11px]' numberOfLines={2}>
                                                    {rec.title.toString()}
                                                </Text>
                                                <Text className='text-[10px] text-foreground-500' numberOfLines={1}>
                                                    {rec.type}
                                                </Text>
                                            </Card.Body>
                                        </Card>
                                    ))}
                                </ScrollView>
                            </View>
                        </>
                    )}

                    {/* Relations */}
                    {relations && relations.length > 0 && (
                        <>
                            <Divider className='mx-4 my-2' />
                            <View className='mb-4 px-4'>
                                <Text className='mb-2 font-semibold text-base'>Related</Text>
                                {relations.slice(0, 8).map((rel) => (
                                    <View className='mb-2 flex-row items-center justify-between' key={rel.id}>
                                        <View className='mr-2 flex-1'>
                                            <Text className='text-sm' numberOfLines={1}>
                                                {rel.title.toString()}
                                            </Text>
                                            <Text className='text-[11px] text-foreground-500' numberOfLines={1}>
                                                {rel.relationType} • {rel.type}
                                            </Text>
                                        </View>
                                        <Button size='sm' variant='secondary'>
                                            View
                                        </Button>
                                    </View>
                                ))}
                            </View>
                        </>
                    )}
                </ScrollView>
            </Animated.View>
        </Container>
    )
}

export default MediaContentDetailScreen

const styles = StyleSheet.create({
    hero: {
        aspectRatio: HERO_ASPECT_RATIO,
        backgroundColor: 'black',
        overflow: 'hidden',
        width: '100%',
    },
    heroWrapper: {
        width: '100%',
    },
    video: {
        backgroundColor: 'black',
        height: '100%',
        width: '100%',
    },
})
