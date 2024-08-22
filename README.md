
# StreamFlix

StreamFlix is a streaming application that allows users to browse, search, and stream movies and TV shows. Built with a robust backend powered by Spring Boot and a dynamic frontend using a modern JavaScript framework, StreamFlix delivers a seamless streaming experience.

## Features

- Browse and search movies and TV shows
- Stream videos in high quality
- User authentication and profile management
- Personalized recommendations
- Responsive UI for both desktop and mobile devices

## Technologies Used

- **Backend:**
  - Spring Boot
  - Spring Security
  - JPA/Hibernate
  - MySQL/PostgreSQL
- **Frontend:**
  - JavaScript (React/Vue/Angular)
  - HTML/CSS
  - Bootstrap/Material-UI
- **Streaming:**
  - HLS/DASH for video streaming
  - FFmpeg for video processing

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Node.js 14+ (for frontend development)
- MySQL 8+ or PostgreSQL 12+

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/wexad/StreamFlix.git
   ```
2. **Backend Setup:**
   - Navigate to the backend directory:
     ```bash
     cd StreamFlix/backend
     ```
   - Configure the `application.properties` file with your database credentials and streaming settings.
   - Run the application:
     ```bash
     mvn spring-boot:run
     ```

3. **Frontend Setup:**
   - Navigate to the frontend directory:
     ```bash
     cd ../frontend
     ```
   - Install dependencies:
     ```bash
     npm install
     ```
   - Start the development server:
     ```bash
     npm start
     ```

### Running the Application

Once both backend and frontend are running, access the application in your browser:

```
http://localhost:3000
```

### API Documentation

API documentation is available via Swagger. Once the backend is running, visit:

```
http://localhost:8080/swagger-ui.html
```

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request. For major changes, it’s recommended to open an issue first to discuss the proposed changes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Download

You can download this project directly from GitHub:

[StreamFlix - GitHub Repository](https://github.com/wexad/StreamFlix.git)
