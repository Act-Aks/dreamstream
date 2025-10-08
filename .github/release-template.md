{{CURRENT_BODY}}

---

## 📱 Download & Install

<div align="center">

### 🚀 Quick Install - Scan QR Code

**Point your Android camera at this QR code to visit the release page:**

<img src="data:image/png;base64,{{QR_BASE64}}" alt="QR Code for Release Page" width="200" height="200" style="border: 2px solid #e1e4e8; border-radius: 8px; padding: 10px; background: white;">

<details>
<summary>QR Code not showing? Click here for alternative</summary>

**Direct QR Code Link:** [Download QR Code](https://github.com/{{REPOSITORY}}/releases/download/v{{VERSION}}/DreamStream-release-qr.png)

**Or visit this URL directly:**
```
https://github.com/{{REPOSITORY}}/releases/tag/v{{VERSION}}
```

</details>

*📲 QR Code links to this release page for easy mobile access!*

</div>

### 📦 APK Variants - Choose Your Architecture

<div align="center">

**🎯 Not sure which one to download? Choose Universal APK - it works on all devices!**

</div>

<table>
<tr>
<th align="center">📱 APK Variant</th>
<th align="center">🏗️ Architecture</th>
<th align="center">📏 Size</th>
<th align="center">📥 Download</th>
<th align="center">🔗 QR Code</th>
</tr>
<tr>
<td align="center"><strong>🌍 Universal</strong><br><em>Recommended</em></td>
<td align="center">All Architectures<br><small>Works on any Android device</small></td>
<td align="center"><code>{{UNIVERSAL_SIZE}}</code></td>
<td align="center"><a href="https://github.com/{{REPOSITORY}}/releases/download/v{{VERSION}}/DreamStream-universal-v{{VERSION}}.apk"><strong>Download</strong></a></td>
<td align="center"><a href="https://github.com/{{REPOSITORY}}/releases/download/v{{VERSION}}/DreamStream-universal-qr.png">📱 QR</a></td>
</tr>
<tr>
<td align="center"><strong>🚀 ARM64</strong><br><em>Optimized</em></td>
<td align="center">64-bit ARM<br><small>Modern phones (2017+)</small></td>
<td align="center"><code>{{ARM64_SIZE}}</code></td>
<td align="center"><a href="https://github.com/{{REPOSITORY}}/releases/download/v{{VERSION}}/DreamStream-arm64-v{{VERSION}}.apk"><strong>Download</strong></a></td>
<td align="center"><a href="https://github.com/{{REPOSITORY}}/releases/download/v{{VERSION}}/DreamStream-arm64-qr.png">📱 QR</a></td>
</tr>
<tr>
<td align="center"><strong>⚡ ARM32</strong><br><em>Legacy</em></td>
<td align="center">32-bit ARM<br><small>Older phones (pre-2017)</small></td>
<td align="center"><code>{{ARM32_SIZE}}</code></td>
<td align="center"><a href="https://github.com/{{REPOSITORY}}/releases/download/v{{VERSION}}/DreamStream-arm32-v{{VERSION}}.apk"><strong>Download</strong></a></td>
<td align="center"><a href="https://github.com/{{REPOSITORY}}/releases/download/v{{VERSION}}/DreamStream-arm32-qr.png">📱 QR</a></td>
</tr>
<tr>
<td align="center"><strong>💻 x86_64</strong><br><em>Intel 64-bit</em></td>
<td align="center">64-bit Intel<br><small>Emulators, Chromebooks</small></td>
<td align="center"><code>{{X86_64_SIZE}}</code></td>
<td align="center"><a href="https://github.com/{{REPOSITORY}}/releases/download/v{{VERSION}}/DreamStream-x86_64-v{{VERSION}}.apk"><strong>Download</strong></a></td>
<td align="center"><a href="https://github.com/{{REPOSITORY}}/releases/download/v{{VERSION}}/DreamStream-x86_64-qr.png">📱 QR</a></td>
</tr>
<tr>
<td align="center"><strong>🖥️ x86</strong><br><em>Intel 32-bit</em></td>
<td align="center">32-bit Intel<br><small>Legacy emulators</small></td>
<td align="center"><code>{{X86_SIZE}}</code></td>
<td align="center"><a href="https://github.com/{{REPOSITORY}}/releases/download/v{{VERSION}}/DreamStream-x86-v{{VERSION}}.apk"><strong>Download</strong></a></td>
<td align="center"><a href="https://github.com/{{REPOSITORY}}/releases/download/v{{VERSION}}/DreamStream-x86-qr.png">📱 QR</a></td>
</tr>
</table>

<div align="center">

**💡 Architecture Guide:**
- **Universal APK**: Largest file but works on all devices - choose this if unsure
- **ARM64 APK**: Smaller, optimized for modern Android phones (recommended for newer devices)
- **ARM32 APK**: For older Android devices or specific compatibility needs
- **x86_64 APK**: For Intel-based devices, emulators, and some Chromebooks
- **x86 APK**: For legacy Intel-based devices and older emulators

</div>

### 🔧 Installation Guide

<details>
<summary><strong>📋 Step-by-step installation (click to expand)</strong></summary>

#### Method 1: QR Code (Recommended)
1. **Open Camera**: Use your Android device's camera app
2. **Scan QR Code**: Point camera at the QR code above
3. **Tap Notification**: Tap the download notification that appears
4. **Install**: Follow the installation prompts

#### Method 2: Manual Download
1. **Enable Unknown Sources**: 
   - Go to `Settings` → `Security` → Enable `Unknown Sources`
   - Or `Settings` → `Apps` → `Special Access` → `Install Unknown Apps`
2. **Download APK**: Click the download link above
3. **Install**: Tap the downloaded file and follow prompts
4. **Launch**: Find "{{APP_NAME}}" in your app drawer

#### ⚠️ Security Note
This APK is built and signed by our automated CI/CD pipeline. Always download from official GitHub releases.

</details>

---

## 📝 What's Changed

<details>
<summary><strong>🔍 Recent Changes (click to expand)</strong></summary>

{{CHANGELOG}}

*[View full changelog](https://github.com/{{REPOSITORY}}/compare/{{PREVIOUS_TAG}}...v{{VERSION}})*

</details>

---

## 👥 Contributors

<div align="center">

**🙏 Special thanks to everyone who made this release possible:**

</div>

{{CONTRIBUTORS}}

<div align="center">

*Want to contribute? Check out our [Contributing Guide](https://github.com/{{REPOSITORY}}/blob/main/CONTRIBUTING.md)*

</div>

---

## 📊 Release Information

<table>
<tr>
<td><strong>📦 Version</strong></td>
<td><code>v{{VERSION}}</code></td>
</tr>
<tr>
<td><strong>🗓️ Build Date</strong></td>
<td><code>{{BUILD_DATE}}</code></td>
</tr>
<tr>
<td><strong>📱 Platform</strong></td>
<td>Android (API 21+)</td>
</tr>
<tr>
<td><strong>🏗️ Architectures</strong></td>
<td>Universal, ARM64, ARM32, x86_64, x86</td>
</tr>
<tr>
<td><strong>📏 Total Size</strong></td>
<td>{{TOTAL_SIZE}} (all variants)</td>
</tr>
<tr>
<td><strong>🔄 Commits</strong></td>
<td>{{COMMIT_COUNT}} new commits</td>
</tr>
</table>

### 📊 APK Variant Details

<details>
<summary><strong>📋 Size breakdown by architecture (click to expand)</strong></summary>

{{APK_SIZES}}

**Architecture Notes:**
- **Universal**: Contains all architectures, largest file but maximum compatibility
- **ARM64**: Optimized for 64-bit ARM processors (most modern Android devices)
- **ARM32**: For 32-bit ARM processors (older devices or specific use cases)
- **x86_64**: For 64-bit Intel processors (emulators, Chromebooks, Intel-based Android devices)
- **x86**: For 32-bit Intel processors (legacy emulators and older Intel devices)

</details>

---

<div align="center">

## 🌟 Enjoying {{APP_NAME}}?

<a href="https://github.com/{{REPOSITORY}}">
  <img src="https://img.shields.io/github/stars/{{REPOSITORY}}?style=social" alt="GitHub stars">
</a>
<a href="https://github.com/{{REPOSITORY}}/issues">
  <img src="https://img.shields.io/github/issues/{{REPOSITORY}}" alt="GitHub issues">
</a>
<a href="https://github.com/{{REPOSITORY}}/releases">
  <img src="https://img.shields.io/github/v/release/{{REPOSITORY}}" alt="GitHub release">
</a>

**Help us improve!**

[⭐ Star this repo](https://github.com/{{REPOSITORY}}) • [🐛 Report Issues](https://github.com/{{REPOSITORY}}/issues) • [💬 Join Discussions](https://github.com/{{REPOSITORY}}/discussions) • [📖 Documentation](https://github.com/{{REPOSITORY}}/wiki)

</div>