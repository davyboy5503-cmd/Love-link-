# LoveLink Android — v0.1 starter

Native Android starter built with Kotlin and Jetpack Compose.

## Included in this first prototype
- Four-tab app shell: Chat, Memories, Together, Settings
- Neon Night palette presets (Violet, Electric blue, Rose neon, Emerald, Solar)
- Bubble shape selector (Rounded, Pill, Sharp, Notched)
- Local-only demo chat composer

## Not implemented yet
This is a UI foundation, not a working messaging service. Messages currently exist only in app memory and are not sent to another phone. Authentication, pairing, persistent storage, end-to-end encryption, Bluetooth/Wi-Fi Direct transport, server messaging, voice/video calls, location sharing, private vault, scheduled messages and reminders still need implementation and security testing.

## Econet WhatsApp bundle constraint
The app does not disguise traffic as WhatsApp. Econet bundle eligibility must be confirmed through an approved carrier arrangement or documented policy. Until then, assume ordinary mobile data or Wi-Fi is needed for internet messaging. Nearby peer-to-peer communication can be developed separately.

## Open in Android Studio
1. Open this `LoveLink` folder in Android Studio.
2. Use JDK 17.
3. Allow Gradle to sync and download dependencies.
4. Run on an Android device or emulator (Android 8.0/API 26 or later).

Package: `com.lovelink.app`
Version: `0.1.0` (development starter)
