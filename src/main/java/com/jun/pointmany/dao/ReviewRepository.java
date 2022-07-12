package com.jun.pointmany.dao;

import com.jun.pointmany.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByReviewId(String reviewId);
}
