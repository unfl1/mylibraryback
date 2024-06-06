package com.example.mylibrary.mylibary.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostCreateDto {
    private String title;
    private String content;
    private String location;
    private int cost;
    private int deposit;
    private String username; // 사용자 이름을 추가합니다.
    private MultipartFile image;
}