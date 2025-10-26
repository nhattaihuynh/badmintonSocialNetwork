package com.social.comment_service.controller;

import com.social.comment_service.dto.request.CommentCreateRequest;
import com.social.comment_service.dto.response.CommentResponseDto;
import com.social.comment_service.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentResponseDto createComment(@RequestBody CommentCreateRequest request) {
        return commentService.createComment(request);
    }

    @GetMapping("/{postId}/comments-with-replies")
    public List<CommentResponseDto> getAllCommentsIncludeReplies(@PathVariable("postId") UUID postId) {
        return commentService.getAllCommentsIncludeReplies(postId);
    }
}
