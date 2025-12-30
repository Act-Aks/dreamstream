/**
 * @type {import('semantic-release').GlobalConfig}
 */
/** biome-ignore-all lint/suspicious/noTemplateCurlyInString: Follows docs */
export default {
    branches: ['main'],
    plugins: [
        '@semantic-release/commit-analyzer',
        '@semantic-release/release-notes-generator',
        [
            '@semantic-release/npm',
            {
                tarballDir: 'dist',
            },
        ],
        [
            '@semantic-release/git',
            {
                assets: ['package.json'],
                message:
                    'chore(release): @dreamstream/providers v${nextRelease.version} [skip ci]\n\n${nextRelease.notes}',
            },
        ],
        '@semantic-release/github',
    ],
    tagFormat: '@dreamstream/providers-v${version}',
}
