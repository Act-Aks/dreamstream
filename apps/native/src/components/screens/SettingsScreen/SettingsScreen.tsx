import { Ionicons } from '@expo/vector-icons'
import { Button, Card, PressableFeedback, useThemeColor } from 'heroui-native'
import { Switch, Text, View } from 'react-native'
import { Container } from '@/components/atoms/Container/Container'
import { useAppTheme } from '@/contexts/ThemeContext'

interface SettingsSectionProps {
    title: string
    icon: keyof typeof Ionicons.glyphMap
    onPress?: () => void
    rightElement?: React.ReactNode
    description?: string
}

const SettingsSection: React.FC<SettingsSectionProps> = ({ title, icon, onPress, rightElement, description }) => {
    const foregroundColor = useThemeColor('foreground')
    const surfaceColor = useThemeColor('surface')

    const content = (
        <View className='flex-row items-center justify-between p-4'>
            <View className='flex-1 flex-row items-center'>
                <View className='mr-3'>
                    <Ionicons color={foregroundColor} name={icon} size={24} />
                </View>
                <View className='flex-1'>
                    <Text className='font-medium text-base text-foreground'>{title}</Text>
                    {description && (
                        <Text className='mt-1 text-sm opacity-70' style={{ color: foregroundColor }}>
                            {description}
                        </Text>
                    )}
                </View>
            </View>
            {rightElement && <View className='ml-3'>{rightElement}</View>}
            {onPress && !rightElement && <Ionicons color={surfaceColor} name='chevron-forward' size={20} />}
        </View>
    )

    if (onPress && !rightElement) {
        return (
            <PressableFeedback className='overflow-hidden rounded-lg' onPress={onPress}>
                {content}
            </PressableFeedback>
        )
    }

    return <View className='overflow-hidden rounded-lg'>{content}</View>
}

interface SettingsGroupProps {
    title: string
    children: React.ReactNode
}

const SettingsGroup: React.FC<SettingsGroupProps> = ({ title, children }) => {
    const foregroundColor = useThemeColor('foreground')

    return (
        <View className='mb-6'>
            <Text
                className='mb-3 px-2 font-semibold text-sm uppercase tracking-wide opacity-70'
                style={{ color: foregroundColor }}
            >
                {title}
            </Text>
            <Card className='overflow-hidden' variant='secondary'>
                {children}
            </Card>
        </View>
    )
}

const Divider: React.FC = () => {
    const surfaceColor = useThemeColor('surface')

    return <View className='mx-4 h-px' style={{ backgroundColor: surfaceColor, opacity: 0.3 }} />
}

export const SettingsScreen: React.FC = () => {
    const { currentTheme, toggleTheme, isDark } = useAppTheme()
    const accentColor = useThemeColor('accent')
    const surfaceColor = useThemeColor('surface')
    const foregroundColor = useThemeColor('foreground')

    const handleAccountPress = (): void => {
        // Navigate to account settings
    }

    const handleNotificationsPress = (): void => {
        // Navigate to notification settings
    }

    const handlePrivacyPress = (): void => {
        // Navigate to privacy settings
    }

    const handleStoragePress = (): void => {
        // Navigate to storage settings
    }

    const handleDownloadsPress = (): void => {
        // Navigate to downloads settings
    }

    const handlePlaybackPress = (): void => {
        // Navigate to playback settings
    }

    const handleLanguagePress = (): void => {
        // Navigate to language settings
    }

    const handleAboutPress = (): void => {
        // Navigate to about screen
    }

    const handleHelpPress = (): void => {
        // Navigate to help screen
    }

    const handleFeedbackPress = (): void => {
        // Navigate to feedback screen
    }

    const handleSignOut = (): void => {
        // Handle sign out
    }

    return (
        <Container className='p-4'>
            <View className='mb-6'>
                <Text className='mb-2 font-bold text-2xl text-foreground'>Settings</Text>
                <Text className='text-foreground opacity-70'>Customize your app experience and preferences</Text>
            </View>

            {/* Account & Profile */}
            <SettingsGroup title='Account & Profile'>
                <SettingsSection
                    description='Manage your account and profile'
                    icon='person-circle-outline'
                    onPress={handleAccountPress}
                    title='Account'
                />
                <Divider />
                <SettingsSection
                    description='Configure notification preferences'
                    icon='notifications-outline'
                    onPress={handleNotificationsPress}
                    title='Notifications'
                />
                <Divider />
                <SettingsSection
                    description='Control your privacy settings'
                    icon='shield-checkmark-outline'
                    onPress={handlePrivacyPress}
                    title='Privacy & Security'
                />
            </SettingsGroup>

            {/* App Preferences */}
            <SettingsGroup title='App Preferences'>
                <SettingsSection
                    description={`Currently using ${currentTheme} theme`}
                    icon='color-palette-outline'
                    rightElement={
                        <Switch
                            accessibilityLabel='Toggle dark mode'
                            onValueChange={toggleTheme}
                            thumbColor={foregroundColor}
                            trackColor={{ false: surfaceColor, true: accentColor }}
                            value={isDark}
                        />
                    }
                    title='Theme'
                />
                <Divider />
                <SettingsSection
                    description='Change app language'
                    icon='language-outline'
                    onPress={handleLanguagePress}
                    title='Language'
                />
                <Divider />
                <SettingsSection
                    description='Video and audio settings'
                    icon='play-circle-outline'
                    onPress={handlePlaybackPress}
                    title='Playback'
                />
            </SettingsGroup>

            {/* Storage & Downloads */}
            <SettingsGroup title='Storage & Downloads'>
                <SettingsSection
                    description='Manage app storage and cache'
                    icon='folder-outline'
                    onPress={handleStoragePress}
                    title='Storage'
                />
                <Divider />
                <SettingsSection
                    description='Download preferences and location'
                    icon='download-outline'
                    onPress={handleDownloadsPress}
                    title='Downloads'
                />
            </SettingsGroup>

            {/* Support & Info */}
            <SettingsGroup title='Support & Info'>
                <SettingsSection
                    description='Get help and contact support'
                    icon='help-circle-outline'
                    onPress={handleHelpPress}
                    title='Help & Support'
                />
                <Divider />
                <SettingsSection
                    description='Share your thoughts with us'
                    icon='chatbubble-outline'
                    onPress={handleFeedbackPress}
                    title='Send Feedback'
                />
                <Divider />
                <SettingsSection
                    description='App version and legal information'
                    icon='information-circle-outline'
                    onPress={handleAboutPress}
                    title='About'
                />
            </SettingsGroup>

            {/* Sign Out */}
            <View className='mt-4 mb-8'>
                <Button className='w-full' onPress={handleSignOut} variant='danger'>
                    <Button.Label>Sign Out</Button.Label>
                </Button>
            </View>
        </Container>
    )
}
