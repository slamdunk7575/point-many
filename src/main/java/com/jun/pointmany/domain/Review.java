package com.jun.pointmany.domain;

import org.hibernate.annotations.Comment;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_id")
    private String reviewId = UUID.randomUUID().toString();

    @Comment("내용")
    @Lob
    private String contents;

    @Comment("이미지 주소")
    private String image;

    @ManyToOne
    @JoinColumn(name = "name_id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public boolean hasContents() {
        return StringUtils.hasText(this.contents);
    }

    public boolean hasImage() {
        return StringUtils.hasText(this.image);
    }

}
