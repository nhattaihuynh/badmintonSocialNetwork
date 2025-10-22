package com.social.comment_service.dto.request;

import java.util.UUID;

public record CommentCreateRequest(
        UUID postId,
        UUID profileId,
        UUID parentCommentId,
        String content
) {
}
