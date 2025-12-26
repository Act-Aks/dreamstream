import type { MMKV } from 'react-native-mmkv'
import { createMMKV } from 'react-native-mmkv'
import type { StateStorage } from 'zustand/middleware'

interface MmkvStorage {
    clearAll(): void
    contains(key: string): boolean
    getArray<T>(key: string): T[] | undefined
    getBoolean(key: string, defaultValue?: boolean): boolean
    getNumber(key: string): number | undefined
    getObject<T>(key: string): T | undefined
    getString(key: string): string | undefined
    remove(key: string): boolean
    setArray<T>(key: string, value: T[]): void
    setBoolean(key: string, value: boolean): void
    setNumber(key: string, value: number): void
    setObject<T>(key: string, value: T): void
    setString(key: string, value: string): void
}

class MmkvDataStorage implements MmkvStorage {
    private readonly storage: MMKV

    constructor(storageId: string, encryptionKey?: string) {
        this.storage = createMMKV({ id: `dreamstream-${storageId}`, ...(encryptionKey ? { encryptionKey } : {}) })
    }

    getString(key: string) {
        return this.storage.getString(key)
    }
    setString(key: string, value: string) {
        return this.storage.set(key, value)
    }
    getBoolean(key: string, defaultValue?: boolean) {
        const value = this.storage.getBoolean(key)
        return value === undefined ? (defaultValue ?? false) : value
    }
    setBoolean(key: string, value: boolean) {
        return this.storage.set(key, value)
    }
    getNumber(key: string) {
        return this.storage.getNumber(key)
    }
    setNumber(key: string, value: number) {
        return this.storage.set(key, value)
    }
    getObject<T>(key: string) {
        const objectString = this.storage.getString(key)
        if (!objectString) {
            return undefined
        }
        try {
            return JSON.parse(objectString) as T
        } catch (error) {
            console.error(`Failed to parse object for key ${key}:`, error)
            return undefined
        }
    }
    setObject<T>(key: string, value: T) {
        return this.storage.set(key, JSON.stringify(value))
    }
    getArray<T>(key: string) {
        return this.getObject<T[]>(key)
    }
    setArray<T>(key: string, value: T[]) {
        return this.setObject(key, value)
    }
    remove(key: string) {
        return this.storage.remove(key)
    }
    contains(key: string) {
        return this.storage.contains(key)
    }
    clearAll() {
        return this.storage.clearAll()
    }
}

const createMmkvStorage = (storageId: string, encryptionKey?: string): StateStorage => {
    const storage = createMMKV({ id: `dreamstream-${storageId}`, ...(encryptionKey ? { encryptionKey } : {}) })

    return {
        getItem: (name) => {
            const value = storage.getString(name)
            return value ?? null
        },
        removeItem: (name) => {
            return storage.remove(name)
        },
        setItem: (name, value) => {
            return storage.set(name, value)
        },
    }
}

const mainStorage: MmkvStorage = new MmkvDataStorage('main')
const cacheStorage: MmkvStorage = new MmkvDataStorage('cache')
const secureStorage: MmkvStorage = new MmkvDataStorage('secure', 'your-encryption-key')

export const AppStorage = {
    cache: cacheStorage,
    createMmkvStorage,
    main: mainStorage,
    secure: secureStorage,
}
