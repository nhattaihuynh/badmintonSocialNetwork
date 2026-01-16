package com.social.media_upload_service.service

import com.social.media_upload_service.document.FileDocument
import com.social.media_upload_service.dto.FileResponse
import com.social.media_upload_service.dto.FileUploadRequest
import com.social.media_upload_service.repository.FileDocumentRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.UUID

@Service
class MediaUploadService(
    private val fileDocumentRepository: FileDocumentRepository
) {

    /**
     * Upload a new media file
     * @param file The file to upload
     * @param uploadedBy The profile ID of the uploader
     * @param uploadRequest Metadata for the file
     * @return FileResponse with uploaded file details
     */
    fun uploadFile(
        file: MultipartFile,
        uploadedBy: String,
        uploadRequest: FileUploadRequest
    ): FileResponse {
        // Generate unique file ID
        val fileId = UUID.randomUUID().toString()

        // Extract file extension
        val originalFileName = file.originalFilename ?: "file"
        val fileExtension = originalFileName.substringAfterLast(".", "")
        val storedFileName = "$fileId.$fileExtension"

        // Create file path (can be adjusted based on storage strategy)
        val filePath = "uploads/$uploadedBy/$storedFileName"

        // Create FileDocument
        val fileDocument = FileDocument(
            id = fileId,
            fileName = storedFileName,
            originalFileName = originalFileName,
            fileSize = file.size,
            mimeType = file.contentType ?: "application/octet-stream",
            filePath = filePath,
            uploadedBy = uploadedBy,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            fileExtension = fileExtension,
            isPublic = uploadRequest.isPublic,
            description = uploadRequest.description,
            tags = uploadRequest.tags
        )

        // TODO: Save file to storage (S3, Minio, local filesystem)
        // For now, just save metadata to database

        // Save FileDocument to MongoDB repository
        val savedDocument = fileDocumentRepository.save(fileDocument)

        return savedDocument.toResponse()
    }

    /**
     * Retrieve file metadata by ID
     * @param fileId The file ID
     * @return FileResponse if found, null otherwise
     */
    fun getFileById(fileId: String): FileResponse? {
        return fileDocumentRepository.findById(fileId)
            .map { it.toResponse() }
            .orElse(null)
    }

    /**
     * Get all files uploaded by a specific user
     * @param uploadedBy The profile ID of the uploader
     * @return List of FileResponse
     */
    fun getFilesByUploadedBy(uploadedBy: String): List<FileResponse> {
        return fileDocumentRepository.findByUploadedBy(uploadedBy)
            .map { it.toResponse() }
    }

    /**
     * Get all public files
     * @return List of public FileResponse
     */
    fun getPublicFiles(): List<FileResponse> {
        return fileDocumentRepository.findByIsPublicTrue()
            .map { it.toResponse() }
    }

    /**
     * Get all private files of a user
     * @param uploadedBy The profile ID of the uploader
     * @return List of private FileResponse
     */
    fun getPrivateFilesByUploadedBy(uploadedBy: String): List<FileResponse> {
        return fileDocumentRepository.findByUploadedByAndIsPublicFalse(uploadedBy)
            .map { it.toResponse() }
    }

    /**
     * Get files by tag
     * @param tag The tag to search for
     * @return List of FileResponse with the tag
     */
    fun getFilesByTag(tag: String): List<FileResponse> {
        return fileDocumentRepository.findByTagsContaining(tag)
            .map { it.toResponse() }
    }

    /**
     * Delete a file
     * @param fileId The file ID
     * @param uploadedBy The profile ID of the uploader (for ownership verification)
     * @return Boolean indicating success
     */
    fun deleteFile(fileId: String, uploadedBy: String): Boolean {
        return fileDocumentRepository.findById(fileId)
            .takeIf { it.uploadedBy == uploadedBy }
            ?.let { fileDocument ->
                // TODO: Delete file from storage
                fileDocumentRepository.delete(fileDocument)
                true
            }
            ?: false
    }

    /**
     * Update file metadata
     * @param fileId The file ID
     * @param uploadedBy The profile ID of the uploader (for ownership verification)
     * @param updateRequest Updated metadata
     * @return Updated FileResponse if successful, null otherwise
     */
    fun updateFileMetadata(
        fileId: String,
        uploadedBy: String,
        updateRequest: FileUploadRequest
    ): FileResponse? {
        return fileDocumentRepository.findById(fileId)
            .takeIf { it.uploadedBy == uploadedBy }
            ?.let { fileDocument ->
                val updatedDocument = fileDocument.copy(
                    description = updateRequest.description,
                    tags = updateRequest.tags,
                    isPublic = updateRequest.isPublic,
                    updatedAt = LocalDateTime.now()
                )
                fileDocumentRepository.save(updatedDocument)
                updatedDocument.toResponse()
            }
    }

    /**
     * Convert FileDocument to FileResponse
     */
    private fun FileDocument.toResponse(): FileResponse {
        return FileResponse(
            id = this.id,
            fileName = this.fileName,
            originalFileName = this.originalFileName,
            fileSize = this.fileSize,
            mimeType = this.mimeType,
            filePath = this.filePath,
            uploadedBy = this.uploadedBy,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            fileExtension = this.fileExtension,
            isPublic = this.isPublic,
            description = this.description,
            tags = this.tags
        )
    }
}