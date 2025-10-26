package com.social.comment_service.service;

import com.social.comment_service.dto.request.CommentCreateRequest;
import com.social.comment_service.dto.response.CommentResponseDto;
import com.social.comment_service.entity.Comment;
import com.social.comment_service.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {
    private final CommentRepository repository;

    public CommentService(CommentRepository commentRepository) {
        this.repository = commentRepository;
    }

    @Transactional
    public CommentResponseDto createComment(CommentCreateRequest request) {
        Comment comment = Comment.builder()
                .postId(request.postId())
                .profileId(request.profileId())
                .parentCommentId(request.parentCommentId())
                .content(request.content())
                .build();
        repository.save(comment);
        return new CommentResponseDto(
                comment.getId(),
                comment.getPostId(),
                comment.getProfileId(),
                comment.getParentCommentId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                null
        );
    }

    @Transactional
    public List<CommentResponseDto> getAllCommentsIncludeReplies(UUID postId) {
        List<Comment> comments = repository.findByPostIdOrderByCreatedAtDesc(postId);
        // Map comments to CommentResponseDto with nested replies
        Map<UUID, CommentResponseDto> commentsResponse = new HashMap<>();
        for (Comment comment : comments) {
            if (comment.getParentCommentId() == null) {
                commentsResponse.put(comment.getId(), toDto(comment));
            } else {
                CommentResponseDto parentComment = commentsResponse.get(comment.getParentCommentId());
                parentComment.replyComments().add(toDto(comment));
            }
        }

        return commentsResponse.values().stream().toList();
    }

    private CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getPostId(),
                comment.getProfileId(),
                comment.getParentCommentId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                List.of()
        );
    }
}
