package com.example.mylibrary.mylibary.controller;

import com.example.mylibrary.mylibary.dto.CommentCreateDto;
import com.example.mylibrary.mylibary.dto.CommentDto;
import com.example.mylibrary.mylibary.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<?> createComment(@RequestBody CommentCreateDto commentCreateDto) {
        commentService.createComment(commentCreateDto);
        return new ResponseEntity<>("Comment created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable("postId") Long postId) {
        List<CommentDto> commentDtos = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(commentDtos);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId,
                                                @RequestHeader("username") String username) {
        boolean deleted = commentService.deleteComment(commentId, username);
        if (deleted) {
            return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete comment", HttpStatus.BAD_REQUEST);
        }
    }
}