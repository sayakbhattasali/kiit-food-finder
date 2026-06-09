# 🍽️ KIIT Food Finder

> A cinematic, high-performance Android food discovery experience built natively for KIIT students. 
> Find nearby restaurants, cafés, desserts, late-night spots, and campus favorites with an ultra-polished premium UI.

---

## 📲 Download APK

[⬇ Download KIIT Food Finder v2.1](https://github.com/sayakbhattasali/kiit-food-finder/releases/latest)

---

# ✨ About The Project

KIIT Food Finder is a modern, design-driven Android application engineered to help KIITians instantly locate the best food spots surrounding their specific residential hostel zones. 

Built entirely with a high-end, premium dark cinematic aesthetic, the app merges granular campus data with interactive physics, smart state preservation, and immediate geospatial logic. It eliminates the friction of campus food hunting—transforming a standard utility app into an immersive digital extension of student life.

---

# 🚀 Features

### 🏠 Hostel-Anchored Recommendation Engine
*   **35+ Hostels Pre-Mapped:** Built-in positioning support for every major residential hostel complex at KIIT University.
*   **Geospatial Campus Logic:** Intuitively anchors searches to your selected hostel to calculate dynamic distance metrics and realistic walking time approximations ($<1.5\text{ km}$ average campus radius).

### 🎬 Interactive, Next-Gen UI/UX
*   **Cinematic About Screen:** Features an ultra-modern glassmorphic layout accessible by tapping the Home screen logo. 
*   **3D "Coin Flip" Developer Card:** Integrated `pointerInput` matrix gestures and spring animations (`DampingRatioLowBouncy`) allow users to swipe, spin, and flip the developer card to reveal hidden layers.
*   **Buttery Smooth Motion:** Custom entrance transitions designed with fluid `EaseOutCubic` interpolation curves alongside ambient, breathing visual indicators to guide interactions.
*   **Trophy Recommendation Layer:** Advanced dynamic depth mapping renders a premier **"🏆 Best Pick"** high-elevation card overlay for elite restaurant discovery.

### 🧠 Advanced Search Intelligence & Filtering
*   **Bulletproof Filter Resets:** Upgraded structural state synchronization wipes cached query values upon leaving the Results view. Completely fixes memory-lock bugs, guaranteeing a completely fresh, unfiltered list whenever a new browsing session begins.
*   **Persistent Scroll Restoration:** Leverages real-time `snapshotFlow` tracking to save and restore your exact `LazyColumn` list index and pixel offset when jumping back and forth from detail views.
*   **Tactile Micro-Interactions:** Crisp, localized haptic vibration pulses (`LongPress` and `TextHandleMove`) embedded directly into filter chips and card interactions.

### 🎯 Pro-Tier Sorting Options
*   **Nearest:** Sorted strictly by campus geospatial proximity.
*   **Best Rated:** High-performance restaurant index filtering by real customer ratings.
*   **Cheapest:** Driven by a concrete pricing parser ($₹\text{XX}$ for one) rather than basic categorical estimations.
*   **Open Now / Late Night:** Real-time business hour validation filters for immediate cravings and post-midnight campus food runs.

---

# 📸 Screenshots

## 🏠 Home Screen

<p align="center">
  <img src="./screenshots/home.jpg" width="210"/>
</p>

---

## 🍽️ Nearby Food Discovery

<p align="center">
  <img src="./screenshots/results.jpg" width="210"/>
</p>

---

## 📍 Store Details

<p align="center">
  <img src="./screenshots/details.jpg" width="210"/>
</p>

---

## ❤️ Favorites

<p align="center">
  <img src="./screenshots/favorites.jpg" width="210"/>
</p>

---

## 🔍 Smart Search

<p align="center">
  <img src="./screenshots/search.jpg" width="210"/>
</p>

---

# 🛠️ Tech Stack

*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose (Modern Declarative Layouts)
*   **Architecture:** MVVM (Model-View-ViewModel) with strict Clean Architecture boundaries
*   **Data Layer:** Room DB (SQLite wrapper with Thread-safe Volatile Singleton initialization)
*   **Asynchronous Flow:** Kotlin Coroutines & `StateFlow` reactive streams
*   **Design Paradigm:** Material 3 Guidelines integrated with custom premium Glassmorphism implementations
*   **Intents & Navigation:** Hardened Google Maps deep-linking with automated fallback wrappers

---

# 🎨 Design Philosophy

KIIT Food Finder breaks away from generic grid-list structures to deliver something atmospheric. It is intentionally designed to reflect the aesthetic of student culture—late-night hostel walks, cafe hopping, and social hangouts around Patia.

The interface prioritizes:
*   **Centralized Iconography:** Zero string-based emojis. The entire layout relies on a strict, centralized `FoodFinderIcon` vector mapping system for superior performance and design uniformity.
*   **Visual Depth & Hierarchy:** Dynamic canvas radial gradients layered over a true dark palette (`Surface900`).
*   **Responsive Scaling:** Screen viewports are optimized through programmatic responsiveness, scaling flawlessly from compact, short Android hardware to the largest flagship flagships.

---

# 📦 Releases & Versioning

### Current Version:
```text
v2.1

```

### What's New in v2.1:

* **Cinematic About Screen:** Fully responsive glassmorphic landscape detailing application usage.
* **3D Coin-Flip Animation:** Immersive mechanical rotation added to the Developer Showcase.
* **Search Engine Hardening:** Automatic screen-disposal logic added to prevent persistent empty search states.
* **Centralized Design System:** Transitioned all elements to the unified `FoodFinderIcon` registry.
* **Global Device Responsiveness:** Absolute font, padding, and layout fluid adaptation across all Android sizes.
* **Build Optimization:** Upgraded Kotlin compilation parameters and cleaned internal dependency build-trees.

---

# ❤️ Built For KIIT Students

A project inspired by campus life, food culture, and the local spots that define our university years. Designed and engineered for the KIIT community.

~ **Sayak-2328045**

