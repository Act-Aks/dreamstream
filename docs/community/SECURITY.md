# Security Policy

DreamStream takes security, privacy, and responsible disclosure seriously.

Do not report security vulnerabilities through public GitHub issues, pull requests, discussions, or comments.

## Supported Versions

DreamStream is currently in early development. Security fixes are handled on the `main` branch until versioned releases are established.

| Version | Supported |
| --- | --- |
| `main` | Yes |
| Released versions | Not yet released |

## Reporting a Vulnerability

Report suspected security vulnerabilities privately by email:

```text
akashsrivastava.git@gmail.com
```

Please include as much detail as you can safely provide:

- A clear description of the vulnerability
- Affected code, dependency, workflow, or behavior
- Steps to reproduce, proof of concept, or screenshots if appropriate
- Impact assessment, including what an attacker could do
- Any known mitigations or workaround ideas
- Your preferred contact information for follow-up

Do not include secrets, access tokens, private user data, or exploit payloads that are not necessary to understand the issue.

## Response Expectations

The maintainers will make a best effort to:

- Acknowledge receipt within 72 hours
- Triage the report and ask clarifying questions when needed
- Keep the reporter informed of material status changes
- Coordinate disclosure timing when a fix is required
- Credit the reporter if requested and appropriate

Response times may vary because DreamStream is an open-source project maintained by contributors.

## Scope

Reports are especially useful when they involve:

- Credential, token, cookie, or private URL exposure
- Unsafe source integration behavior
- Access-control bypass in DreamStream-owned code
- Dependency vulnerabilities affecting runtime behavior
- CI, release, or repository configuration that could compromise the project
- Privacy leaks involving playback history, local files, or user data

The following are usually out of scope unless they show a concrete exploit path in DreamStream-owned code:

- Vulnerabilities in third-party websites or services not controlled by DreamStream
- Rate-limit concerns without a demonstrated security impact
- Social engineering against maintainers or contributors
- Denial-of-service reports that require excessive traffic or abusive testing
- Issues requiring physical access to a user's unlocked device

## Safe Harbor

Security research is welcome when performed responsibly. Do not access, modify, delete, exfiltrate, or retain data that is not yours. Do not disrupt services or attempt to bypass legal access controls. Stop testing and report promptly if you discover sensitive data or a live exploit path.

Good-faith reports following this policy will be treated as authorized security research for the purpose of project response.

## Project Security Principles

DreamStream does not accept contributions that implement DRM circumvention, paywall bypassing, credential theft, token scraping, ad rendering, tracker preservation, or access-control evasion.

Tokens, cookies, private URLs, personally identifiable information, and playback history must not be logged or committed.
