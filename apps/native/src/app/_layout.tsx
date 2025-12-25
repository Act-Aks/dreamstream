import '@/styles/global.css'
import { Stack } from 'expo-router'
import { HeroUINativeProvider } from 'heroui-native'
import { GestureHandlerRootView } from 'react-native-gesture-handler'
import { KeyboardProvider } from 'react-native-keyboard-controller'
import { heroNativeUiConfig } from '@/configs/heroNativeUi'
import { NotificationProvider } from '@/contexts/NotificationContext'
import { AppThemeProvider } from '@/contexts/ThemeContext'
import { AppNotification } from '@/utils/notifications'

AppNotification.initNotificationHandler()

function StackLayout() {
    return (
        <Stack>
            <Stack.Screen name={'(tabs)'} options={{ headerShown: false }} />
        </Stack>
    )
}

export default function RootLayout() {
    return (
        <GestureHandlerRootView style={{ flex: 1 }}>
            <NotificationProvider>
                <KeyboardProvider>
                    <AppThemeProvider>
                        <HeroUINativeProvider config={heroNativeUiConfig}>
                            <StackLayout />
                        </HeroUINativeProvider>
                    </AppThemeProvider>
                </KeyboardProvider>
            </NotificationProvider>
        </GestureHandlerRootView>
    )
}
