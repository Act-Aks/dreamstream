import type { HeroUINativeConfig } from 'heroui-native'
import { KeyboardAvoidingView } from 'react-native-keyboard-controller'

export const heroNativeUiConfig: HeroUINativeConfig = {
    textProps: {
        allowFontScaling: true,
        maxFontSizeMultiplier: 1.5,
    },
    toast: {
        contentWrapper: (children) => (
            <KeyboardAvoidingView
                behavior={'padding'}
                className='flex-1'
                keyboardVerticalOffset={24}
                pointerEvents='box-none'
            >
                {children}
            </KeyboardAvoidingView>
        ),
        defaultProps: {
            placement: 'bottom',
            variant: 'default',
        },
        insets: {
            bottom: 60,
            left: 16,
            right: 16,
            top: 0,
        },
        maxVisibleToasts: 3,
    },
}
