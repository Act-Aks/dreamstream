export const genres = {
    ACTION: 'Action',
    ADVENTURE: 'Adventure',
    CARS: 'Cars',
    COMEDY: 'Comedy',
    DRAMA: 'Drama',
    FANTASY: 'Fantasy',
    HORROR: 'Horror',
    MAHOU_SHOUJO: 'Mahou Shoujo',
    MECHA: 'Mecha',
    MUSIC: 'Music',
    MYSTERY: 'Mystery',
    PSYCHOLOGICAL: 'Psychological',
    ROMANCE: 'Romance',
    SCI_FI: 'Sci-Fi',
    SLICE_OF_LIFE: 'Slice of Life',
    SPORTS: 'Sports',
    SUPERNATURAL: 'Supernatural',
    THRILLER: 'Thriller',
} as const
export type Genres = (typeof genres)[keyof typeof genres]

export const topics = {
    ANIMATION: 'animation',
    ANIME: 'anime',
    COVID_19: 'covid-19',
    EVENTS: 'events',
    GAMES: 'games',
    INDUSTRY: 'industry',
    LIVE_ACTION: 'live-action',
    MANGA: 'manga',
    MERCH: 'merch',
    MUSIC: 'music',
    NOVELS: 'novels',
    PEOPLE: 'people',
} as const
export type Topics = (typeof topics)[keyof typeof topics]
