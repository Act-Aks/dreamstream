// biome-ignore lint/performance/noNamespaceImport: by docs
import * as Notifications from 'expo-notifications'
import { createContext, type PropsWithChildren, useContext, useEffect, useMemo, useRef, useState } from 'react'
import { AppNotification } from '@/utils/notifications'

interface NotificationContextType {
    error: Error | null
    expoPushToken: string | null
    notification: Notifications.Notification | null
}

const NotificationContext = createContext<NotificationContextType | undefined>(undefined)

export const NotificationProvider: React.FC<PropsWithChildren> = ({ children }) => {
    const [error, setError] = useState<Error | null>(null)
    const [expoPushToken, setExpoPushToken] = useState<string | null>(null)
    const [notification, setNotification] = useState<Notifications.Notification | null>(null)

    const notificationListener = useRef<Notifications.EventSubscription>(null)
    const responseListener = useRef<Notifications.EventSubscription>(null)

    useEffect(() => {
        AppNotification.registerForPushNotifications()
            .then((token) => setExpoPushToken(token ?? ''))
            .catch((error) => setError(error))

        notificationListener.current = Notifications.addNotificationReceivedListener((notification) => {
            console.info('🔔 Notification Received: ', notification)
            setNotification(notification)
        })

        responseListener.current = Notifications.addNotificationResponseReceivedListener((response) => {
            console.info(
                '🔔 Notification Response: ',
                JSON.stringify(response, null, 2),
                JSON.stringify(response.notification.request.content.data, null, 2)
            )
        })

        return () => {
            notificationListener.current?.remove()
            responseListener.current?.remove()
        }
    }, [])

    const value = useMemo(() => ({ error, expoPushToken, notification }), [error, expoPushToken, notification])

    return <NotificationContext.Provider value={value}>{children}</NotificationContext.Provider>
}

export function useNotification() {
    const context = useContext(NotificationContext)
    if (context === undefined) {
        throw new Error('useNotification must be used within a NotificationProvider')
    }
    return context
}
