package com.example.mylibrary.mylibary.repository;

import com.example.mylibrary.domain.Post;
import com.example.mylibrary.domain.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(SiteUser author);
    List<Post> findByTitleContainingIgnoreCase(String title);
}