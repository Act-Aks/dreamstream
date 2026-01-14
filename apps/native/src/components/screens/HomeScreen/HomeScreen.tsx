import { AnimeKai } from '@dreamstream/providers'
import type React from 'react'
import { Container } from '@/components/atoms/Container/Container'
import { ScrollableMediaCards } from '@/components/organisms/ScrollableMediaCards/ScrollableMediaCards'

const animekai = new AnimeKai()

export const HomeScreen: React.FC = () => {
    return (
        <Container>
            <ScrollableMediaCards
                fetchFn={(page) => animekai.fetchRecentlyAdded(page)}
                sectionKey='recently-added'
                title='Recently Added'
            />
            <ScrollableMediaCards
                fetchFn={(page) => animekai.fetchRecentlyUpdated(page)}
                sectionKey='recently-updated'
                title='Recently Updated'
            />
            <ScrollableMediaCards
                fetchFn={(page) => animekai.fetchLatestCompleted(page)}
                sectionKey='completed'
                title='Latest Completed'
            />
        </Container>
    )
}
