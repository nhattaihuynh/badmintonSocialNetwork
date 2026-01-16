package com.social.media_upload_service.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "files")
data class FileDocument(
    @Id
    val id: String,
    val fileName: String,
    val originalFileName: String,
    val fileSize: Long,
    val mimeType: String,
    val filePath: String,
    val uploadedBy: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val fileExtension: String,
    val isPublic: Boolean = false,
    val description: String? = null,
    val tags: List<String> = emptyList(),
) {
}