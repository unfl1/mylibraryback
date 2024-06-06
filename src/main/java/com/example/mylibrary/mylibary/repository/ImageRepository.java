package com.example.mylibrary.mylibary.repository;

import com.example.mylibrary.mylibary.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}