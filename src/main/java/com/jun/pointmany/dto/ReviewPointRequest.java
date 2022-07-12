package com.jun.pointmany.dto;

import com.jun.pointmany.domain.ReviewEvent;
import lombok.Getter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Getter
public class ReviewPointRequest {

    private String type;
    private ReviewEvent reviewEvent;
    private String reviewId;
    private String contents;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;

    public boolean hasContents() {
        return StringUtils.hasText(contents);
    }

    public boolean hasImages() {
        return !CollectionUtils.isEmpty(attachedPhotoIds);
    }
}
