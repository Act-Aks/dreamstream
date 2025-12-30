import { defineConfig } from 'tsup'

export default defineConfig({
    clean: true,
    dts: true,
    entry: ['src'],
    format: ['esm'],
    minify: process.env.NODE_ENV === 'production',
    outDir: 'dist',
    platform: 'node',
    skipNodeModulesBundle: true,
    sourcemap: process.env.NODE_ENV !== 'production',
    splitting: false,
    treeshake: true,
    tsconfig: 'tsconfig.json',
})
