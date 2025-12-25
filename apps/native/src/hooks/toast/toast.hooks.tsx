import { Toast, type ToastVariant, useToast } from 'heroui-native'
import { useCallback } from 'react'

type ToastType = 'error' | 'info' | 'success' | 'warning'

const mapToastVariants = {
    error: 'danger',
    info: 'accent',
    success: 'success',
    warning: 'warning',
} as const satisfies Record<ToastType, ToastVariant>

function useAppToast() {
    const { toast } = useToast()

    return {
        toast: useCallback(
            (type: 'success' | 'error' | 'warning' | 'info') => (message: string) =>
                toast.show({
                    component: (props) => (
                        <Toast
                            variant={mapToastVariants[type]}
                            {...props}
                            className='flex items-center justify-center text-center'
                        >
                            <Toast.Title>{message}</Toast.Title>
                        </Toast>
                    ),
                }),
            [toast]
        ),
    }
}

export const ToastHooks = {
    useAppToast,
}
