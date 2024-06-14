package com.example.mylibrary.mylibary.service;

import com.example.mylibrary.mylibary.domain.Image;
import com.example.mylibrary.mylibary.domain.Post;
import com.example.mylibrary.mylibary.domain.SiteUser;
import com.example.mylibrary.mylibary.dto.MyPostListDto;
import com.example.mylibrary.mylibary.dto.PostCreateDto;
import com.example.mylibrary.mylibary.dto.PostDetailDto;
import com.example.mylibrary.mylibary.dto.PostListDto;
import com.example.mylibrary.mylibary.repository.ImageRepository;
import com.example.mylibrary.mylibary.repository.PostRepository;
import com.example.mylibrary.mylibary.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    //post 생성
    @Transactional
    public Post createPost(PostCreateDto postCreateDto) {
        // 요청에서 사용자 이름을 받아서 해당 사용자를 찾습니다.
        SiteUser author = userRepository.findByUsername(postCreateDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = new Post();
        post.setTitle(postCreateDto.getTitle());
        post.setContent(postCreateDto.getContent());
        post.setLocation(postCreateDto.getLocation());
        post.setCost(postCreateDto.getCost());
        post.setDeposit(postCreateDto.getDeposit());
        post.setAuthor(author);
        post.setCreatedAt(new Date());
        post.setViews(0);

        // 이미지 저장 및 Image 엔티티 생성
        MultipartFile imageFile = postCreateDto.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = saveImage(imageFile);
                Image image = new Image();
                image.setUrl(fileName);
                image.setPost(post);
                imageRepository.save(image);
                post.setImage(image);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }

        return postRepository.save(post);
    }

    // 이미지 저장 메소드
    private String saveImage(MultipartFile imageFile) throws IOException {
        String fileName = imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadPath);

        // 디렉토리가 존재하지 않으면 생성합니다.
        if (!Files.exists(imagePath)) {
            Files.createDirectories(imagePath);
        }

        Path filePath = imagePath.resolve(fileName);
        Files.write(filePath, imageFile.getBytes());

        // 이미지 URL 구성
        return "/uploads/" + fileName;  // URL 경로를 "/uploads/"로 수정
    }

    // 모든 post 조회
    public List<PostListDto> getAllPosts() {
        return postRepository.findAll().stream().map(post -> {
            PostListDto postResponseDto = new PostListDto();
            postResponseDto.setPostId(post.getId()); // postId 설정
            postResponseDto.setTitle(post.getTitle());
            postResponseDto.setLocation(post.getLocation());
            postResponseDto.setCost(post.getCost());
            postResponseDto.setAuthorNickname(post.getAuthor().getNickname());

            // 이미지 URL 설정
            if (post.getImage() != null) {
                postResponseDto.setImageUrl(post.getImage().getUrl());
            }

            return postResponseDto;
        }).collect(Collectors.toList());
    }

    //상세보기
    public PostDetailDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        PostDetailDto postDetailDto = new PostDetailDto();
        postDetailDto.setPostId(post.getId());
        postDetailDto.setTitle(post.getTitle());
        postDetailDto.setContent(post.getContent());
        postDetailDto.setLocation(post.getLocation());
        postDetailDto.setCost(post.getCost());
        postDetailDto.setDeposit(post.getDeposit());
        postDetailDto.setAuthorNickname(post.getAuthor().getNickname());
        postDetailDto.setUsername(post.getAuthor().getUsername());
        postDetailDto.setCreatedAt(post.getCreatedAt());
        postDetailDto.setViews(post.getViews());

        // 이미지 URL 설정
        if (post.getImage() != null) {
            postDetailDto.setImageUrl(post.getImage().getUrl());
        }

        post.setViews(post.getViews() + 1);
        postRepository.save(post);

        return postDetailDto;
    }

    // 게시물 삭제
    public void deletePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 게시물 작성자와 요청한 사용자를 비교하여 인증합니다.
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("You are not authorized to delete this post");
        }

        postRepository.delete(post);
    }

    // 제목으로 포스트 검색
    public List<PostListDto> searchPostsByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title).stream().map(post -> {
            PostListDto postResponseDto = new PostListDto();
            postResponseDto.setPostId(post.getId()); // postId 설정
            postResponseDto.setTitle(post.getTitle());
            postResponseDto.setLocation(post.getLocation());
            postResponseDto.setCost(post.getCost());
            postResponseDto.setAuthorNickname(post.getAuthor().getNickname());

            // 이미지 URL 설정
            if (post.getImage() != null) {
                postResponseDto.setImageUrl(post.getImage().getUrl());
            }

            return postResponseDto;
        }).collect(Collectors.toList());
    }

    // 내가 작성한 모든 post 조회
    public List<MyPostListDto> getMyPosts(String username) {
        SiteUser author = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return postRepository.findByAuthor(author).stream().map(post -> {
            MyPostListDto myPostListDto = new MyPostListDto();
            myPostListDto.setPostId(post.getId());
            myPostListDto.setTitle(post.getTitle());
            myPostListDto.setLocation(post.getLocation());
            myPostListDto.setCost(post.getCost());
            myPostListDto.setAuthorUsername(post.getAuthor().getUsername());

            if (post.getImage() != null) {
                myPostListDto.setImageUrl(post.getImage().getUrl());
            }

            return myPostListDto;
        }).collect(Collectors.toList());
    }
}