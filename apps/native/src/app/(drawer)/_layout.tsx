import { Ionicons, MaterialIcons } from "@expo/vector-icons";
import { Link } from "expo-router";
import { Drawer } from "expo-router/drawer";
import { useThemeColor } from "heroui-native";
import { useCallback } from "react";
import { Pressable, Text } from "react-native";

import { ThemeToggle } from "@/src/components/theme-toggle";

function DrawerLayout() {
    const themeColorForeground = useThemeColor("foreground");
    const themeColorBackground = useThemeColor("background");

    const renderThemeToggle = useCallback(() => <ThemeToggle />, []);

    return (
        <Drawer
            screenOptions={{
                drawerStyle: { backgroundColor: themeColorBackground },
                headerRight: renderThemeToggle,
                headerStyle: { backgroundColor: themeColorBackground },
                headerTintColor: themeColorForeground,
                headerTitleStyle: {
                    color: themeColorForeground,
                    fontWeight: "600",
                },
            }}
        >
            <Drawer.Screen
                name="index"
                options={{
                    drawerIcon: ({ size, color, focused }) => (
                        <Ionicons color={focused ? color : themeColorForeground} name="home-outline" size={size} />
                    ),
                    drawerLabel: ({ color, focused }) => (
                        <Text style={{ color: focused ? color : themeColorForeground }}>Home</Text>
                    ),
                    headerTitle: "Home",
                }}
            />
            <Drawer.Screen
                name="(tabs)"
                options={{
                    drawerIcon: ({ size, color, focused }) => (
                        <MaterialIcons
                            color={focused ? color : themeColorForeground}
                            name="border-bottom"
                            size={size}
                        />
                    ),
                    drawerLabel: ({ color, focused }) => (
                        <Text style={{ color: focused ? color : themeColorForeground }}>Tabs</Text>
                    ),
                    headerRight: () => (
                        <Link asChild href="/modal">
                            <Pressable className="mr-4">
                                <Ionicons color={themeColorForeground} name="add-outline" size={24} />
                            </Pressable>
                        </Link>
                    ),
                    headerTitle: "Tabs",
                }}
            />
        </Drawer>
    );
}

export default DrawerLayout;
