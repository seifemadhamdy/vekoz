# 🎬 Vekoz

A modern Android application that showcases movies using The Movie Database (TMDB) API. Built with the latest Android development practices and Material Design 3, this app allows users to discover popular movies, search for specific titles, and manage their personal watchlist.

<table style="width: 100%; text-align: center;">
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/22f3dcb3-e8cc-4a8c-8710-2db82b7395b0" alt="Error Light"><br>
      <strong>Error Screen (Light)</strong>
    </td>
    <td>
      <img src="https://github.com/user-attachments/assets/5f6f950b-1eeb-4bb1-9cee-087241f43b17" alt="Home Light"><br>
      <strong>Home Screen (Light)</strong>
    </td>
    <td>
      <img src="https://github.com/user-attachments/assets/156ec139-a2f8-4b4c-9580-297a4eefc4a0" alt="Movie Details Light"><br>
      <strong>Movie Details Screen (Light)</strong>
    </td>
  </tr>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/c0d7c974-d471-4124-877d-f3439539d989" alt="Error Dark"><br>
      <strong>Error Screen (Dark)</strong>
    </td>
    <td>
      <img src="https://github.com/user-attachments/assets/07148664-e713-462b-8888-b0f1f63310c0" alt="Home Dark"><br>
      <strong>Home Screen (Dark)</strong>
    </td>
    <td>
      <img src="https://github.com/user-attachments/assets/625dbe1d-e1d0-4f14-9102-94d81ac44361" alt="Movie Details Dark"><br>
      <strong>Movie Details Screen (Dark)</strong>
    </td>
  </tr>
</table>

## ✨ Features

- Browse popular movies with a clean, intuitive interface
- Search movies by title with real-time results
- View detailed movie information including revenue, release date, and status
- Discover similar movies and explore cast information
- Manage your personal watchlist with offline support
- Movies grouped by year for better organization
- Top 5 similar movies for each selected film
- Cast information grouped by department (Actors and Directors)
- Sorted cast lists based on popularity

## 🏗️ Technical Architecture

### Architecture Overview

The application follows Clean Architecture principles with MVVM pattern, ensuring:

- Clear separation of concerns
- High testability
- Scalable and maintainable codebase
- Unidirectional data flow

### Key Technical Decisions

#### UI Layer

- **Jetpack Compose**: Modern declarative UI toolkit for native Android development
- **Material Design 3**: Latest material design guidelines for a polished user experience
- **Compose Navigation**: Type-safe navigation between screens
- **State Flow & Live Data**: Reactive state management
- **Coil**: Efficient image loading and caching

#### Domain Layer

- **Clean Architecture**: Clear separation between data, domain, and presentation layers
- **Use Cases**: Encapsulated business logic
- **Repository Pattern**: Abstract data sources
- **Coroutines**: Asynchronous programming
- **State Flow**: Reactive data streams

#### Data Layer

- **Retrofit**: Type-safe HTTP client for API communication
- **Room Database**: Local data persistence for watchlist
- **GSON**: JSON parsing
- **Repository Implementation**: Single source of truth pattern

#### Dependency Injection

- **Hilt**: Streamlined DI implementation
- **Modular Design**: Easy to test and maintain

## 🔄 Trade-offs & Decisions

1. **Offline-First vs Online-First**

   - Chose online-first approach due to the dynamic nature of movie data
   - Cached essential data for watchlist functionality

2. **Compose vs XML**

   - Selected Compose for modern UI development
   - Faster development and better maintainability
   - Slight learning curve for developers new to declarative UI

3. **State Management**
   - Used combination of StateFlow and LiveData
   - StateFlow for UI states
   - LiveData for lifecycle-aware observations

## 🔜 Future Improvements

1. **Technical**
   - Implement Kotlin Multiplatform
   - Add more unit and integration tests
   - Implement CI/CD pipeline
   - Performance optimization for large lists

## 🧪 Testing

The project includes unit tests using:

- JUnit
- Mockito

## 🛠️ Setup & Installation

1. Clone the repository
2. Change TMDB_ACCESS_TOKEN to yours in `tmdb.properties`:
   ```
   TMDB_ACCESS_TOKEN = "{YOUR_API_KEY}"
   ```
3. Build and run the project

## 👨‍💻 Author

Seif Abu El-Ela
