package com.jun.pointmany.domain;

import lombok.Getter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId = UUID.randomUUID().toString();

    @Comment("이메일")
    private String email;

    @OneToOne
    @JoinColumn(name = "point_id")
    private Point point;

    public Integer getTotalPoint() {
        return point.calculateTotalPoint();
    }
}
