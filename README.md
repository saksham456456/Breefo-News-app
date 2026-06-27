<div align="center">
  <h1>📰 BriefX</h1>
  <p><strong>Your daily dose of news, curated and summarized efficiently. BriefX respects your time.</strong></p>

  [![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue.svg?logo=kotlin)](https://kotlinlang.org)
  [![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material3-4285F4?logo=android)](https://developer.android.com/jetpack/compose)
  [![Architecture](https://img.shields.io/badge/Architecture-MVVM-green.svg)]()
  [![License](https://img.shields.io/badge/License-MIT-yellow.svg)](#license)
</div>

---

## 📖 About BriefX

**BriefX** is a modern, lightweight Android application designed to deliver the latest news efficiently. Built with cutting-edge Android development technologies like **Jetpack Compose** and **Kotlin**, BriefX focuses on delivering a streamlined reading experience.

At the core of BriefX is the **Smart Summary Engine**, which extracts the most crucial information from news articles, providing you with a quick TL;DR and Key Points, so you can stay informed without spending hours reading long-form content.

Whether you're interested in General News, Technology, Business, Science, Health, Entertainment, or Sports, BriefX tailors the feed to your preferences.

---

## 🚀 Key Features

- **Smart Summary Engine 🧠:** Automatically generates TL;DRs and extracts Key Points from lengthy news articles.
- **Category Filtering 🔍:** Seamlessly switch between 7 distinct news categories (General, Business, Technology, Science, Health, Entertainment, Sports).
- **Beautiful UI 🎨:** Fully implemented using **Jetpack Compose** and **Material 3** guidelines, providing a responsive and fluid user experience.
- **Fast Image Loading ⚡:** Uses [Coil](https://coil-kt.github.io/coil/) for heavily optimized asynchronous image loading.
- **Instant Sharing 📤:** Easily share your favorite articles and summaries with friends or on social media.
- **Responsive Navigation 📱:** Clean transitions between the Home feed and Detailed article views.

---

## 📸 Screenshots

<div align="center">
  <img src="https://placehold.co/300x600/1E1E1E/00D2FF?text=BriefX\nHome+Screen&font=Montserrat" alt="BriefX Home Screen" width="30%" />
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://placehold.co/300x600/1E1E1E/00D2FF?text=Smart+Summary\nDetail+View&font=Montserrat" alt="BriefX Detail View" width="30%" />
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://placehold.co/300x600/1E1E1E/00D2FF?text=Category\nFiltering&font=Montserrat" alt="Category Filtering" width="30%" />
</div>

> *Note: These are illustrative placeholders for actual application screens.*

---

## 🛠️ Tech Stack & Architecture

BriefX showcases modern Android development practices:

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture:** MVVM (Model-View-ViewModel) - Ensuring a clean separation of concerns and robust state management.
- **Asynchronous Programming:** Kotlin Coroutines & `StateFlow`.
- **Networking:** [Retrofit2](https://square.github.io/retrofit/) with OkHttp and Gson Converter for fetching data from the API.
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/) for Compose.
- **API Provider:** [NewsAPI.org](https://newsapi.org/)

---

## 🏗️ Getting Started

Follow these instructions to get a copy of BriefX up and running on your local machine for development and testing.

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (Giraffe or newer recommended)
- JDK 11 or higher
- An active API key from [NewsAPI](https://newsapi.org/)

### Installation Steps

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/BriefX.git
   cd BriefX
   ```

2. **Open the Project:**
   Open Android Studio, select `Open an existing Android Studio project`, and navigate to the cloned `BriefX` directory.

3. **Configure the API Key:**
   - The app currently uses a hardcoded API key in the `NewsApiService.kt` for demonstration purposes.
   - For production or personal use, replace the key `95027c8988fb4eb19747da2a19fd867d` with your own personal key from NewsAPI.

4. **Build and Run:**
   - Sync the project with Gradle files.
   - Select your preferred emulator or connected physical Android device.
   - Click the **Run** button (`Shift + F10`).

---

## 🧪 Testing

The project is configured for testing using standard Android testing libraries. To run tests from the command line:

```bash
./gradlew test
./gradlew connectedAndroidTest
```

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!
Feel free to check out the [issues page](https://github.com/yourusername/BriefX/issues) if you want to contribute.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.

---

<div align="center">
  <p>Built with ❤️ using Android and Jetpack Compose.</p>
</div>
