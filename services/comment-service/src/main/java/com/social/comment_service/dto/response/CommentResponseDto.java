package com.social.comment_service.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CommentResponseDto(
        UUID id,
        UUID postId,
        UUID profileId,
        UUID parentCommentId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CommentResponseDto> replyComments
) {
}
