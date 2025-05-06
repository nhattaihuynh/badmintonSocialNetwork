package com.social.post_service.mapper

import com.social.post_service.dto.PostRequest
import com.social.post_service.dto.PostResponse
import com.social.post_service.model.Post
import org.bson.types.ObjectId
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface PostMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "likes", constant = "0")
    @Mapping(target = "likedBy", expression = "java(java.util.Collections.emptyList())")
    @Mapping(target = "commentCount", constant = "0")
    @Mapping(target = "shareCount", constant = "0")
    fun toEntity(postRequest: PostRequest): Post
    
    @Mapping(target = "id", source = "id", qualifiedByName = ["objectIdToString"])
    fun toResponse(post: Post): PostResponse
    
    @Named("objectIdToString")
    fun objectIdToString(objectId: ObjectId?): String {
        return objectId?.toString() ?: ""
    }
}
