import { Ionicons } from "@expo/vector-icons";
import { Tabs } from "expo-router";
import { useThemeColor } from "heroui-native";

export default function TabLayout() {
    const themeColorForeground = useThemeColor("foreground");
    const themeColorBackground = useThemeColor("background");

    return (
        <Tabs
            screenOptions={{
                headerShown: false,
                headerStyle: { backgroundColor: themeColorBackground },
                headerTintColor: themeColorForeground,
                headerTitleStyle: {
                    color: themeColorForeground,
                    fontWeight: "600",
                },
                tabBarStyle: {
                    backgroundColor: themeColorBackground,
                },
            }}
        >
            <Tabs.Screen
                name="index"
                options={{
                    tabBarIcon: ({ color, size }: { color: string; size: number }) => (
                        <Ionicons color={color} name="home" size={size} />
                    ),
                    title: "Home",
                }}
            />
            <Tabs.Screen
                name="two"
                options={{
                    tabBarIcon: ({ color, size }: { color: string; size: number }) => (
                        <Ionicons color={color} name="compass" size={size} />
                    ),
                    title: "Explore",
                }}
            />
        </Tabs>
    );
}
