# Badminton Social Network - Microservices Architecture

## Project Overview

A distributed microservices-based social network platform for badminton enthusiasts, built with Spring Boot, Spring Cloud, and various databases.

## Services Status

### ✅ Implemented Services

1. **Profile Service**
   - Tech: Spring Boot 3.4.4, Spring Data JPA, PostgreSQL, Flyway, Java 21
   - Purpose: Manage user profiles (CRUD), personal details

2. **Post Service**
   - Tech: Spring Data JPA, MongoDB, Kafka, Kotlin, Spring Boot 3.5.0
   - Purpose: Create, read, delete posts; handle privacy and tagging

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

### ✅ Implemented Services (Continued)

5. **Media Upload Service**
   - Tech: Spring Data MongoDB, Spring Boot 3.5.7, Kotlin, Java 21
   - Purpose: File upload management, media metadata storage, access control
   - Database: MongoDB (files collection)

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
- **MongoDB** - Post Service (flexible schemas), Media Upload Service (file metadata)
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
│   ├── notification-service/        (Planned)
│   ├── post-service/                (MongoDB, Kotlin)
│   ├── profile-service/             (PostgreSQL, Java)
│   ├── reaction-service/            (Planned)
│   ├── search-service/              (Planned)
│   ├── newsfeed-service/            (Planned)
│   ├── fanpage-service/             (Planned)
│   ├── chat-service/                (Planned)
│   └── authentication-service/      (Planned)
│   ├── media-upload-service/        (MongoDB, Kotlin - Document Model Complete)
│   ├── post-service/                (MongoDB, Kotlin)
│   └── profile-service/             (PostgreSQL, Java)
├── scripts/
└── README.md
```

