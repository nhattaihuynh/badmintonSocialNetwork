# all services in project
1. main-contract-service
2. authen-service
3. profile-service
4. post-service
5. comment-service
6. reaction-service
7. friendship-service
8. chat-service
9. notification-service
10. search-service
11. media-upload-service
12. api-gateway
13. discovery-service
14. fanpage-service
15. config-server
16. newsfeed-service

# core infrastructure services
1. discovery service (netflix eureka)

  tech: spring cloud netflix eureka

  purpose: service registration and discovery for dynamic scaling.

2. api gateway

  tech: spring cloud gateway

  purpose: route requests, enforce security (jwt validation), rate limiting, and load balancing.

3. config server

  tech: spring cloud config server

  purpose: centralized configuration management (git/consul).
  
# functional services

1. authentication service

  tech: spring security, jwt, oauth2, redis (token blacklist)

  purpose: user registration, login, token generation, and oauth2 integration.

2. profile service

  tech: spring data jpa, postgresql, hibernate, flyway (database migrations)

  purpose: manage user profiles (crud), personal details

3. post service

  tech: spring data jpa, mongodb (for flexible post content), kafka (event-driven updates)

  purpose: create, edit, delete posts; handle privacy and tagging.

4. comment service

  tech: spring data mongodb, couchbase (nested comments)

  purpose: manage comments on posts, replies, and deletion.

5. reaction service

  tech: mongodb (for high write throughput), redis (caching frequent reactions)

  purpose: track likes, emojis, and other reactions on posts/comments.

6. friendship service

  tech: neo4j (graph db for relationships), kotlin

  purpose: manage friend requests, followers, and relationship statuses.

7. chat service

  tech: websocket/stomp (spring websocket), redis (pub/sub for messaging)

  purpose: real-time chat, message persistence, and typing indicators.

8. notification service

  tech: kafka (event streaming), firebase cloud messaging (push notifications)

  purpose: send real-time notifications (likes, comments, friend requests).

9. search service

  tech: elasticsearch (full-text search), redis (query caching), kotlin

  purpose: search users, posts, and pages with filters.

10. media upload service

  tech: aws s3/minio storage, spring webflux (async upload), kotlin

  purpose: upload images/videos, generate thumbnails, and optimize storage.

11. fanpage service

  tech: spring data jpa, postgresql, kafka (for page updates)

  purpose: manage pages, admin roles, and page-related posts.

12. newsfeed service

  tech: redis (caching feed data), kafka (aggregate events), cassandra (time-series data)

  purpose: generate personalized feeds using ranking algorithms (e.g., user interests).
  
# additional tech stack

## databases:

relational: postgresql (acid compliance for profiles/friendships).

nosql: mongodb (flexible schemas for posts/comments), cassandra (scalable feeds).

graph: neo4j (friend recommendations).

cache: redis (session storage, reaction counts).


## inter-service communication:

rest (sync): openfeign/resttemplate/webclient.

async: apache kafka/rabbitmq (event-driven updates).

## security:

spring security oauth2, jwt, vault (secrets management).

## deployment:

docker, kubernetes (orchestration), aws eks/google gke.

## monitoring:

prometheus + grafana (metrics), sleuth/zipkin (distributed tracing), elk stack (logging).

## testing:

junit, mockito, testcontainers (integration tests).

## api docs:

swagger/openapi.
