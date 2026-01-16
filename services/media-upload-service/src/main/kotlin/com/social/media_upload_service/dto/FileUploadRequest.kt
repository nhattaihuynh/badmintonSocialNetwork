package com.social.media_upload_service.dto

data class FileUploadRequest(
    val description: String? = null,
    val tags: List<String> = emptyList(),
    val isPublic: Boolean = false
)


