package com.social.post_service.repository

import com.social.post_service.model.Post
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PostRepository : MongoRepository<Post, ObjectId> {
    fun findByProfileId(profileId: UUID): List<Post>
}