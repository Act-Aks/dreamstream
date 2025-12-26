import { Button, Card } from 'heroui-native'
import { View } from 'react-native'
import { Container } from '@/components/atoms/Container/Container'
import { ToastHooks } from '@/hooks/toast/toast.hooks'

export const HomeScreen: React.FC = () => {
    const { toast } = ToastHooks.useAppToast()

    return (
        <Container>
            <View className='flex-1 items-center justify-center'>
                <Card className='items-center p-8' variant='secondary'>
                    <Card.Title className='mb-2 text-3xl'>Tab One</Card.Title>
                    <Card.Description className='text-xl'>This is the first tab.</Card.Description>
                    <Button
                        className='mt-4'
                        onPress={() => {
                            toast('success')('Hi its me')
                        }}
                        variant='secondary'
                    >
                        Press me
                    </Button>
                </Card>
            </View>
        </Container>
    )
}
