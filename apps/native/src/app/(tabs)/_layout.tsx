import { Ionicons } from '@expo/vector-icons'
import { Tabs } from 'expo-router'
import { useThemeColor } from 'heroui-native'
import { Profile } from '@/components/molecules/Profile/Profile'
import { SearchBar } from '@/components/molecules/SearchBar/SearchBar'

export default function TabLayout() {
    const themeColorForeground = useThemeColor('foreground')
    const themeColorBackground = useThemeColor('background')

    return (
        <Tabs
            screenOptions={{
                headerStyle: { backgroundColor: themeColorBackground },
                headerTintColor: themeColorForeground,
                headerTitleStyle: { color: themeColorForeground, fontWeight: '600' },
                tabBarStyle: { backgroundColor: themeColorBackground },
            }}
        >
            <Tabs.Screen
                name='index'
                options={{
                    headerLeft: () => <SearchBar />,
                    headerLeftContainerStyle: { paddingLeft: 8 },
                    headerRight: () => <Profile />,
                    headerRightContainerStyle: { paddingRight: 8 },
                    headerTitle: '',
                    tabBarIcon: ({ color, size }) => <Ionicons color={color} name='home' size={size} />,
                    title: 'Home',
                }}
            />
            <Tabs.Screen
                name={'search'}
                options={{
                    headerShown: false,
                    tabBarIcon: ({ color, size }) => <Ionicons color={color} name='search' size={size} />,
                    title: 'Search',
                }}
            />
            <Tabs.Screen
                name={'downloads'}
                options={{
                    headerShown: false,
                    tabBarIcon: ({ color, size }) => <Ionicons color={color} name='download' size={size} />,
                    title: 'Downloads',
                }}
            />
            <Tabs.Screen
                name={'settings'}
                options={{
                    headerShown: false,
                    tabBarIcon: ({ color, size }) => <Ionicons color={color} name='settings' size={size} />,
                    title: 'Settings',
                }}
            />
        </Tabs>
    )
}
