package com.example.mylibrary.mylibary.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PostDetailDto {
    private String title;
    private String content;
    private String location;
    private int cost;
    private int deposit;
    private String authorNickname;
    private Date createdAt;
    private int views;
    private Long postId;
    private String username;
    private String imageUrl;
}