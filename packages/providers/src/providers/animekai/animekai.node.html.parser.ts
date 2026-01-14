import {
    type AnimeInfo,
    AnimeParser,
    type AnimeResult,
    assertCondition,
    type EpisodeServer,
    type Intro,
    type MediaFormat,
    type MediaStatus,
    mediaStatus,
    parseIntSafe,
    type Search,
    type Season,
    type Source,
    type StreamingServers,
    type SubOrDub,
    streamingServers,
    subOrDub,
    toLowerCase,
} from '@dreamstream/core'
import { MegaUp } from '@dreamstream/extractors'
import { type HTMLElement, parse } from 'node-html-parser'
import { parseScheduleDate, type ScheduleOptions } from './animekai.static'

const megaUpExtractor = new MegaUp()

/**
 * Normalizes whitespace in a text string by collapsing multiple spaces and trimming.
 */
const normalizeText = (text?: string | null): string => (text ?? '').replace(/\s+/g, ' ').trim()

/**
 * Safely extracts and normalizes text content from a node.
 */
const textOf = (node?: HTMLElement | null): string => normalizeText(node?.textContent)

/**
 * AnimeKai provider implemented with node-html-parser.
 */
export class AnimeKai extends AnimeParser {
    private static BASE_URL = 'https://anikai.to'

    override readonly name = 'AnimeKai'

    protected override baseUrl = 'https://anikai.to'
    protected override classPath = 'ANIME.AnimeKai'
    protected override logo =
        'https://anikai.to//assets/uploads/37585a39fe8c8d8fafaa2c7bfbf5374ecac859ea6a0288a6da2c61f5.png'

    private static readonly BACKGROUND_IMAGE_REGEX = /background-image:\s*url\(["']?(.+?)["']?\)/i

    /**
     * Ensures page number is at least 1.
     */
    private getPage(page: number): number {
        return page <= 0 ? 1 : page
    }

    constructor() {
        super(AnimeKai.BASE_URL)
    }

    // ========== Public listing/search APIs ==========

    /**
     * Search for anime by keyword.
     */
    override search(query: string, page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(
            `${this.baseUrl}/browser?keyword=${query.replace(/[\W_]+/g, '+')}&page=${this.getPage(page)}`
        )
    }

    /**
     * Fetch list of completed anime.
     */
    fetchLatestCompleted(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/completed?page=${this.getPage(page)}`)
    }

    /**
     * Fetch recently added anime.
     */
    fetchRecentlyAdded(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/recent?page=${this.getPage(page)}`)
    }

    /**
     * Fetch recently updated anime.
     */
    fetchRecentlyUpdated(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/updates?page=${this.getPage(page)}`)
    }

    /**
     * Fetch newly released anime.
     */
    fetchNewReleases(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/new-releases?page=${this.getPage(page)}`)
    }

