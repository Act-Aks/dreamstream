export interface Book {
    authors: string[]
    description: string
    edition: string
    image: string
    isbn: string[]
    link: string
    publisher: string
    series: string
    title: string
    volume: string
    year: string
}

export interface Hashes {
    AICH: string
    CRC32: string
    eDonkey: string
    MD5: string
    SHA1: string
    SHA256: string[]
    TTH: string
}

export interface LibgenBook extends Book {
    format: string
    hashes: Hashes
    id: string
    language: string
    pages: string
    size: string
    tableOfContents: string
    topic: string
}

export interface LibgenResult {
    hasNextPage: boolean
    result: LibgenBook[]
}
