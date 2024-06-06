package com.example.mylibrary.mylibary.service;

import com.example.mylibrary.mylibary.domain.Comment;
import com.example.mylibrary.mylibary.domain.Post;
import com.example.mylibrary.mylibary.domain.SiteUser;
import com.example.mylibrary.mylibary.dto.CommentCreateDto;
import com.example.mylibrary.mylibary.dto.CommentDto;
import com.example.mylibrary.mylibary.repository.CommentRepository;
import com.example.mylibrary.mylibary.repository.PostRepository;
import com.example.mylibrary.mylibary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //댓글 생성
    public Comment createComment(CommentCreateDto commentCreateDto) {
        Post post = postRepository.findById(commentCreateDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        SiteUser user = userRepository.findByUsername(commentCreateDto.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment parentComment = null;
        if (commentCreateDto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(commentCreateDto.getParentCommentId()).orElse(null);
        }

        Comment comment = new Comment();
        comment.setContent(commentCreateDto.getContent());
        comment.setCreatedAt(new Date());
        comment.setPost(post);
        comment.setUser(user);
        comment.setParentComment(parentComment);

        return commentRepository.save(comment);
    }

    //댓글 목록, 대댓글 위해 계층 구조 만들기
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDto> commentDTOs = comments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return buildCommentHierarchy(commentDTOs);
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setPostId(comment.getPost().getId());
        commentDto.setUsername(comment.getUser().getUsername());
        commentDto.setParentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null);
        commentDto.setNickname(comment.getUser().getNickname());

        return commentDto;
    }

    private List<CommentDto> buildCommentHierarchy(List<CommentDto> comments) {
        List<CommentDto> rootComments = new ArrayList<>();
        comments.forEach(comment -> {
            if (comment.getParentCommentId() == null) {
                rootComments.add(comment);
            } else {
                CommentDto parentComment = comments.stream()
                        .filter(c -> c.getId().equals(comment.getParentCommentId()))
                        .findFirst()
                        .orElse(null);
                if (parentComment != null) {
                    if (parentComment.getReplies() == null) {
                        parentComment.setReplies(new ArrayList<>());
                    }
                    parentComment.getReplies().add(comment);
                }
            }
        });
        return rootComments;
    }

    //댓글 삭제
    public boolean deleteComment(Long commentId, String username) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if (comment.getUser().getUsername().equals(username)) {
                commentRepository.delete(comment);
                return true;
            }
        }
        return false;
    }
}