package com.social.post_service.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document("post")
data class Post(
    @Id
    val id: ObjectId? = null,
    val profileId: UUID,
    val content: String,
    val imageUrls: List<String> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    val likes: Int = 0,
    val likedBy: List<String> = emptyList(),
    val commentCount: Int = 0,
    val shareCount: Int = 0,
    val location: String? = null,
    val feeling: String? = null,
    val privacyLevel: PrivacyLevel = PrivacyLevel.PUBLIC
)

enum class PrivacyLevel {
    PUBLIC,
    FRIENDS,
    PRIVATE
}