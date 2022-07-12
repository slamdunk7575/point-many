package com.jun.pointmany.ui;

import com.jun.pointmany.application.PointManyService;
import com.jun.pointmany.dto.ReviewPointRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class PointManyController {
    private final PointManyService pointManyService;

    @PostMapping("/point")
    public ResponseEntity calculatePoint(@RequestBody ReviewPointRequest reviewPointRequest) {
        pointManyService.calculateReviewPoint(reviewPointRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/point")
    public ResponseEntity getTotalPoint(@RequestParam(value = "user") String userId) {
        Integer totalPoint = pointManyService.getTotalPoint(userId);
        return ResponseEntity.ok().body(totalPoint);
    }
}