    /**
     * Fetch anime movies.
     */
    fetchMovie(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/movie?page=${this.getPage(page)}`)
    }

    /**
     * Fetch TV anime.
     */
    fetchTV(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/tv?page=${this.getPage(page)}`)
    }

    /**
     * Fetch OVA anime.
     */
    fetchOVA(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/ova?page=${this.getPage(page)}`)
    }

    /**
     * Fetch ONA anime.
     */
    fetchONA(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/ona?page=${this.getPage(page)}`)
    }

    /**
     * Fetch special anime.
     */
    fetchSpecial(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/special?page=${this.getPage(page)}`)
    }

    /**
     * Fetch all genre names from the home page sidebar.
     */
    async fetchGenres(): Promise<string[]> {
        try {
            const data = await this.client.get<string>(`${this.baseUrl}/home`, { headers: this.headers() })
            const root = parse(data)

            const res: string[] = []
            const sideBar = root.querySelector('#menu')
            if (!sideBar) {
                return res
            }

            const links = sideBar.querySelectorAll('ul.c4 li a')
            for (const ele of links) {
                const text = ele.textContent
                if (text) {
                    res.push(text.toLowerCase())
                }
            }

            return res
        } catch (error) {
            this.throwError(error, 'Genre fetch error')
        }
    }

    /**
     * Fetch anime for a given genre.
     */
    genreSearch(genre: string, page = 1): Promise<Search<AnimeResult>> {
        if (genre === '') {
            this.throwError('Genre is empty')
        }
        return this.scrapeCardPage(`${this.baseUrl}/genres/${genre}?page=${this.getPage(page)}`)
    }

    // ========== Schedule / Spotlight / Suggestions ==========

    /**
     * Fetch schedule entries for a given date and timezone.
     */
    async fetchSchedule({ date, timezone = 5.5 }: ScheduleOptions): Promise<Search<AnimeResult>> {
        try {
            const targetDate = parseScheduleDate(date)
            const epochTime = Math.floor(targetDate.getTime() / 1000)
            const data = await this.client.get(`${this.baseUrl}/ajax/schedule/items?tz=${timezone}&time=${epochTime}`, {
                headers: this.headers(),
            })

            const htmlContent = (() => {
                // @ts-expect-error
                const resultData = data?.result ?? data
                if (typeof resultData === 'object' && !!resultData && 'html' in resultData) {
                    return resultData.html
                }
                return resultData
            })()

            const root = parse(htmlContent)
            const res: Search<AnimeResult> = { results: [] }
            const items = root.querySelectorAll('ul li')

            for (const card of items) {
                res.results.push(this.parseScheduleItem(card))
            }

            return res
        } catch (error) {
            this.throwError(error, 'Schedule fetch error')
        }
    }

    /**
     * Fetch spotlight (hero/slider) entries from the home page.
     */
    async fetchSpotlight(): Promise<Search<AnimeResult>> {
        try {
            const data = await this.client.get<string>(`${this.baseUrl}/home`, { headers: this.headers() })
            const root = parse(data)

            const res: Search<AnimeResult> = { results: [] }
            const slides = root.querySelectorAll('div.swiper-wrapper > div.swiper-slide')

            for (const card of slides) {
                res.results.push(this.parseSpotlightCard(card))
            }

            return res
        } catch (error) {
            this.throwError(error, 'Spotlight fetch error')
        }
    }

    /**
     * Fetch search suggestion cards for a query (used in autocomplete).
     */
    async fetchSearchSuggestions(query: string): Promise<Search<AnimeResult>> {
        try {
            const res: Search<AnimeResult> = { results: [] }
            const data = await this.client.get<{ result: { html: string } }>(
                `${this.baseUrl}/ajax/anime/search?keyword=${query.replace(/[\W_]+/g, '+')}`,
                { headers: this.headers() }
            )
            const root = parse(data.result.html)

            const items = root.querySelectorAll('a.aitem')
            for (const card of items) {
                res.results.push(this.parseSuggestionCard(card))
            }

            return res
        } catch (error) {
            this.throwError(error, 'Search suggestions fetch error')
        }
    }

    // ========== Anime info & episodes ==========

    /**
     * Fetch full anime info (details, recommendations, relations, episodes).
     */
    override fetchAnimeInfo = async (id: string): Promise<AnimeInfo> => {
        const info: AnimeInfo = { id, title: '' }
        try {
            const html = await this.client.get<string>(`${this.baseUrl}/watch/${id}`, {
                headers: this.headers(),
            })
            const root = parse(html)

            this.parseAnimeInfoPage(root, id, info)

            const aniId = root.querySelector('.rate-box#anime-rating')?.getAttribute('data-id')
            assertCondition(aniId, 'Ani Id not found')

            const episodesAjax = await this.client.get<{ result: string }>(
                `${this.baseUrl}/ajax/episodes/list?ani_id=${aniId}&_=${await megaUpExtractor.generateToken(
                    aniId ?? ''
                )}`,
                {
                    headers: {
                        ...this.headers(),
                        Referer: `${this.baseUrl}/watch/${id}`,
                        'X-Requested-With': 'XMLHttpRequest',
                    },
                }
            )

            const episodesRoot = parse(episodesAjax.result)
            this.parseEpisodesHtml(root, episodesRoot, info)

            return info
        } catch (error) {
            this.throwError(error, 'Anime info fetch error')
        }
    }

    /**
     * Resolve episode sources, following redirect to the extractor if needed.
     */
    override fetchEpisodeSources = async (
        episodeId: string,
        server: StreamingServers = streamingServers.MegaUp,
        subbedOrDubbed: SubOrDub = subOrDub.SUB
    ): Promise<Source> => {
        // Direct extractor URL path.
        if (episodeId.startsWith('http')) {
            const serverUrl = new URL(episodeId)
            return {
                headers: { Referer: serverUrl.href },
                ...(await megaUpExtractor.extract(serverUrl)),
                // biome-ignore lint/performance/useTopLevelRegex: Ignore here
                download: serverUrl.href.replace(/\/e\//, '/download/'),
            }
        }

        try {
            const servers = await this.fetchEpisodeServers(episodeId, subbedOrDubbed)
            const i = servers.findIndex((s) => s.name.toLowerCase().includes(server))

            if (i === -1) {
                throw new Error(`Server ${server} not found`)
            }

            const selectedServer = servers[i]
            assertCondition(!!selectedServer, 'Server not selected')

            const serverUrl: URL = new URL(selectedServer.url)
            const sources = await this.fetchEpisodeSources(serverUrl.href, server, subbedOrDubbed)

            const { intro, outro } = selectedServer ?? {}
            sources.intro = intro as Intro
            sources.outro = outro as Intro

            return sources
        } catch (error) {
            this.throwError(error, 'Episode sources fetch error')
        }
    }

    /**
     * Fetch episode server list (MegaUp) for an episode.
     */
    override async fetchEpisodeServers(
        episodeId: string,
        subbedOrDubbed: SubOrDub = subOrDub.SUB
    ): Promise<EpisodeServer[]> {
        const ajaxEpisodeId = await (async () => {
            if (!episodeId.startsWith(`${this.baseUrl}/ajax`)) {
                const splitToken = episodeId.split('$token=')[1]
                assertCondition(splitToken, 'Episode id should have `$token`')
                const token = await megaUpExtractor.generateToken(splitToken)
                return `${this.baseUrl}/ajax/links/list?token=${episodeId.split('$token=')[1]}&_=${token}`
            }
            return episodeId
        })()

        try {
            const data = await this.client.get<{ result: string }>(ajaxEpisodeId, { headers: this.headers() })
            const root = parse(data.result)
            const servers: EpisodeServer[] = []
            const subOrDubStr = subbedOrDubbed === subOrDub.SUB ? 'softsub' : 'dub'
            const serverItems = root.querySelectorAll(`.server-items.lang-group[data-id="${subOrDubStr}"] .server`)

            for (const server of serverItems) {
                const id = server.getAttribute('data-lid')
                assertCondition(id, 'ID is no available')
                const viewData = await this.client.get<{ result: string }>(
                    `${this.baseUrl}/ajax/links/view?id=${id}&_=${await megaUpExtractor.generateToken(id ?? '')}`,
                    { headers: this.headers() }
                )
                const decodedIframeData = await megaUpExtractor.decodeIframeData(viewData.result)
                servers.push({
                    intro: {
                        end: decodedIframeData?.skip.intro[1],
                        start: decodedIframeData?.skip.intro[0],
                    },
                    name: toLowerCase(`MegaUp ${normalizeText(server.textContent)}`),
                    outro: {
                        end: decodedIframeData?.skip.outro[1],
                        start: decodedIframeData?.skip.outro[0],
                    },
                    url: decodedIframeData.url,
                })
            }

            return servers
        } catch (error) {
            this.throwError(error, 'Get episode servers error')
        }
    }

    // ========== Scrape card pages (listing) ==========

    /**
     * Fetch and parse a paginated listing page that renders anime cards.
     */
    private async scrapeCardPage(url: string): Promise<Search<AnimeResult>> {
        try {
            const res: Search<AnimeResult> = {
                currentPage: 0,
                hasNextPage: false,
                results: [],
                totalPages: 0,
            }

            const data = await this.client.get<string>(url, { headers: this.headers() })
            const root = parse(data)

            const pagination = root.querySelector('ul.pagination')

            if (pagination) {
                const activePageText =
                    pagination.querySelector('.page-item.active span.page-link')?.textContent?.trim() ?? ''
                res.currentPage = parseIntSafe(activePageText)

                const activeItem = pagination.querySelector('.page-item.active')
                const nextItem = activeItem?.nextElementSibling
                const nextHref = nextItem?.querySelector('a.page-link')?.getAttribute('href')
                const nextPage = nextHref?.split('page=')[1]
                res.hasNextPage = !!nextPage && nextPage !== ''

                const lastHref = pagination.querySelector('.page-item:last-child a.page-link')?.getAttribute('href')
                const totalPages = lastHref?.split('page=')[1]
                res.totalPages = !totalPages || totalPages === '' ? res.currentPage : parseIntSafe(totalPages)
            }

            res.results = this.parseCardList(root)

            if (res.results.length === 0) {
                res.currentPage = 0
                res.hasNextPage = false
                res.totalPages = 0
            }

            return res
        } catch (error) {
            this.throwError(error, `ScrapeCardPage error for URL: ${url}, Error`)
        }
    }

    // ========== Parsing helpers (alphabetical) ==========

    /**
     * Extracts genres from an info container, splitting the last child text by comma.
     */
    private extractGenresFromInfo(info: HTMLElement | null): string[] {
        if (!info) {
            return []
        }
        const children = info.querySelectorAll('*')
        const lastChild = children.at(-1)
        const text = normalizeText(lastChild?.textContent)
        if (!text) {
            return []
        }
        return text.split(',').map((genre) => genre.trim())
    }

    /**
     * Parses the main anime info section from the watch page into the AnimeInfo object.
     */
    private parseAnimeInfoPage(root: HTMLElement, id: string, info: AnimeInfo): void {
        const titleNode = root.querySelector('.entity-scroll > .title') ?? root.querySelector('.entity-scroll .title')

        const rawTitle = titleNode?.textContent ?? ''
        info.title = normalizeText(rawTitle)
        // biome-ignore lint/complexity/useLiteralKeys: This is an extra field
        info['japaneseTitle'] = titleNode?.getAttribute('data-jp')?.trim() ?? null

        info.image = root.querySelector('div.poster > div > img')?.getAttribute('src') ?? ''
        info.description = textOf(root.querySelector('.entity-scroll > .desc'))

        const infoContainer = root.querySelector('.entity-scroll > .info')
        if (infoContainer) {
            const children = infoContainer.querySelectorAll('*')
            const lastChild = children.at(-1)
            info.type = normalizeText(lastChild?.textContent).toUpperCase() as MediaFormat
        } else {
            info.type = '' as MediaFormat
        }

        info.url = `${this.baseUrl}/watch/${id}`

        info.recommendations = this.parseRecommendations(root)
        info.relations = this.parseRelations(root)

        info.hasSub = !!infoContainer?.querySelector('span.sub')
        info.hasDub = !!infoContainer?.querySelector('span.dub')

        info.genres = this.parseGenres(root)
        const { status, season } = this.parseStatusAndSeason(root)
        info.status = status
        info.season = season as Season
    }

    /**
     * Parses a single anime card (listing card) into an AnimeResult.
     */
    private parseCard(card: HTMLElement): AnimeResult | null {
        const inner = card.querySelector('div.inner')
        const atag = inner?.querySelector('a[href^="/watch/"]') ?? card.querySelector('a[href^="/watch/"]')
        if (!atag) {
            return null
        }

        const href = atag.getAttribute('href') ?? ''
        const id = href.replace('/watch/', '')

        // Use the dedicated title anchor if present, otherwise fall back to atag text.
        const titleAnchor = card.querySelector('a.title')
        const title = normalizeText(titleAnchor?.textContent ?? atag.textContent ?? '')

        const info = card.querySelector('.info')
        const infoChildren = (info?.childNodes ?? []).filter((n) => n.nodeType === 1) as HTMLElement[]

        const lastInfoChild = infoChildren.at(-1)
        const typeText = normalizeText(lastInfoChild?.textContent)

        const secondLastInfoChild = infoChildren.at(-2)
        const episodesTextRaw =
            normalizeText(secondLastInfoChild?.textContent) || info?.querySelector('span.sub')?.textContent || ''
        const episodes = parseIntSafe(episodesTextRaw)

        const imageNode = card.querySelector('img')
        const image = imageNode?.getAttribute('data-src') ?? imageNode?.getAttribute('src') ?? ''

        const jpTitleNode = card.querySelector('a.title')
        const japaneseTitle = jpTitleNode?.getAttribute('data-jp')?.trim()

        const sub = parseIntSafe(info?.querySelector('span.sub')?.textContent)
        const dub = parseIntSafe(info?.querySelector('span.dub')?.textContent)

        return {
            dub,
            episodes,
            id: id ?? '',
            image,
            japaneseTitle,
            sub,
            title,
            type: typeText as MediaFormat,
            url: `${this.baseUrl}${href}`,
        }
    }

    /**
     * Parses all anime cards in a listing page.
     */
    private parseCardList(root: HTMLElement): AnimeResult[] {
        const results: AnimeResult[] = []
        const cards = root.querySelectorAll('.aitem')

        for (const card of cards) {
            const parsed = this.parseCard(card)
            if (parsed) {
                results.push(parsed)
            }
        }

        return results
    }

    /**
     * Parses all episode list HTML into the AnimeInfo episodes array.
     */
    private parseEpisodesHtml(root: HTMLElement, episodesRoot: HTMLElement, info: AnimeInfo): void {
        const episodeLis = episodesRoot.querySelectorAll('div.eplist > ul > li')
        info.totalEpisodes = episodeLis.length

        info.episodes = []
        const episodeLinks = episodesRoot.querySelectorAll('div.eplist > ul > li > a')

        const subCount = parseIntSafe(root.querySelector('.entity-scroll > .info > span.sub')?.textContent?.trim())
        const dubCount = parseIntSafe(root.querySelector('.entity-scroll > .info > span.dub')?.textContent?.trim())

        for (const el of episodeLinks) {
            const numAttr = el.getAttribute('num')
            const number = parseIntSafe(numAttr)
            const title = textOf(el.querySelector('span'))
            const href = el.getAttribute('href') ?? ''
            const url = `${this.baseUrl}/watch/${info.id}${href}ep=${numAttr}`
            const isFiller = el.classList.contains('filler')
            const isSubbed = number <= subCount
            const isDubbed = number <= dubCount

            const episodeId = `${info.id}$ep=${numAttr}$token=${el.getAttribute('token')}`

            info.episodes.push({
                id: episodeId,
                isDubbed,
                isFiller,
                isSubbed,
                number,
                title,
                url,
            })
        }
    }

    /**
     * Parses genre strings from the detail section of the info page.
     */
    private parseGenres(root: HTMLElement): string[] {
        const genres: string[] = []
        const genreDivs = root.querySelectorAll('.entity-scroll > .detail div:contains("Genres")')
        for (const div of genreDivs) {
            const genre = normalizeText(div.textContent)
            if (genre) {
                genres.push(genre)
            }
        }
        return genres
    }

    /**
     * Parses recommended anime cards from the sidebar.
     */
    private parseRecommendations(root: HTMLElement): AnimeResult[] {
        const results: AnimeResult[] = []
        const recItems = root.querySelectorAll('section.sidebar-section:not(#related-anime) .aitem-col .aitem')

        for (const aTag of recItems) {
            const animeId = aTag.getAttribute('href')?.replace('/watch/', '')
            const recInfo = aTag.querySelector('.info')
            const recChildren = recInfo?.querySelectorAll('*') ?? []

            const episodesText =
                normalizeText(recChildren.at(-2)?.textContent) ?? recInfo?.querySelector('span.sub')?.textContent ?? ''

            results.push({
                dub: parseIntSafe(recInfo?.querySelector('span.dub')?.textContent),
                episodes: parseIntSafe(episodesText),
                id: animeId ?? '',
                image: aTag.getAttribute('style')?.match(AnimeKai.BACKGROUND_IMAGE_REGEX)?.[1] ?? '',
                japaneseTitle: aTag.querySelector('.title')?.getAttribute('data-jp')?.trim(),
                sub: parseIntSafe(recInfo?.querySelector('span.sub')?.textContent),
                title: textOf(aTag.querySelector('.title')),
                type: normalizeText(recChildren.at(-1)?.textContent) as MediaFormat,
                url: `${this.baseUrl}${aTag.getAttribute('href')}`,
            })
        }

        return results
    }

    /**
     * Parses relation cards (prequel/sequel/etc.) from the related-anime section.
     */
    private parseRelations(root: HTMLElement): AnimeResult[] {
        const results: AnimeResult[] = []
        const relationCards = root.querySelectorAll('section#related-anime .tab-body .aitem-col')

        for (const card of relationCards) {
            const aTag = card.querySelector('a.aitem')
            const animeId = aTag?.getAttribute('href')?.replace('/watch/', '')
            const relInfo = card.querySelector('.info')
            const relChildren = relInfo?.querySelectorAll('*') ?? []

            const episodesText =
                normalizeText(relChildren.at(-3)?.textContent) ?? relInfo?.querySelector('span.sub')?.textContent ?? ''

            results.push({
                dub: parseIntSafe(relInfo?.querySelector('span.dub')?.textContent),
                episodes: parseIntSafe(episodesText),
                id: animeId ?? '',
                image: aTag?.getAttribute('style')?.match(AnimeKai.BACKGROUND_IMAGE_REGEX)?.[1] ?? '',
                japaneseTitle: aTag?.querySelector('.title')?.getAttribute('data-jp')?.trim(),
                relationType: normalizeText(relChildren.at(-1)?.textContent) ?? '',
                sub: parseIntSafe(relInfo?.querySelector('span.sub')?.textContent),
                title: textOf(aTag?.querySelector('.title') ?? null),
                type: normalizeText(relChildren.at(-2)?.textContent) as MediaFormat,
                url: `${this.baseUrl}${aTag?.getAttribute('href')}`,
            })
        }

        return results
    }

    /**
     * Parses a single schedule list item into an AnimeResult-like object.
     */
    private parseScheduleItem(card: HTMLElement): AnimeResult {
        const titleElement = card.querySelector('span.title')
        const spans = card.querySelectorAll('span')
        const lastSpan = spans.at(-1)
        const episodeText = normalizeText(lastSpan?.textContent)

        return {
            airingEpisode: episodeText.replace('EP ', ''),
            airingTime: textOf(card.querySelector('span.time')),
            id: card.querySelector('a')?.getAttribute('href')?.split('/')[2] ?? '',
            japaneseTitle: titleElement?.getAttribute('data-jp') ?? undefined,
            title: textOf(titleElement),
        }
    }

    /**
     * Parses a spotlight slide card into an AnimeResult.
     */
    private parseSpotlightCard(card: HTMLElement): AnimeResult {
        const titleElement = card.querySelector('div.detail > p.title')
        const btn = card.querySelector('div.swiper-ctrl > a.btn')
        const id = btn?.getAttribute('href')?.replace('/watch/', '')
        const img = card.getAttribute('style')

        const info = card.querySelector('div.detail > div.info')
        const genres = this.extractGenresFromInfo(info)

        const qualitySpan = card.querySelector('div.detail > div.mics >div:contains("Quality") span')
        const releaseSpan = card.querySelector('div.detail > div.mics >div:contains("Release") span')

        const infoChildren = info?.querySelectorAll('*') ?? []
        const typeNode = infoChildren.at(-2)
        const typeText = normalizeText(typeNode?.textContent)

        return {
            banner: img?.match(AnimeKai.BACKGROUND_IMAGE_REGEX)?.[1] ?? null,
            description: textOf(card.querySelector('div.detail > p.desc')),
            dub: parseIntSafe(info?.querySelector('span.dub')?.textContent),
            genres,
            id: id ?? '',
            japaneseTitle: titleElement?.getAttribute('data-jp') ?? undefined,
            quality: textOf(qualitySpan),
            releaseDate: textOf(releaseSpan),
            sub: parseIntSafe(info?.querySelector('span.sub')?.textContent),
            title: textOf(titleElement),
            type: typeText as MediaFormat,
            url: `${this.baseUrl}/watch/${id}`,
        }
    }

    /**
     * Parses a suggestion card (autocomplete result) into an AnimeResult.
     */
    private parseSuggestionCard(card: HTMLElement): AnimeResult {
        const image = card.querySelector('.poster img')?.getAttribute('src')
        const titleElement = card.querySelector('.title')
        const title = textOf(titleElement)
        const japaneseTitle = titleElement?.getAttribute('data-jp') ?? null
        const id = card.getAttribute('href')?.split('/')[2]

        const info = card.querySelector('.info')
        const infoChildren = info?.querySelectorAll('*') ?? []
        const year = normalizeText(infoChildren.at(-2)?.textContent)
        const type = normalizeText(infoChildren.at(-3)?.textContent)

        const sub = parseIntSafe(info?.querySelector('span.sub')?.textContent)
        const dub = parseIntSafe(info?.querySelector('span.dub')?.textContent)
        const episodesText =
            normalizeText(infoChildren.at(-4)?.textContent) ?? info?.querySelector('span.sub')?.textContent ?? ''
        const episodes = parseIntSafe(episodesText)

        return {
            dub,
            episodes,
            id: id ?? '',
            image: image ?? '',
            japaneseTitle,
            sub,
            title,
            type: type as MediaFormat,
            url: `${this.baseUrl}/watch/${id}`,
            year,
        }
    }

    /**
     * Parses status and season from the detail section.
     */
    private parseStatusAndSeason(root: HTMLElement): { status: MediaStatus; season: Season | undefined } {
        const statusSpan = root.querySelector('.entity-scroll > .detail div:contains("Status") > span')
        const statusText = normalizeText(statusSpan?.textContent)
        let status: MediaStatus

        switch (statusText) {
            case 'Completed':
                status = mediaStatus.COMPLETED
                break
            case 'Releasing':
                status = mediaStatus.ONGOING
                break
            case 'Not yet aired':
                status = mediaStatus.NOT_YET_AIRED
                break
            default:
                status = mediaStatus.UNKNOWN
                break
        }

        const seasonSpan = root.querySelector('.entity-scroll > .detail div:contains("Premiered") > span')
        const season = normalizeText(seasonSpan?.textContent) as Season | undefined

        return { season, status }
    }

    // ========== Request headers ==========

    /**
     * Builds headers used for all HTTP requests to AnimeKai.
     */
    private headers(): Record<string, string> {
        return {
            Accept: 'text/html, */*; q=0.01',
            'Accept-Language': 'en-US,en;q=0.5',
            'Cache-Control': 'no-cache',
            Connection: 'keep-alive',
            Cookie: '__p_mov=1; usertype=guest; session=vLrU4aKItp0QltI2asH83yugyWDsSSQtyl9sxWKO',
            Pragma: 'no-cache',
            Priority: 'u=0',
            Referer: `${this.baseUrl}/`,
            'Sec-Fetch-Dest': 'empty',
            'Sec-Fetch-Mode': 'cors',
            'Sec-Fetch-Site': 'same-origin',
            'Sec-GPC': '1',
            'User-Agent':
                'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36',
        }
    }
}
