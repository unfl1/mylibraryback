package com.example.mylibrary.mylibary.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private Date createdAt;
    private Long postId;
    private String username;
    private Long parentCommentId;
    private String nickname;
    private List<CommentDto> replies;
}