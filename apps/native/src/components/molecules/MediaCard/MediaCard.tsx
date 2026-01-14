import type { AnimeInfo } from '@dreamstream/core'
import { Card, Chip, PressableFeedback } from 'heroui-native'
import { Image, View } from 'react-native'

export interface MediaCardProps extends Pick<AnimeInfo, 'title' | 'image' | 'type'> {
    episodes?: number | null
    sub?: number | null
    dub?: number | null
    onPlay: () => void
}

export const MediaCard: React.FC<MediaCardProps> = (props) => {
    const { title, image, type, episodes, sub } = props

    const episodesLabel = (() => {
        if (episodes) {
            return `${episodes} episode${episodes === 1 ? '' : 's'}`
        }
        if (sub) {
            return `${sub} sub`
        }
        return 'N/A'
    })()

    return (
        <PressableFeedback className='flex flex-1'>
            <Card className='h-[400px] w-full'>
                <Image className='absolute inset-0' source={{ uri: image }} />
                <Card.Title>{title.toString()}</Card.Title>
                <View className='flex-row items-center gap-2'>
                    <Chip size='sm' variant='primary'>
                        {type}
                    </Chip>
                    <Card.Title>{episodesLabel}</Card.Title>
                </View>
            </Card>
        </PressableFeedback>
    )
}
