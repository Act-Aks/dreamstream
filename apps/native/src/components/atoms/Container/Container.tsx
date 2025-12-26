import { cn } from 'heroui-native'
import type { PropsWithChildren } from 'react'
import { ScrollView, type ViewProps } from 'react-native'
import Animated, { type AnimatedProps } from 'react-native-reanimated'
import { useSafeAreaInsets } from 'react-native-safe-area-context'

export const Container: React.FC<PropsWithChildren<AnimatedProps<ViewProps>>> = ({ children, className, ...props }) => {
    const insets = useSafeAreaInsets()

    return (
        <Animated.View
            className={cn('flex-1 bg-background', className) ?? ''}
            style={{ paddingBottom: insets.bottom }}
            {...props}
        >
            <ScrollView automaticallyAdjustKeyboardInsets contentContainerStyle={{ flexGrow: 1 }}>
                {children}
            </ScrollView>
        </Animated.View>
    )
}
