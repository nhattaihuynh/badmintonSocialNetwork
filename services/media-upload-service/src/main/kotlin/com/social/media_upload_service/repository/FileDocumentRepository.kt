package com.social.media_upload_service.repository

import com.social.media_upload_service.document.FileDocument
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FileDocumentRepository : MongoRepository<FileDocument, String> {
    /**
     * Find all files uploaded by a specific user
     * @param uploadedBy The profile ID of the uploader
     * @return List of FileDocuments uploaded by the user
     */
    fun findByUploadedBy(uploadedBy: String): List<FileDocument>

    /**
     * Find all public files
     * @return List of public FileDocuments
     */
    fun findByIsPublicTrue(): List<FileDocument>

    /**
     * Find all private files uploaded by a specific user
     * @param uploadedBy The profile ID of the uploader
     * @return List of private FileDocuments
     */
    fun findByUploadedByAndIsPublicFalse(uploadedBy: String): List<FileDocument>

    /**
     * Find files by tag
     * @param tag The tag to search for
     * @return List of FileDocuments with the tag
     */
    fun findByTagsContaining(tag: String): List<FileDocument>
}

