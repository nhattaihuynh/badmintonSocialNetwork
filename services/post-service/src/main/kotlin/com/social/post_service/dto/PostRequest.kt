package com.social.post_service.dto

import com.social.post_service.model.PrivacyLevel
import java.util.UUID

data class PostRequest(
    val profileId: UUID,
    val content: String,
    val imageUrls: List<String> = emptyList(),
    val location: String? = null,
    val feeling: String? = null,
    val privacyLevel: PrivacyLevel = PrivacyLevel.PUBLIC
)