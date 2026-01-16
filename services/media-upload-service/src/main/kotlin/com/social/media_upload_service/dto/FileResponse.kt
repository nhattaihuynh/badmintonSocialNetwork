package com.social.media_upload_service.dto

import java.time.LocalDateTime

data class FileResponse(
    val id: String,
    val fileName: String,
    val originalFileName: String,
    val fileSize: Long,
    val mimeType: String,
    val filePath: String,
    val uploadedBy: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val fileExtension: String,
    val isPublic: Boolean,
    val description: String?,
    val tags: List<String>
)

