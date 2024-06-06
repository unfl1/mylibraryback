package com.example.mylibrary.mylibary.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String location;

    private int cost;

    private int deposit;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private int views;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private SiteUser author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private Image image;
}
