package com.social.post_service.controller

import com.social.post_service.dto.PostRequest
import com.social.post_service.dto.PostResponse
import com.social.post_service.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService
) {
    @PostMapping
    fun createPost(@RequestBody postRequest: PostRequest): ResponseEntity<PostResponse> {
        val createdPost = postService.createPost(postRequest)
        return ResponseEntity(createdPost, HttpStatus.CREATED)
    }
    
    @GetMapping("/{id}")
    fun getPostById(@PathVariable id: String): ResponseEntity<PostResponse> {
        val post = postService.getPostById(id)
        return if (post != null) {
            ResponseEntity(post, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
    
    @GetMapping("/profile/{profileId}")
    fun getPostsByProfileId(@PathVariable profileId: UUID): ResponseEntity<List<PostResponse>> {
        val posts = postService.getPostsByProfileId(profileId)
        return ResponseEntity(posts, HttpStatus.OK)
    }
}