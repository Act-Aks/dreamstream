package com.dreamstream.core.data.util

import java.io.File

internal val appDataDirectory: File
    get() {
        val userHome = System.getProperty("user.home")
        return when (currentOs) {
            DesktopOs.WINDOWS -> File(System.getenv("APPDATA"), "DreamStream")
            DesktopOs.MACOS -> File(userHome, "Library/Application Support/DreamStream")
            DesktopOs.LINUX -> File(userHome, ".local/share/DreamStream")
        }
    }
