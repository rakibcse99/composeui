# WhatsAppClone

A modern Android application that replicates core messaging functionalities using Jetpack Compose, Kotlin, and Room database. This project is built with Clean Architecture principles, leveraging modern Android development practices for a scalable and maintainable codebase.

## Architecture

This project follows **Clean Architecture** to ensure separation of concerns and scalability. Key components include:

- **Layers**:
  - **Domain**: Contains business logic and data models (`User`, `Message`, `Call`).
  - **Data**: Manages data sources (Room database).
  - **Presentation**: Handles UI and state management using Jetpack Compose and ViewModel.
- **Dependency Injection**: Uses **Hilt** for dependency injection, simplifying the management of dependencies across the app (e.g., ViewModels, Repositories).
- **State Management**: Uses `ViewModel` with `StateFlow` and `MutableState` for reactive UI updates.
- **Navigation**: Implements `Navigation Compose` for seamless screen transitions.
- **UI**: Built entirely with **Jetpack Compose**, featuring modern, declarative UI design.
- **Database**: Utilizes **Room** for local data persistence (e.g., storing messages and user data).
- **Animations**: Incorporates animations (e.g., message fade-in, scrolling effects) using Compose’s animation APIs.

## Features

- **Chats**: View a list of users and engage in one-on-one messaging.
- **Calls**: Display a call log with outgoing, incoming, and missed calls.
- **Search**: Search for users and navigate to their chat screen.
- **File Sharing**: Send and receive PDFs and images in chats, with clickable links to open files.
- **Custom Navigation**: Hide app bars and bottom navigation on specific screens (Chat, Calls, Search).
- **Animations**: Smooth UI transitions and effects (e.g., message animations, scroll-to-bottom).

## Prerequisites

Before setting up the project, ensure you have the following:

- **Android Studio**: Version 2023.1.1 or later (e.g., Flamingo, Giraffe, or Hedgehog).
- **Kotlin**: Version 1.9.0 or higher.
- **Device/Emulator**: Android API 24 or higher.

## Setup Instructions

Follow these steps to get the project running:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/WhatsAppClone.git
   cd WhatsAppClone
   ```

2. **Set Up Hilt**:
   - Ensure the Hilt Gradle plugin is applied in the project. The project already includes Hilt dependencies:
     - `com.google.dagger:hilt-android` for runtime.
     - `com.google.dagger:hilt-compiler` for annotation processing.
     - `androidx.hilt:hilt-navigation-compose` for Jetpack Compose navigation support.
   - Verify that the `MainActivity` is annotated with `@AndroidEntryPoint`.

3. **Sync and Build**:
   - Open the project in Android Studio.
   - Sync the project with Gradle (click "Sync Project with Gradle Files").
   - Build the project (Build > Rebuild Project).

4. **Run the App**:
   - Run the app on an emulator or device with API 24+.
   - Grant permissions for media access when prompted (Android 13+ requires `READ_MEDIA_IMAGES` or equivalent).

## Usage

Here’s how to use the app’s core features:

1. **Chats Screen**:
   - View a list of users.
   - Click on a user to start a chat.
   - Send text messages or attach files (PDFs, images).

2. **Calls Screen**:
   - View recent calls (simulated data).
   - Add favorites (placeholder functionality).

3. **Search Screen**:
   - Click the search icon in the main app bar.
   - Search for users by name.
   - Click a user to navigate to their chat screen.

## Project Structure

The project is organized as follows:

```
WhatsAppClone/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/rakib/composeui/
│   │   │   │   ├── domain/            # Data models and business logic
│   │   │   │   ├── data/              # Data sources (Room)
│   │   │   │   ├── presentation/      # UI screens and ViewModel
│   │   │   │   │   ├── screens/       # Composable screens (Chats, Calls, etc.)
│   │   │   │   │   └── viewmodel/     # ViewModel with StateFlow
│   │   │   │   ├── ui/theme/          # Jetpack Compose theme
│   │   │   │   └── MainActivity.kt    # Main entry point and navigation
│   │   │   ├── res/                   # Resources (drawables, colors, etc.)
│   │   │   └── AndroidManifest.xml
│   └── build.gradle                   # App-level Gradle configuration
└── build.gradle                       # Project-level Gradle configuration
```

## Screenshots

Below are some screenshots of the app in action:

- **Chats Screen**  
  ![Chats Screen](https://raw.githubusercontent.com/rakibcse99/composeui/refs/heads/master/app/src/main/res/drawable/image1.jpeg)

- **Chat Screen**  
  ![Chat Screen](https://raw.githubusercontent.com/rakibcse99/composeui/refs/heads/master/app/src/main/res/drawable/image2.jpeg)

- **Search Screen**  
  ![Search Screen](https://raw.githubusercontent.com/rakibcse99/composeui/refs/heads/master/app/src/main/res/drawable/image3.jpeg)



## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

*Last updated: June 01, 2025*
