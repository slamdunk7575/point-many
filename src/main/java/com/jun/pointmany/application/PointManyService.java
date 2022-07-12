package com.jun.pointmany.application;

import com.jun.pointmany.dao.PlaceRepository;
import com.jun.pointmany.dao.ReviewRepository;
import com.jun.pointmany.dao.UserRepository;
import com.jun.pointmany.domain.Place;
import com.jun.pointmany.domain.Point;
import com.jun.pointmany.domain.Review;
import com.jun.pointmany.domain.User;
import com.jun.pointmany.dto.ReviewPointRequest;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PointManyService {
    private static final String POINT_LOCK = "POINT_LOCK";
    private static final Long LOCK_WAIT_TIME = 500L;
    private static final Long LOCK_RELEASE_TIME = 1000L;

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final RedissonClient redissonClient;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void calculateReviewPoint(ReviewPointRequest reviewPointRequest) {

        RLock lock = redissonClient.getLock(POINT_LOCK);

        try {
            boolean hasLock = lock.tryLock(LOCK_WAIT_TIME, LOCK_RELEASE_TIME, TimeUnit.MILLISECONDS);

            if (hasLock) {
                Review existingReview = reviewRepository.findByReviewId(reviewPointRequest.getReviewId());
                User user = userRepository.findByUserId(reviewPointRequest.getUserId());
                Point point = user.getPoint();

                switch (reviewPointRequest.getType()) {
                    case "ADD":
                        addReviewPoint(reviewPointRequest, point);
                        break;

                    case "MOD":
                        modReviewPoint(reviewPointRequest, existingReview, point);
                        break;

                    case "DELETE":
                        deleteReviewPoint(reviewPointRequest, existingReview, point);
                        break;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("LOCK 을 수행하는 동안 오류가 발생하였습니다.", e);
        } finally {
            lock.unlock();
        }
    }

    private void addReviewPoint(ReviewPointRequest reviewPointRequest, Point point) {
        //==> 내용 점수
        Place place = placeRepository.findByPlaceId(reviewPointRequest.getPlaceId());

        // 1자 이상 테스트 작성 (+1점)
        if (reviewPointRequest.hasContents()) {
            point.addContentsScore();
        }

        // 1장 이상 사진 첨부 (+1점)
        if (reviewPointRequest.hasImages()) {
            point.addContentsScore();
        }

        //==> 보너스 점수
        // 장소에 첫리뷰이면 보너스 점수 추가
        if (CollectionUtils.isEmpty(place.getReviews())) {
            point.addBoundScore();
        }
    }

    private void modReviewPoint(ReviewPointRequest reviewPointRequest, Review existingReview, Point point) {
        // 기존에 리뷰글만 있었고(이미지 X) 수정 이벤트로 이미지를 추가하는 경우
        if (existingReview.hasContents() && !existingReview.hasImage() &&
                reviewPointRequest.hasImages()) {
            point.addContentsScore();
        }

        // 기존에 리뷰 이미지만 있었고(내용 X) 수정 이벤트로 내용을 추가하는 경우
        if (existingReview.hasImage() && !existingReview.hasContents() &&
                reviewPointRequest.hasContents()
        ) {
            point.addContentsScore();
        }

    }

    private void deleteReviewPoint(ReviewPointRequest reviewPointRequest, Review existingReview, Point point) {
        // 기존 리뷰에 내용이 있었고 삭제 이벤트로 내용을 삭제하는 경우
        if (existingReview.hasContents() && !reviewPointRequest.hasContents()) {
            point.removeContentsScore();
        }

        // 기존 리뷰에 이미지가 있었고 삭제 이벤트로 이미지를 삭제하는 경우
        if (existingReview.hasImage() && !reviewPointRequest.hasImages()) {
            point.removeContentsScore();
        }
    }

    @Transactional(readOnly = true)
    public Integer getTotalPoint(String userId) {
        User user = userRepository.findByUserId(userId);
        Integer totalPoint = user.getTotalPoint();
        return totalPoint;
    }
}
