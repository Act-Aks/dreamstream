import {
    type AnimeInfo,
    AnimeParser,
    type AnimeResult,
    assertCondition,
    type EpisodeServer,
    type Intro,
    type MediaFormat,
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
import { type CheerioAPI, load } from 'cheerio'
import { parseScheduleDate, type ScheduleOptions } from './animekai.static'

const megaUpExtractor = new MegaUp()

export class AnimeKai extends AnimeParser {
    private static BASE_URL = 'https://anikai.to'

    override readonly name = 'AnimeKai'

    protected override baseUrl = 'https://anikai.to'
    protected override classPath = 'ANIME.AnimeKai'
    protected override logo =
        'https://anikai.to//assets/uploads/37585a39fe8c8d8fafaa2c7bfbf5374ecac859ea6a0288a6da2c61f5.png'

    private static readonly BACKGROUND_IMAGE_REGEX = /background-image:\s*url\(["']?(.+?)["']?\)/i
    private getPage(page: number): number {
        return page <= 0 ? 1 : page
    }

    constructor() {
        super(AnimeKai.BASE_URL)
    }

    /**
     * Search for anime
     * @param query Search query string
     * @param page Page number (default: 1)
     * @returns Promise<Search<AnimeResult>>
     */
    override search(query: string, page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(
            `${this.baseUrl}/browser?keyword=${query.replace(/[\W_]+/g, '+')}&page=${this.getPage(page)}`
        )
    }

    /**
     * @param page number
     */
    fetchLatestCompleted(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/completed?page=${this.getPage(page)}`)
    }

    /**
     * @param page number
     */
    fetchRecentlyAdded(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/recent?page=${this.getPage(page)}`)
    }

    /**
     * @param page number
     */
    fetchRecentlyUpdated(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/updates?page=${this.getPage(page)}`)
    }

    /**
     * @param page number
     */
    fetchNewReleases(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/new-releases?page=${this.getPage(page)}`)
    }

    /**
     * @param page number
     */
    fetchMovie(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/movie?page=${this.getPage(page)}`)
    }

    /**
     * @param page number
     */
    fetchTV(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/tv?page=${this.getPage(page)}`)
    }

    /**
     * @param page number
     */
    fetchOVA(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/ova?page=${this.getPage(page)}`)
    }

    /**
     * @param page number
     */
    fetchONA(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/ona?page=${this.getPage(page)}`)
    }

    /**
     * @param page number
     */
    fetchSpecial(page = 1): Promise<Search<AnimeResult>> {
        return this.scrapeCardPage(`${this.baseUrl}/special?page=${this.getPage(page)}`)
    }

    async fetchGenres(): Promise<string[]> {
        try {
            const res: string[] = []
            const data = await this.client.get<string>(`${this.baseUrl}/home`, { headers: this.headers() })
            const $ = load(data)

            const sideBar = $('#menu')
            sideBar.find('ul.c4 li a').each((_i, ele) => {
                const genres = $(ele)
                res.push(genres.text().toLowerCase())
            })

            return res
        } catch (error) {
            this.throwError(error, 'Genre fetch error')
        }
    }

    /**
     * @param page number
     */
    genreSearch(genre: string, page = 1): Promise<Search<AnimeResult>> {
        if (genre === '') {
            this.throwError('Genre is empty')
        }
        return this.scrapeCardPage(`${this.baseUrl}/genres/${genre}?page=${this.getPage(page)}`)
    }

    /**
     * Fetches the schedule for a given date.
     * @param date
     * @param timezone
     * @returns A promise that resolves to an object containing the search results.
     */
    async fetchSchedule({ date, timezone = 5.5 }: ScheduleOptions): Promise<Search<AnimeResult>> {
        try {
            const res: Search<AnimeResult> = { results: [] }
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

            const $ = load(htmlContent)

            $('ul li').each((_i, ele) => {
                const card = $(ele)
                const titleElement = card.find('span.title')
                const episodeText = card.find('span').last().text().trim()

                res.results.push({
                    airingEpisode: episodeText.replace('EP ', ''), // Extract episode number
                    airingTime: card.find('span.time').text().trim(),
                    id: card.find('a').attr('href')?.split('/')[2] ?? '', // Extract anime Id
                    japaneseTitle: titleElement.attr('data-jp'),
                    title: titleElement.text().trim(),
                })
            })
            return res
        } catch (error) {
            this.throwError(error, 'Schedule fetch error')
        }
    }

    /**
     * Fetches the spotlight section of the anime website.
     *
     * @returns A promise that resolves to an object containing the anime results.
     */
    async fetchSpotlight(): Promise<Search<AnimeResult>> {
        try {
            const res: Search<AnimeResult> = { results: [] }
            const data = await this.client.get<string>(`${this.baseUrl}/home`, { headers: this.headers() })
            const $ = load(data)

            $('div.swiper-wrapper > div.swiper-slide').each((_i, el) => {
                const card = $(el)
                const titleElement = card.find('div.detail > p.title')
                const id = card.find('div.swiper-ctrl > a.btn').attr('href')?.replace('/watch/', '')
                const img = card.attr('style')
                res.results.push({
                    banner: img?.match(AnimeKai.BACKGROUND_IMAGE_REGEX)?.[1] ?? null,
                    description: card.find('div.detail > p.desc').text().trim(),
                    dub: parseIntSafe(card.find('div.detail > div.info > span.dub').text().trim()),
                    genres: card
                        .find('div.detail > div.info')
                        .children()
                        .last()
                        .text()
                        .trim()
                        .split(',')
                        .map((genre) => genre.trim()),
                    id: id ?? '',
                    japaneseTitle: titleElement.attr('data-jp'),
                    quality: card.find('div.detail > div.mics >div:contains("Quality")').children('span').text().trim(),
                    releaseDate: card
                        .find('div.detail > div.mics >div:contains("Release")')
                        .children('span')
                        .text()
                        .trim(),
                    sub: parseIntSafe(card.find('div.detail > div.info > span.sub').text().trim()),
                    title: titleElement.text(),
                    type: card.find('div.detail > div.info').children().eq(-2).text().trim() as MediaFormat,
                    url: `${this.baseUrl}/watch/${id}`,
                })
            })

            return res
        } catch (error) {
            this.throwError(error, 'Spotlight fetch error')
        }
    }

    /**
     * Fetches the search suggestions for a given query.
     * @param query - The search query string.
     * @returns A promise that resolves to an object containing the search results.
     */
    async fetchSearchSuggestions(query: string): Promise<Search<AnimeResult>> {
        try {
            const res: Search<AnimeResult> = { results: [] }
            const data = await this.client.get<{ result: { html: string } }>(
                `${this.baseUrl}/ajax/anime/search?keyword=${query.replace(/[\W_]+/g, '+')}`,
                { headers: this.headers() }
            )
            const $ = load(data.result.html)

            $('a.aitem').each((_i, el) => {
                const card = $(el)
                const image = card.find('.poster img').attr('src')
                const titleElement = card.find('.title')
                const title = titleElement.text().trim()
                const japaneseTitle = titleElement.attr('data-jp')
                const id = card.attr('href')?.split('/')[2]
                const year = card.find('.info').children().eq(-2).text().trim()
                const type = card.find('.info').children().eq(-3).text().trim()
                const sub = parseIntSafe(card.find('.info span.sub')?.text())
                const dub = parseIntSafe(card.find('.info span.dub')?.text())
                const episodes = parseIntSafe(
                    card.find('.info').children().eq(-4).text().trim() ?? card.find('.info span.sub')?.text()
                )

                res.results.push({
                    dub,
                    episodes,
                    id: id ?? '',
                    image: image ?? '',
                    japaneseTitle: japaneseTitle ?? null,
                    sub,
                    title,
                    type: type as MediaFormat,
                    url: `${this.baseUrl}/watch/${id}`,
                    year,
                })
            })

            return res
        } catch (error) {
            this.throwError(error, 'Search suggestions fetch error')
        }
    }

    /**
     * Fetch anime information
     * @param id Anime Id/slug
     * @returns A promise that resolves to an object containing the anime info.
     */
    override fetchAnimeInfo = async (id: string): Promise<AnimeInfo> => {
        const info: AnimeInfo = { id, title: '' }
        try {
            const data = await this.client.get<string>(`${this.baseUrl}/watch/${id}`, { headers: this.headers() })
            const $ = load(data)

            info.title = $('.entity-scroll > .title').text()
            // biome-ignore lint/complexity/useLiteralKeys: This is an extra field
            info['japaneseTitle'] = $('.entity-scroll > .title').attr('data-jp')?.trim()
            info.image = $('div.poster > div >img').attr('src') ?? ''
            info.description = $('.entity-scroll > .desc').text().trim()
            // Movie, TV, OVA, ONA, Special, Music
            info.type = $('.entity-scroll > .info').children().last().text().toUpperCase() as MediaFormat
            info.url = `${this.baseUrl}/watch/${id}`

            info.recommendations = []
            $('section.sidebar-section:not(#related-anime) .aitem-col .aitem').each((_i, ele) => {
                const aTag = $(ele)
                const id = aTag.attr('href')?.replace('/watch/', '')
                info.recommendations?.push({
                    dub: parseIntSafe(aTag.find('.info span.dub')?.text()),
                    episodes: parseIntSafe(
                        aTag.find('.info').children().eq(-2).text().trim() ?? aTag.find('.info span.sub')?.text()
                    ),
                    id: id ?? '',
                    image: aTag.attr('style')?.match(AnimeKai.BACKGROUND_IMAGE_REGEX)?.[1] ?? '',
                    japaneseTitle: aTag.find('.title').attr('data-jp')?.trim(),
                    sub: parseIntSafe(aTag.find('.info span.sub')?.text()),
                    title: aTag.find('.title').text().trim(),
                    type: aTag.find('.info').children().last().text().trim() as MediaFormat,
                    url: `${this.baseUrl}${aTag.attr('href')}`,
                })
            })

            info.relations = []
            $('section#related-anime .tab-body .aitem-col').each((_i, ele) => {
                const card = $(ele)
                const aTag = card.find('a.aitem')
                const id = aTag.attr('href')?.replace('/watch/', '')
                info.relations?.push({
                    dub: parseIntSafe(card.find('.info span.dub')?.text()),
                    episodes: parseIntSafe(
                        card.find('.info').children().eq(-3).text().trim() ?? card.find('.info span.sub')?.text()
                    ),
                    id: id ?? '',
                    image: aTag.attr('style')?.match(AnimeKai.BACKGROUND_IMAGE_REGEX)?.[1] ?? '',
                    japaneseTitle: aTag.find('.title').attr('data-jp')?.trim(),
                    relationType: card.find('.info').children().last().text().trim(),
                    sub: parseIntSafe(card.find('.info span.sub')?.text()),
                    title: aTag.find('.title').text().trim(),
                    type: card.find('.info').children().eq(-2).text().trim() as MediaFormat,
                    url: `${this.baseUrl}${aTag.attr('href')}`,
                })
            })

            info.hasSub = $('.entity-scroll > .info > span.sub').length > 0 // check for sub
            info.hasDub = $('.entity-scroll > .info > span.dub').length > 0 // check for dub

            info.genres = []
            $('.entity-scroll > .detail')
                .find('div:contains("Genres")')
                .each(function () {
                    const genre = $(this).text().trim()
                    if (genre) {
                        info.genres?.push(genre)
                    }
                })

            switch ($('.entity-scroll > .detail').find("div:contains('Status') > span").text().trim()) {
                case 'Completed':
                    info.status = mediaStatus.COMPLETED
                    break
                case 'Releasing':
                    info.status = mediaStatus.ONGOING
                    break
                case 'Not yet aired':
                    info.status = mediaStatus.NOT_YET_AIRED
                    break
                default:
                    info.status = mediaStatus.UNKNOWN
                    break
            }

            info.season = $('.entity-scroll > .detail').find("div:contains('Premiered') > span").text().trim() as Season

            const aniId = $('.rate-box#anime-rating').attr('data-id')
            assertCondition(aniId, 'Ani Id not found')
            const episodesAjax = await this.client.get<{ result: string }>(
                `${this.baseUrl}/ajax/episodes/list?ani_id=${aniId}&_=${await megaUpExtractor.generateToken(aniId ?? '')}`,
                {
                    headers: {
                        ...this.headers(),
                        Referer: `${this.baseUrl}/watch/${id}`,
                        'X-Requested-With': 'XMLHttpRequest',
                    },
                }
            )

            const $$ = load(episodesAjax.result)

            info.totalEpisodes = $$('div.eplist > ul > li').length
            info.episodes = []
            $$('div.eplist > ul > li > a').each((_i, el) => {
                const episodeId = `${info.id}$ep=${$$(el).attr('num')}$token=${$$(el).attr('token')}` //appending token to episode id, as it is required to fetch servers keeping the structure same as other providers
                const number = parseIntSafe($$(el).attr('num'))
                const title = $$(el).children('span').text().trim()
                const url = `${this.baseUrl}/watch/${info.id}${$$(el).attr('href')}ep=${$$(el).attr('num')}`
                const isFiller = $$(el).hasClass('filler')
                const isSubbed = number <= parseIntSafe($('.entity-scroll > .info > span.sub').text().trim())
                const isDubbed = number <= parseIntSafe($('.entity-scroll > .info > span.dub').text().trim())

                info.episodes?.push({
                    id: episodeId,
                    isDubbed,
                    isFiller,
                    isSubbed,
                    number,
                    title,
                    url,
                })
            })

            return info
        } catch (error) {
            this.throwError(error, 'Anime info fetch error')
        }
    }

    /**
     * Fetch episode video sources
     * @param episodeId Episode Id
     * @param server Server type (default: VidCloud)
     * @param subOrDub Sub or dub preference (default: SUB)
     * @returns A promise that resolves to an object containing the anime episode source.
     */
    override fetchEpisodeSources = async (
        episodeId: string,
        server: StreamingServers = streamingServers.MegaUp,
        subbedOrDubbed: SubOrDub = subOrDub.SUB
    ): Promise<Source> => {
        if (episodeId.startsWith('http')) {
            const serverUrl = new URL(episodeId)
            switch (server) {
                case streamingServers.MegaUp:
                    return {
                        headers: { Referer: serverUrl.href },
                        ...(await megaUpExtractor.extract(serverUrl)),
                        // biome-ignore lint/performance/useTopLevelRegex: Ignore here
                        download: serverUrl.href.replace(/\/e\//, '/download/'),
                    }
                default:
                    return {
                        headers: { Referer: serverUrl.href },
                        ...(await megaUpExtractor.extract(serverUrl)),
                        // biome-ignore lint/performance/useTopLevelRegex: Ignore here
                        download: serverUrl.href.replace(/\/e\//, '/download/'),
                    }
            }
        }

        try {
            const servers = await this.fetchEpisodeServers(episodeId, subbedOrDubbed)
            const i = servers.findIndex((s) => s.name.toLowerCase().includes(server)) //for now only megaup is available, hence directly using it

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
     * @param url string
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
            const $ = load(data)

            const pagination = $('ul.pagination')
            res.currentPage = parseIntSafe(pagination.find('.page-item.active span.page-link').text().trim(), 10) ?? 0
            const nextPage = pagination
                .find('.page-item.active')
                .next()
                .find('a.page-link')
                .attr('href')
                ?.split('page=')[1]
            if (nextPage !== undefined && nextPage !== '') {
                res.hasNextPage = true
            }
            const totalPages = pagination.find('.page-item:last-child a.page-link').attr('href')?.split('page=')[1]
            if (totalPages === undefined || totalPages === '') {
                res.totalPages = res.currentPage
            } else {
                res.totalPages = parseIntSafe(totalPages, 10) ?? 0
            }
            res.results = this.scrapeCard($)
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

    /**
     * @param $ cheerio instance
     */
    private scrapeCard($: CheerioAPI): AnimeResult[] {
        try {
            const results: AnimeResult[] = []

            $('.aitem').each((_i, ele) => {
                const card = $(ele)
                const atag = card.find('div.inner > a')
                const id = atag.attr('href')?.replace('/watch/', '')
                const type = card.find('.info').children().last()?.text().trim()

                results.push({
                    dub: parseIntSafe(card.find('.info span.dub')?.text(), 10) ?? 0,
                    episodes: parseIntSafe(
                        card.find('.info').children().eq(-2).text().trim() ?? card.find('.info span.sub')?.text()
                    ), //if no direct episode count, then just use sub count
                    id: id ?? '',
                    image: card.find('img')?.attr('data-src') ?? card.find('img')?.attr('src') ?? '',
                    //   duration: card.find('.fdi-duration')?.text(),
                    japaneseTitle: card.find('a.title')?.attr('data-jp')?.trim(),
                    //   nsfw: card.find('.tick-rate')?.text() === '18+' ? true : false,
                    sub: parseIntSafe(card.find('.info span.sub')?.text(), 10),
                    title: atag.text().trim(),
                    type: type as MediaFormat,
                    url: `${this.baseUrl}${atag.attr('href')}`,
                })
            })

            return results
        } catch (error) {
            this.throwError(error, 'ScrapeCard error')
        }
    }
    /**
     * Fetch available episode servers
     * @param episodeId Episode ID
     * @param subOrDub Sub or dub preference (default: SUB)
     * @returns A promise that resolves to an array containing the episode servers.
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
            const $ = load(data.result)
            const servers: EpisodeServer[] = []
            const subOrDubStr = subbedOrDubbed === subOrDub.SUB ? 'softsub' : 'dub'
            const serverItems = $(`.server-items.lang-group[data-id="${subOrDubStr}"] .server`)
            await Promise.all(
                serverItems.map(async (_i, server) => {
                    const id = $(server).attr('data-lid')
                    assertCondition(id, 'ID is no available')
                    const data = await this.client.get<{ result: string }>(
                        `${this.baseUrl}/ajax/links/view?id=${id}&_=${await megaUpExtractor.generateToken(id ?? '')}`,
                        { headers: this.headers() }
                    )
                    const decodedIframeData = await megaUpExtractor.decodeIframeData(data.result)
                    servers.push({
                        intro: {
                            end: decodedIframeData?.skip.intro[1],
                            start: decodedIframeData?.skip.intro[0],
                        },
                        name: toLowerCase(`MegaUp ${$(server).text().trim()}`), //megaup is the only server for now
                        outro: {
                            end: decodedIframeData?.skip.outro[1],
                            start: decodedIframeData?.skip.outro[0],
                        },
                        url: decodedIframeData.url,
                    })
                })
            )
            return servers
        } catch (error) {
            this.throwError(error, 'Get episode servers error')
        }
    }

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
