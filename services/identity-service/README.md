# Profile Service

This microservice is part of the Badminton Social Network application. It manages user profiles with badminton-specific attributes.

## Features

- Create, read, update, and delete user profiles
- Search profiles by skill level, play style, location, and keywords
- Find nearby players for matches
- Get player recommendations for mentorship and practice
- RESTful API for integration with other services
- Secured endpoints

## Tech Stack

- Java 21
- Spring Boot 3.4.4
- Spring Data JPA
- PostgreSQL
- Flyway for database migrations
- Spring Cloud for microservice integration
- JUnit 5 for testing

## API Endpoints

### Profile Management

- `GET /api/profiles` - Get all profiles
- `GET /api/profiles/{id}` - Get profile by ID
- `GET /api/profiles/username/{username}` - Get profile by username
- `POST /api/profiles` - Create a new profile
- `PUT /api/profiles/{id}` - Update an existing profile
- `DELETE /api/profiles/{id}` - Delete a profile

### Search Functionality

- `GET /api/profiles/search/skill-level/{skillLevel}` - Find profiles by skill level
- `GET /api/profiles/search/play-style/{playStyle}` - Find profiles by play style
- `GET /api/profiles/search` - Advanced search with multiple parameters
  - Query parameters:
    - `skillLevel` - Filter by skill level (BEGINNER, INTERMEDIATE, ADVANCED, PROFESSIONAL)
    - `playStyle` - Filter by play style (SINGLES, DOUBLES, MIXED, ALL)
    - `location` - Filter by preferred location
    - `keyword` - Search in username, full name, and bio

### Social Features

- `GET /api/profiles/nearby` - Find nearby players
  - Query parameters:
    - `location` - The location to search near
    - `limit` - Maximum number of results (default: 10)
    
- `GET /api/profiles/{id}/similar` - Find players with similar skill level
  - Query parameters:
    - `limit` - Maximum number of results (default: 5)
    
- `GET /api/profiles/recommendations/{id}` - Get player recommendations for mentorship
  - Query parameters:
    - `limit` - Maximum number of results (default: 5)

## Running the Service

### Prerequisites

- Java 21
- PostgreSQL
- Maven

### Setup

1. Create a PostgreSQL database named `badminton_profile_db`
2. Configure database credentials in `application.yml` if needed
3. Build the project:
   ```
   mvn clean install
   ```
4. Run the service:
   ```
   mvn spring-boot:run
   ```

## Testing

Run the tests using:
```
mvn test
```

## Docker Support

Build a Docker image:
```
docker build -t badminton-identity-service .
```

Run the container:
```
docker run -p 8081:8081 badminton-identity-service
```
