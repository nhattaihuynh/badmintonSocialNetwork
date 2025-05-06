package com.social.post_service.dto

import com.social.post_service.model.PrivacyLevel
import java.time.LocalDateTime
import java.util.UUID

data class PostResponse(
    val id: String,
    val profileId: UUID,
    val content: String,
    val imageUrls: List<String>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val likes: Int,
    val likedBy: List<String>,
    val commentCount: Int,
    val shareCount: Int,
    val location: String?,
    val feeling: String?,
    val privacyLevel: PrivacyLevel
)