import Ionicons from '@expo/vector-icons/Ionicons'
import { Avatar, useThemeColor } from 'heroui-native'
import { FadeInDown } from 'react-native-reanimated'

export const Profile: React.FC = () => {
    const themeColorForeground = useThemeColor('foreground')

    return (
        <Avatar alt='profile' color='accent'>
            <Avatar.Fallback animation={{ entering: { value: FadeInDown.duration(400) } }}>
                <Ionicons color={themeColorForeground} name='person' size={24} />
            </Avatar.Fallback>
        </Avatar>
    )
}
