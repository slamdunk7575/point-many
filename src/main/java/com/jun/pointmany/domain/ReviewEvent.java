package com.jun.pointmany.domain;

import java.util.Arrays;

public enum ReviewEvent {

    ADD("포인트 추가"),
    MOD("포인트 수정"),
    DELETE("포인트 삭제")
    ;

    private final String message;

    ReviewEvent(String message) {
        this.message = message;
    }

    public static ReviewEvent value(String requestMessage) {
        return Arrays.stream(ReviewEvent.values())
                .filter(reviewEvent -> reviewEvent.message.equals(requestMessage))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(requestMessage + " 이벤트 메시지가 존재하지 않습니다."));
    }

}
