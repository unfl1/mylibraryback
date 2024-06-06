package com.example.mylibrary.mylibary.dto;

import lombok.Data;

@Data
public class MyPostListDto {
    private Long postId;
    private String title;
    private String location;
    private int cost;
    private String authorUsername; // 작성자의 username 추가
    private String imageUrl;
}