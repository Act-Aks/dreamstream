/** biome-ignore-all lint/performance/noNamespaceImport: default way to import from docs */
import Constants from 'expo-constants'
import * as Device from 'expo-device'
import * as Notifications from 'expo-notifications'
import { Platform } from 'react-native'

function handleRegistrationError(errorMessage: string) {
    throw new Error(errorMessage)
}

async function registerForPushNotifications(): Promise<string | undefined> {
    if (Platform.OS === 'android') {
        await Notifications.setNotificationChannelAsync('default', {
            importance: Notifications.AndroidImportance.MAX,
            lightColor: '#FF231F7C',
            name: 'default',
            vibrationPattern: [0, 250, 250, 250],
        })
    }

    if (Device.isDevice) {
        const { status: existingStatus } = await Notifications.getPermissionsAsync()
        let finalStatus = existingStatus
        if (existingStatus !== 'granted') {
            const { status } = await Notifications.requestPermissionsAsync()
            finalStatus = status
        }
        if (finalStatus !== 'granted') {
            handleRegistrationError('Permission not granted to get push token for push notification!')
            return
        }
        const projectId = Constants?.expoConfig?.extra?.eas?.projectId ?? Constants?.easConfig?.projectId
        if (!projectId) {
            handleRegistrationError('Project ID not found')
        }
        try {
            const pushTokenString = (
                await Notifications.getExpoPushTokenAsync({
                    projectId,
                })
            ).data
            return pushTokenString
        } catch (error) {
            handleRegistrationError(`${error}`)
            return
        }
    } else {
        handleRegistrationError('Must use physical device for push notifications')
        return
    }
}

function initNotificationHandler() {
    Notifications.setNotificationHandler({
        handleNotification: async () => ({
            shouldPlaySound: true,
            shouldSetBadge: true,
            shouldShowBanner: true,
            shouldShowList: true,
        }),
    })
}

export const AppNotification = {
    initNotificationHandler,
    registerForPushNotifications,
}
