package com.example.shared_auth.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blog_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    @JsonBackReference(value = "blog-images")
    private Blog blog;
}
