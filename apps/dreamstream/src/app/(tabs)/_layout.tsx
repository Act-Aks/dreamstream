import { Ionicons } from "@expo/vector-icons";
import { Tabs } from "expo-router";

export default function TabsLayout() {
    return (
        <Tabs screenOptions={{ headerShown: false }}>
            <Tabs.Screen
                name={"index"}
                options={{
                    title: "Home",
                    tabBarIcon: ({ color, size }) => <Ionicons color={color} name="home" size={size} />,
                }}
            />
            <Tabs.Screen
                name={"search"}
                options={{
                    title: "Search",
                    tabBarIcon: ({ color, size }) => <Ionicons color={color} name="search" size={size} />,
                }}
            />
            <Tabs.Screen
                name={"downloads"}
                options={{
                    title: "Downloads",
                    tabBarIcon: ({ color, size }) => <Ionicons color={color} name="download" size={size} />,
                }}
            />
            <Tabs.Screen
                name={"settings"}
                options={{
                    title: "Settings",
                    tabBarIcon: ({ color, size }) => <Ionicons color={color} name="settings" size={size} />,
                }}
            />
        </Tabs>
    );
}
