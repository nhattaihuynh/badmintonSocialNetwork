# All services in project
1. Authen-service
2. Profile-service
3. Post-service
4. Comment-service
5. Reaction-service
6. Friendship-service
7. Chat-service
8. Notification-service
9. Search-service
10. Media-upload-service
11. Api-gateway
12. Discovery-service
13. Fanpage-service
14. Config-server
15. NewsFeed-service
 
# Core Infrastructure Services
1. Discovery Service (Netflix Eureka)

  Tech: Spring Cloud Netflix Eureka

  Purpose: Service registration and discovery for dynamic scaling.

2. API Gateway

  Tech: Spring Cloud Gateway

  Purpose: Route requests, enforce security (JWT validation), rate limiting, and load balancing.

3. Config Server

  Tech: Spring Cloud Config Server

  Purpose: Centralized configuration management (Git/Consul).
  
# Functional Services

1. Authentication Service

  Tech: Spring Security, JWT, OAuth2, Redis (token blacklist)

  Purpose: User registration, login, token generation, and OAuth2 integration.

2. Profile Service

  Tech: Spring Data JPA, PostgreSQL, Hibernate

  Purpose: Manage user profiles (CRUD), personal details, and privacy settings.

3. Post Service

  Tech: Spring Data JPA, MongoDB (for flexible post content), Kafka (event-driven updates)

  Purpose: Create, edit, delete posts; handle privacy and tagging.

4. Comment Service

  Tech: Spring Data MongoDB, Couchbase (nested comments)

  Purpose: Manage comments on posts, replies, and deletion.

5. Reaction Service

  Tech: MongoDB (for high write throughput), Redis (caching frequent reactions)

  Purpose: Track likes, emojis, and other reactions on posts/comments.

6. Friendship Service

  Tech: Neo4j (graph DB for relationships), Kotlin

  Purpose: Manage friend requests, followers, and relationship statuses.

7. Chat Service

  Tech: WebSocket/STOMP (Spring WebSocket), Redis (pub/sub for messaging)

  Purpose: Real-time chat, message persistence, and typing indicators.

8. Notification Service

  Tech: Kafka (event streaming), Firebase Cloud Messaging (push notifications)

  Purpose: Send real-time notifications (likes, comments, friend requests).

9. Search Service

  Tech: Elasticsearch (full-text search), Redis (query caching), Kotlin

  Purpose: Search users, posts, and pages with filters.

10. Media Upload Service

  Tech: AWS S3/MinIO Storage, Spring WebFlux (async upload), Kotlin

  Purpose: Upload images/videos, generate thumbnails, and optimize storage.

11. Fanpage Service

  Tech: Spring Data JPA, PostgreSQL, Kafka (for page updates)

  Purpose: Manage pages, admin roles, and page-related posts.

12. NewsFeed Service

  Tech: Redis (caching feed data), Kafka (aggregate events), Cassandra (time-series data)

  Purpose: Generate personalized feeds using ranking algorithms (e.g., user interests).
  
# Additional Tech Stack

## Databases:

Relational: PostgreSQL (ACID compliance for profiles/friendships).

NoSQL: MongoDB (flexible schemas for posts/comments), Cassandra (scalable feeds).

Graph: Neo4j (friend recommendations).

Cache: Redis (session storage, reaction counts).


## Inter-Service Communication:

REST (sync): OpenFeign/RestTemplate/WebClient.

Async: Apache Kafka/RabbitMQ (event-driven updates).

## Security:

Spring Security OAuth2, JWT, Vault (secrets management).

## Deployment:

Docker, Kubernetes (orchestration), AWS EKS/Google GKE.

## Monitoring:

Prometheus + Grafana (metrics), Sleuth/Zipkin (distributed tracing), ELK Stack (logging).

## Testing:

JUnit, Mockito, Testcontainers (integration tests).

## API Docs:

Swagger/OpenAPI.
