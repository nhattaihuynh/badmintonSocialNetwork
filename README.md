# Badminton Social Network - Microservices Architecture

## Project Overview

A distributed microservices-based social network platform for badminton enthusiasts, built with Spring Boot, Spring Cloud, and various databases.

## Services Status

### ✅ Implemented Services

1. **Profile Service**
   - Tech: Spring Boot 3.4.4, Spring Data JPA, PostgreSQL, Flyway, Java 21
   - Purpose: Manage user profiles (CRUD), personal details
   - Endpoints:
     - `GET /api/profile/{id}` - Get profile by ID
     - `POST /api/profile` - Create new profile

2. **Post Service**
   - Tech: Spring Data JPA, MongoDB, Kafka, Kotlin, Spring Boot 3.5.0
   - Purpose: Create, read, delete posts; handle privacy and tagging
   - Endpoints:
     - `POST /api/posts` - Create a new post
     - `GET /api/posts/{id}` - Get post by ID
     - `GET /api/posts/profile/{profileId}` - Get posts by profile ID

3. **Comment Service**
   - Tech: Spring Data JPA, PostgreSQL, Java
   - Purpose: Manage comments on posts and replies
   - Endpoints:
     - `POST /comments` - Create a new comment
     - `GET /comments/{postId}/comments-with-replies` - Get comments with replies for a post

4. **Friendship Service**
   - Tech: Neo4j, Kotlin, Spring Boot 3.5.0, Kafka integration
   - Purpose: Manage friend requests, followers, and relationship statuses using graph database
   - Key Features:
     - Graph-based relationship tracking
     - Friend request management
     - Mutual friend detection
   - Endpoints:
     - `POST /friendship/request/send` - Send friend request (params: sourceProfileId, targetProfileId)
     - `POST /friendship/request/accept` - Accept friend request (params: profileId, friendProfileId)
     - `GET /friendship/request/incoming` - Get incoming friend requests (params: profileId)
     - `GET /friendship/friends` - Get all friends (params: profileId)
     - `GET /friendship/mutual` - Get mutual friends (params: profileId1, profileId2)

5. **Kafka Common Service**
   - Tech: Spring Kafka, Spring Boot 3.5.3, Java 21
   - Purpose: Shared Kafka configurations and common messaging utilities

### 🔧 Core Infrastructure Services

1. **API Gateway**
   - Tech: Spring Cloud Gateway
   - Purpose: Route requests, enforce security (JWT validation), rate limiting, and load balancing

2. **Discovery Service**
   - Tech: Spring Cloud Netflix Eureka
   - Purpose: Service registration and discovery for dynamic scaling

3. **Config Server**
   - Tech: Spring Cloud Config Server
   - Purpose: Centralized configuration management

4. **Main Contract Service**
   - Tech: Spring Boot, Java 21
   - Purpose: Shared data models and DTOs across services

### 🚀 In Development

5. **Media Upload Service**
   - Tech: AWS S3/Minio, Spring WebFlux, Kotlin
   - Purpose: Async image/video upload, thumbnail generation, storage optimization
   - Status: Controller structure in place, implementation in progress

### 📋 Planned Services (Not Started)

- **Authentication Service** - JWT, OAuth2, Spring Security, Redis token blacklist
- **Reaction Service** - MongoDB, Redis caching for likes/emojis
- **Chat Service** - WebSocket/STOMP, Redis pub/sub
- **Notification Service** - Kafka events, Firebase Cloud Messaging
- **Search Service** - Elasticsearch, Redis caching
- **Fanpage Service** - Pages management with admin roles
- **Newsfeed Service** - Personalized feeds with Redis caching and Cassandra

## Technology Stack

### Languages & Frameworks
- **Java 21** - Primary language
- **Kotlin** - Used in: Friendship Service, Post Service, Media Upload Service, Search Service
- **Spring Boot** - Framework (versions 3.4.4 - 3.5.3)
- **Spring Cloud** - Microservices infrastructure (Eureka, Gateway, Config)

### Databases
- **PostgreSQL** - Profile Service, Comment Service (ACID compliance)
- **Neo4j** - Friendship Service (graph relationships)
- **MongoDB** - Post Service (flexible schemas)
- **Redis** - Caching, session storage (planned for reactions, chat)
- **Cassandra** - Newsfeed Service (time-series, planned)

### Message Queue & Event Streaming
- **Apache Kafka** - Inter-service async communication

### Build & Deployment
- **Maven** - Build tool
- **Docker** - Containerization (planned)
- **Kubernetes** - Orchestration (planned)

## Project Structure

```
badmintonSocialNetwork/
├── services/
│   ├── api-gateway/                 (Spring Cloud Gateway)
│   ├── comment-service/             (PostgreSQL, Java)
│   ├── config-server/               (Spring Cloud Config)
│   ├── discovery-service/           (Netflix Eureka)
│   ├── friendship-service/          (Neo4j, Kotlin)
│   ├── kafka-common-service/        (Kafka Commons)
│   ├── main-contract-service/       (Shared DTOs)
│   ├── media-upload-service/        (In Progress - Kotlin)
│   ├── post-service/                (MongoDB, Kotlin)
│   └── profile-service/             (PostgreSQL, Java)
├── scripts/
└── README.md
```

## Next Steps

1. Complete Media Upload Service APIs
2. Implement Authentication Service with JWT/OAuth2
3. Add Reaction Service for likes and emojis
4. Implement Real-time Chat Service
5. Build Notification System with Kafka + Firebase
6. Create Search Service with Elasticsearch
7. Develop Personalized Newsfeed Service
8. Add API Documentation (OpenAPI/Swagger)
9. Implement Docker & Kubernetes deployment configs

