package com.example.mylibrary.mylibary.dto;

import lombok.Data;

@Data
public class CommentCreateDto {
    private String content;  // 댓글 내용
    private Long postId;  // 댓글이 달릴 게시물 ID
    private String username;  // 댓글 작성자 사용자 이름
    private Long parentCommentId;  // 부모 댓글 ID (대댓글 기능을 위해, null일 수 있음)
}