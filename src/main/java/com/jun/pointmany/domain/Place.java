package com.jun.pointmany.domain;

import lombok.Getter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_id")
    private String placeId = UUID.randomUUID().toString();

    @Comment("장소명")
    @Column(nullable = false)
    private String name;

    @Comment("위치")
    private String location;

    @OneToMany(mappedBy = "place")
    private List<Review> reviews;
}
