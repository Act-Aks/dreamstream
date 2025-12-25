import { Ionicons } from '@expo/vector-icons'
import { Tabs } from 'expo-router'
import { useThemeColor } from 'heroui-native'

export default function TabLayout() {
    const themeColorForeground = useThemeColor('foreground')
    const themeColorBackground = useThemeColor('background')

    return (
        <Tabs
            screenOptions={{
                headerShown: false,
                headerStyle: { backgroundColor: themeColorBackground },
                headerTintColor: themeColorForeground,
                headerTitleStyle: { color: themeColorForeground, fontWeight: '600' },
                tabBarStyle: { backgroundColor: themeColorBackground },
            }}
        >
            <Tabs.Screen
                name='index'
                options={{
                    tabBarIcon: ({ color, size }) => <Ionicons color={color} name='home' size={size} />,
                    title: 'Home',
                }}
            />
            <Tabs.Screen
                name={'search'}
                options={{
                    tabBarIcon: ({ color, size }) => <Ionicons color={color} name='search' size={size} />,
                    title: 'Search',
                }}
            />
            <Tabs.Screen
                name={'downloads'}
                options={{
                    tabBarIcon: ({ color, size }) => <Ionicons color={color} name='download' size={size} />,
                    title: 'Downloads',
                }}
            />
            <Tabs.Screen
                name={'settings'}
                options={{
                    tabBarIcon: ({ color, size }) => <Ionicons color={color} name='settings' size={size} />,
                    title: 'Settings',
                }}
            />
        </Tabs>
    )
}
