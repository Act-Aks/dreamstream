import Ionicons from '@expo/vector-icons/Ionicons'
import { PressableFeedback, TextField, useThemeColor } from 'heroui-native'
import { useReducer, useRef } from 'react'
import { type TextInput, useWindowDimensions, View } from 'react-native'
import Animated, { Easing, LinearTransition } from 'react-native-reanimated'

const AnimatedTextField = Animated.createAnimatedComponent(TextField)
const AnimatedPressableFeedback = Animated.createAnimatedComponent(PressableFeedback)

export const SearchBar: React.FC = () => {
    const [isFocused, toggleFocus] = useReducer((focus) => !focus, false)
    const { width } = useWindowDimensions()
    const inputRef = useRef<TextInput>(null)

    const themeColorForeground = useThemeColor('foreground')
    const themeColorSurface = useThemeColor('surface')

    function onPressSearch() {
        if (isFocused) {
            inputRef.current?.clear()
            inputRef.current?.blur()
        } else {
            inputRef.current?.focus()
        }
        toggleFocus()
    }

    return (
        <AnimatedPressableFeedback
            className={'flex flex-row items-center justify-center rounded-full'}
            onPress={onPressSearch}
            style={[
                {
                    backgroundColor: isFocused ? themeColorSurface : themeColorSurface,
                    transitionDuration: 500,
                    transitionProperty: ['width', 'backgroundColor'],
                    width: isFocused ? width * 0.75 : 56,
                },
            ]}
        >
            <View className='px-4'>
                <Ionicons color={themeColorForeground} name='search' size={24} />
            </View>
            <AnimatedTextField className={'flex-1'} layout={LinearTransition.easing(Easing.linear)}>
                <TextField.Input
                    animation={{
                        backgroundColor: {
                            value: {
                                blur: themeColorSurface,
                                error: themeColorSurface,
                                focus: themeColorSurface,
                            },
                        },
                    }}
                    autoCapitalize='none'
                    className='rounded-none border-0'
                    placeholder='Search content to stream'
                    ref={inputRef}
                />
            </AnimatedTextField>
        </AnimatedPressableFeedback>
    )
}
