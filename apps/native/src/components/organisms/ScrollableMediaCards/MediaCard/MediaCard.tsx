import type { Title } from '@dreamstream/core'
import { Card, PressableFeedback } from 'heroui-native'
import { Image, View } from 'react-native'

export interface MediaCardProps {
    id: string
    image?: string
    title: string | Title
    sub?: number
    dub?: number
    episodes?: number
    type?: string
    onPress: (id: string) => void
}

export const MediaCard: React.FC<MediaCardProps> = ({ id, image, title, episodes, type, onPress }) => {
    const episodesLabel = episodes ? `${episodes} episodes` : 'Ongoing'

    return (
        <PressableFeedback onPress={() => onPress(id)}>
            <View style={{ aspectRatio: 2 / 3, height: 240 }}>
                <Card className='flex-1 overflow-hidden'>
                    <Image className='absolute inset-0' source={{ uri: image }} />
                    <Card.Title>{title.toString()}</Card.Title>
                    <Card.Footer className='absolute right-0 bottom-0 left-0 bg-black/70'>
                        <View className='flex-row justify-between'>
                            <View className='flex-row gap-2'>
                                <Card.Description>{episodesLabel}</Card.Description>
                            </View>
                            <Card.Description>{type}</Card.Description>
                        </View>
                    </Card.Footer>
                </Card>
            </View>
        </PressableFeedback>
    )
}
