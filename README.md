<div align="center">

<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
<img src="https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" />
<img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" />

<br/><br/>

# 🌾 KrishiYantra

### Agricultural Equipment Rental Marketplace

*Connecting farmers with machine owners — tractors, harvesters, sprayers & more*

<br/>

[![Download APK](https://img.shields.io/badge/⬇️%20Download%20APK-Latest%20Release-2EA44F?style=for-the-badge)](https://github.com/Its-darshu/rent-app/releases/latest)

<br/>

</div>

---

## 📱 About the App

**KrishiYantra** is an OLX-style Android marketplace that bridges the gap between small farmers who need agricultural machinery and equipment owners who want to rent out their assets.

Farmers can browse, filter, and book equipment nearby — while owners can list their machines, manage bookings, and track rental requests in real time.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔐 **Authentication** | Sign up / Sign in with Firebase Auth |
| 🗺️ **Marketplace** | Browse equipment with location-based listings |
| ➕ **List Equipment** | Owners can add tractors, harvesters, sprayers & more |
| 📋 **My Listings** | Manage all your listed equipment in one place |
| 📬 **Rental Requests** | Real-time rental request flow for owners & farmers |
| 💰 **Cost Estimator** | Estimate rental cost before booking |
| 📍 **Google Maps** | Pick and view equipment locations on a live map |
| 👤 **Profile** | User profile with role-based dashboard |

---

## 🖼️ Screenshots

> *(Add screenshots here — drag and drop images into this section on GitHub)*

| Auth | Marketplace | My Listings | Detail |
|---|---|---|---|
| ![auth](screenshots/auth.png) | ![home](screenshots/home.png) | ![listings](screenshots/listings.png) | ![detail](screenshots/detail.png) |

---

## ⬇️ Download

Head to the [**Releases**](https://github.com/Its-darshu/rent-app/releases) page to download the latest APK.

1. Go to [Releases](https://github.com/Its-darshu/rent-app/releases/latest)
2. Download `app-release.apk`
3. On your Android device: **Settings → Install unknown apps → Allow**
4. Open the downloaded APK and install

> Requires Android 8.0 (API 26) or higher.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM (ViewModel + Repository) |
| Backend | Firebase Firestore |
| Auth | Firebase Authentication |
| Storage | Firebase Storage |
| Maps | Google Maps SDK + Compose Maps |
| Image Loading | Coil |
| Min SDK | API 26 (Android 8.0) |
| Target SDK | API 34 (Android 14) |

---

## 🏗️ Project Structure

```
app/src/main/java/com/nammayantra/app/
├── data/
│   ├── model/          # Data classes (Equipment, RentalRequest, UserProfile)
│   └── repository/     # AuthRepository, EquipmentRepository, RequestRepository
├── ui/
│   ├── auth/           # Login / Sign-up screens & ViewModel
│   ├── marketplace/    # Home feed & ViewModel
│   ├── listing/        # Add listing, Detail screen & ViewModel
│   ├── mylistings/     # Owner's listings & ViewModel
│   ├── estimator/      # Cost estimator screen
│   ├── profile/        # User profile screen
│   ├── components/     # Shared UI components
│   └── theme/          # Colors, Typography, Theme
└── MainActivity.kt
```

---

## 🚀 Build from Source

### Prerequisites

- Android Studio Hedgehog or newer
- JDK 17
- A Firebase project with **Authentication**, **Firestore**, and **Storage** enabled

### Setup

1. **Clone the repo**
   ```bash
   git clone https://github.com/Its-darshu/rent-app.git
   cd rent-app
   ```

2. **Add Firebase config**  
   Download `google-services.json` from your Firebase console and place it at:
   ```
   app/google-services.json
   ```

3. **Add API keys**  
   Create `local.properties` in the project root:
   ```properties
   MAPS_API_KEY=your_google_maps_api_key
   ```

4. **Build & Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or open in Android Studio and click **Run ▶**

---

## 🔥 Firebase Setup

Enable the following in your Firebase console:

- **Authentication** → Email/Password provider
- **Firestore Database** → Create with these collections:
  - `users` — user profiles
  - `equipment` — equipment listings
  - `rental_requests` — booking requests
- **Storage** — for equipment images

---

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you'd like to change.

1. Fork the repo
2. Create your feature branch: `git checkout -b feature/my-feature`
3. Commit your changes: `git commit -m 'feat: add my feature'`
4. Push to the branch: `git push origin feature/my-feature`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

<div align="center">

Made with ❤️ for Indian farmers

</div>
