import { Text, View } from "react-native";

import { Container } from "@/src/components/container";

export default function Home() {
    return (
        <Container className="p-6">
            <View className="mb-6 py-4">
                <Text className="mb-2 font-bold text-4xl text-foreground">BETTER T STACK</Text>
            </View>
        </Container>
    );
}
