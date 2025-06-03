package com.social.friendship_service.nodes

import org.springframework.data.neo4j.core.schema.*
import java.time.LocalDateTime
import java.util.UUID

@Node("Profile")
data class Profile(
    @Id @GeneratedValue
    val id: UUID = UUID.randomUUID(),
    
    @Property("profileId")
    val profileId: String,
    
    @Property("username")
    val username: String,
    
    @Property("fullName")
    val fullName: String,
    
    @Relationship(type = "FRIEND", direction = Relationship.Direction.OUTGOING)
    val friends: MutableSet<Profile> = mutableSetOf(),

    @Property("createdAt")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Property("updatedAt")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
