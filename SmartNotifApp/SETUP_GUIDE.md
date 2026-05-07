# Smart Notification System — Android App
## How to Run on Your Device or Emulator

---

## What you need (one-time install)
1. **Android Studio** — Download free from https://developer.android.com/studio
   - During install, let it download the Android SDK automatically
   - Minimum version: Android Studio Hedgehog (2023.1.1) or newer

2. **Java 17** — Android Studio bundles it, nothing extra needed

---

## Step 1 — Open the project

1. Unzip `SmartNotifApp.zip` anywhere on your computer
2. Open **Android Studio**
3. Click **"Open"** (not "New Project")
4. Navigate to the unzipped `SmartNotifApp` folder → click **OK**
5. Wait for Gradle sync to finish (bottom bar shows progress — takes ~2 min first time)

---

## Step 2A — Run on the Android Emulator (easiest)

1. In Android Studio, click **Tools → Device Manager**
2. Click **"Create Device"**
3. Choose **Pixel 8** → Next
4. Choose **API 35 (Android 15)** → Download it if needed → Next → Finish
5. Press the green ▶ **Run** button (or `Shift+F10`)
6. Select your emulator → OK
7. The app installs and opens automatically

---

## Step 2B — Run on your real Android phone

1. On your phone: **Settings → About Phone** → tap "Build number" **7 times** → Developer mode enabled
2. Go to **Settings → Developer Options** → enable **USB Debugging**
3. Connect phone to computer via USB → tap "Allow" on the phone prompt
4. In Android Studio, your phone appears in the device dropdown (top toolbar)
5. Press ▶ **Run** — app installs on your phone in ~30 seconds

---

## Step 3 — Grant Notification Access (for real notifications)

To intercept real WhatsApp / SMS / Email notifications:

1. Open the app → go to **Settings tab**
2. Tap **"Open notification access settings"**
3. Find **"Smart Notif"** in the list → toggle it ON → tap Allow

> Without this, the app still works fully using the **"Send test notification"** panel.

---

## How to use the app

### Feed tab
- Use the **Send test notification** panel to simulate incoming messages
- Try the 3 quick-fill demo buttons to see Focus vs Normal mode in action
- Each notification shows a **Certainty Score** bar (the NLP confidence %)

### Dashboard tab
- See total / important / silenced counts
- Source breakdown chart (SMS, WhatsApp, Email, Instagram)
- Full chronological history with certainty scores

### NLP tab
- Type any message and watch the score update in real time
- Toggle "Short-code sender" to see the marketing penalty kick in
- Click the example buttons to see the Hospital disambiguation

### Settings tab
- **Mode toggle** — switch between Normal Mode and Focus Mode
- **Channel toggles** — enable/disable each source
- **Threshold slider** — drag to change the 80% cutoff
- **Keywords** — add/remove words that instantly pass in Focus Mode
- **Notification access** — grant permission for real notifications

---

## App structure (for your report)

```
app/src/main/java/com/smartnotif/
│
├── core/
│   ├── NotificationEntity.kt          ← Room DB entity (Notification class from diagram)
│   ├── NotificationDatabase.kt        ← Room DB + DAO
│   ├── FilterEngine.kt                ← Strategy Pattern (NormalStrategy / FocusStrategy)
│   ├── UserPreferences.kt             ← DataStore preferences
│   └── SmartNotificationListenerService.kt  ← Observer Pattern (real OS notifications)
│
├── nlp/
│   └── AIEngine.kt                    ← NLP scorer (marketing vs personal context)
│
├── ui/
│   ├── MainViewModel.kt               ← Singleton-style shared state (ViewModel)
│   ├── screens/
│   │   ├── FeedScreen.kt              ← Notification feed + test sender
│   │   ├── DashboardScreen.kt         ← History + statistics
│   │   ├── NLPScreen.kt               ← Live NLP analyser
│   │   └── SettingsScreen.kt          ← All preferences
│   ├── components/
│   │   └── NotificationCard.kt        ← Shared UI component
│   └── theme/
│       └── Theme.kt                   ← Material3 purple theme
│
└── MainActivity.kt                    ← Entry point + bottom navigation
```

---

## Design Patterns in the app

| Pattern | Where you can see it |
|---|---|
| **Observer** | `SmartNotificationListenerService` hooks into Android OS; `MainViewModel` exposes `Flow` that Compose screens observe |
| **Strategy** | `FilterEngine.setFocusMode()` / `setNormalMode()` swaps `FocusStrategy` ↔ `NormalStrategy` at runtime — try toggling the mode switch |
| **Singleton** | `NotificationDatabase.getInstance()` — one DB instance across the whole app |
| **MVVM** | `MainViewModel` is the single source of truth; all screens read from it |
