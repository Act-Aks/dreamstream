import type { Provider } from '@dreamstream/common/types'
import { create } from 'zustand'
import { createJSONStorage, persist } from 'zustand/middleware'
import { AppStorage } from '@/utils/mmkvStorage'

export interface Providers {
    activeProvider: Provider | null
    providers: Provider[]
    setActiveProvider: (provider: Provider | null) => void
    setProviders: (providers: Provider[]) => void
}

export const useProvidersStore = create<Providers>()(
    persist(
        (set) => ({
            activeProvider: null,
            providers: [],
            setActiveProvider: (activeProvider) => set({ activeProvider }),
            setProviders: (providers) => set({ providers }),
        }),
        {
            name: 'content-storage',
            partialize: (state) => ({
                activeProvider: state.activeProvider,
                providers: state.providers,
            }),
            storage: createJSONStorage(() => AppStorage.createMmkvStorage('providers')),
        }
    )
)
