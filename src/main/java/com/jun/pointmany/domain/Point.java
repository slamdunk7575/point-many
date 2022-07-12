package com.jun.pointmany.domain;

import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Point {
    private static final int ONE_POINT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "point_id")
    private String pointId = UUID.randomUUID().toString();

    @Comment("내용점수")
    @Column(name = "contents_score")
    private Integer contentsScore;

    @Comment("보너스점수")
    @Column(name = "bonus_score")
    private Integer bonusScore;

    public void addContentsScore() {
        this.contentsScore += ONE_POINT;
    }

    public void addBoundScore() {
        this.bonusScore += ONE_POINT;
    }

    public void removeContentsScore() {
        this.contentsScore -= ONE_POINT;
    }

    public void removeBoundScore() {
        this.bonusScore -= ONE_POINT;
    }

    public Integer calculateTotalPoint() {
        return this.contentsScore + this.bonusScore;
    }
}
