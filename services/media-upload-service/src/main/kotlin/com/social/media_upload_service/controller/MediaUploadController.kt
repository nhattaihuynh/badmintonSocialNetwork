package com.social.media_upload_service.controller

import com.social.media_upload_service.dto.FileResponse
import com.social.media_upload_service.dto.FileUploadRequest
import com.social.media_upload_service.service.MediaUploadService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/media")
class MediaUploadController(
    private val mediaUploadService: MediaUploadService,
) {

    /**
     * Upload a media file
     * POST /api/media/upload
     * @param file The file to upload
     * @param uploadedBy The profile ID of the user uploading the file
     * @param uploadRequest Additional metadata for the file
     * @return FileResponse with file details
     */
    @PostMapping("/upload")
    fun uploadFile(
        @RequestParam file: MultipartFile,
        @RequestParam uploadedBy: String,
        @RequestBody(required = false) uploadRequest: FileUploadRequest? = null
    ): ResponseEntity<FileResponse> {
        val request = uploadRequest ?: FileUploadRequest()
        val uploadedFile = mediaUploadService.uploadFile(file, uploadedBy, request)
        return ResponseEntity(uploadedFile, HttpStatus.CREATED)
    }

    /**
     * Get media file metadata by ID
     * GET /api/media/{id}
     * @param id The file ID
     * @return FileResponse with file details
     */
    @GetMapping("/{id}")
    fun getFileById(@PathVariable id: String): ResponseEntity<FileResponse> {
        val file = mediaUploadService.getFileById(id)
        return if (file != null) {
            ResponseEntity(file, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Get all media files uploaded by a specific profile
     * GET /api/media/profile/{uploadedBy}
     * @param uploadedBy The profile ID of the profile
     * @return List of FileResponse
     */
    @GetMapping("/profile/{uploadedBy}")
    fun getFilesByUploadedBy(@PathVariable uploadedBy: String): ResponseEntity<List<FileResponse>> {
        val files = mediaUploadService.getFilesByUploadedBy(uploadedBy)
        return ResponseEntity(files, HttpStatus.OK)
    }

    /**
     * Get all public media files
     * GET /api/media/public
     * @return List of public FileResponse
     */
    @GetMapping("/public")
    fun getPublicFiles(): ResponseEntity<List<FileResponse>> {
        val files = mediaUploadService.getPublicFiles()
        return ResponseEntity(files, HttpStatus.OK)
    }

    /**
     * Get all private media files for a specific user
     * GET /api/media/private/{uploadedBy}
     * @param uploadedBy The profile ID of the user
     * @return List of private FileResponse
     */
    @GetMapping("/private/{uploadedBy}")
    fun getPrivateFilesByUploadedBy(@PathVariable uploadedBy: String): ResponseEntity<List<FileResponse>> {
        val files = mediaUploadService.getPrivateFilesByUploadedBy(uploadedBy)
        return ResponseEntity(files, HttpStatus.OK)
    }

    /**
     * Search files by tag
     * GET /api/media/search/tag
     * @param tag The tag to search for
     * @return List of FileResponse with the tag
     */
    @GetMapping("/search/tag")
    fun getFilesByTag(@RequestParam tag: String): ResponseEntity<List<FileResponse>> {
        val files = mediaUploadService.getFilesByTag(tag)
        return ResponseEntity(files, HttpStatus.OK)
    }

    /**
     * Delete a media file
     * DELETE /api/media/{id}
     * @param id The file ID
     * @param uploadedBy The profile ID of the user (for ownership verification)
     * @return Success or failure response
     */
    @DeleteMapping("/{id}")
    fun deleteFile(
        @PathVariable id: String,
        @RequestParam uploadedBy: String
    ): ResponseEntity<Map<String, String>> {
        val deleted = mediaUploadService.deleteFile(id, uploadedBy)
        return if (deleted) {
            ResponseEntity(mapOf("message" to "File deleted successfully"), HttpStatus.OK)
        } else {
            ResponseEntity(mapOf("message" to "Failed to delete file or unauthorized"), HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * Update file metadata (description, tags, isPublic)
     * PUT /api/media/{id}
     * @param id The file ID
     * @param uploadedBy The profile ID of the user (for ownership verification)
     * @param updateRequest Updated metadata
     * @return Updated FileResponse
     */
    @PutMapping("/{id}")
    fun updateFileMetadata(
        @PathVariable id: String,
        @RequestParam uploadedBy: String,
        @RequestBody updateRequest: FileUploadRequest
    ): ResponseEntity<FileResponse> {
        val updatedFile = mediaUploadService.updateFileMetadata(id, uploadedBy, updateRequest)
        return if (updatedFile != null) {
            ResponseEntity(updatedFile, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}