interface WithResult<T> {
    result: T
}

export type Token = string

type Data = [number, number]
export interface IFrameData {
    skip: { intro: Data; outro: Data }
    url: string
}

export interface DecodedData {
    download: string
    sources: Record<'file', string>[]
    tracks: Record<'kind' | 'file' | 'label', string>[]
}

export interface TokenResponse extends WithResult<Token> {}
export interface IFrameDataResponse extends WithResult<IFrameData> {}
export interface DecodedDataResponse extends WithResult<DecodedData> {}
