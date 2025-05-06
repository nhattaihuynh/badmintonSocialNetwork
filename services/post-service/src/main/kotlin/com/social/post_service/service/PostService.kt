package com.social.post_service.service

import com.social.post_service.dto.PostRequest
import com.social.post_service.dto.PostResponse
import com.social.post_service.mapper.PostMapper
import com.social.post_service.model.Post
import com.social.post_service.repository.PostRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PostService(
    private val postRepository: PostRepository,
    private val postMapper: PostMapper
) {
    fun createPost(postRequest: PostRequest): PostResponse {
        val post = postMapper.toEntity(postRequest)
        val savedPost = postRepository.save(post)
        return postMapper.toResponse(savedPost)
    }
    
    fun getPostById(id: String): PostResponse? {
        val objectId = try {
            ObjectId(id)
        } catch (e: Exception) {
            return null
        }
        
        val post = postRepository.findById(objectId).orElse(null) ?: return null
        return postMapper.toResponse(post)
    }
    
    fun getPostsByProfileId(profileId: UUID): List<PostResponse> {
        val posts = postRepository.findByProfileId(profileId)
        return posts.map { postMapper.toResponse(it) }
    }
}