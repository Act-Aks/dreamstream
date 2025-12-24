import "@/styles/global.css";
import { Stack } from "expo-router";
import { HeroUINativeProvider } from "heroui-native";
import { GestureHandlerRootView } from "react-native-gesture-handler";
import { KeyboardProvider } from "react-native-keyboard-controller";
import { AppThemeProvider } from "@/contexts/app-theme-context";

function StackLayout() {
    return (
        <Stack>
            <Stack.Screen name={"(tabs)"} options={{ headerShown: false }} />
        </Stack>
    );
}

export default function RootLayout() {
    return (
        <GestureHandlerRootView style={{ flex: 1 }}>
            <KeyboardProvider>
                <AppThemeProvider>
                    <HeroUINativeProvider>
                        <StackLayout />
                    </HeroUINativeProvider>
                </AppThemeProvider>
            </KeyboardProvider>
        </GestureHandlerRootView>
    );
}
