package com.example.mylibrary.mylibary.controller;

import com.example.mylibrary.domain.Post;
import com.example.mylibrary.dto.MyPostListDto;
import com.example.mylibrary.dto.PostCreateDto;
import com.example.mylibrary.dto.PostDetailDto;
import com.example.mylibrary.dto.PostListDto;
import com.example.mylibrary.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<Post> createPost(@ModelAttribute PostCreateDto postCreateDto, @RequestParam("image") MultipartFile image) {
        postCreateDto.setImage(image);
        Post post = postService.createPost(postCreateDto);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/post/posts")
    public ResponseEntity<List<PostListDto>> getAllPosts() {
        List<PostListDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDetailDto> getPostById(@PathVariable("postId") Long postId) {
        PostDetailDto postDetailDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDetailDto);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId, @RequestParam("username") String username) {
        postService.deletePost(postId, username);
        return ResponseEntity.ok("Post deleted successfully");
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable("filename") String filename) {
        try {
            Path imagePath = Paths.get("uploads/", filename);  // `uploadPath`를 사용하도록 수정
            byte[] imageBytes = Files.readAllBytes(imagePath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // 파일 타입에 맞게 설정
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/post/search")
    public ResponseEntity<List<PostListDto>> searchPostsByTitle(@RequestParam("title") String title) {
        List<PostListDto> searchResults = postService.searchPostsByTitle(title);
        if (searchResults.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }

    @GetMapping("/post/mypost/{username}")
    public ResponseEntity<List<MyPostListDto>> getMyPosts(@PathVariable("username") String username) {
        List<MyPostListDto> myPosts = postService.getMyPosts(username);
        return ResponseEntity.ok(myPosts);
    }
}