import { Stack } from 'expo-router'

export default function ScreenLayout() {
    return (
        <Stack>
            <Stack.Screen name='media/[id]' options={{ headerShown: false }} />
        </Stack>
    )
}
