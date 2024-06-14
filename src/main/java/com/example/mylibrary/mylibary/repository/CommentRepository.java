package com.example.mylibrary.mylibary.repository;

import com.example.mylibrary.mylibary.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    List<Comment> findByParentCommentId(Long parentCommentId);
}