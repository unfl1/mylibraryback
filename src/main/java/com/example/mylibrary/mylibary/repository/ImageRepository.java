package com.example.mylibrary.mylibary.repository;

import com.example.mylibrary.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}