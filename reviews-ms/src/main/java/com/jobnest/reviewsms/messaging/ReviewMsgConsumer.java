package com.jobnest.reviewsms.messaging;

import com.jobnest.reviewsms.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReviewMsgConsumer {

    private static final Logger log = LoggerFactory.getLogger(ReviewMsgConsumer.class);
    private final ReviewService reviewService;

    // Constructor
    public ReviewMsgConsumer(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @KafkaListener(topics = "company-deleted-topic", groupId = "reviews-service")
    public void consumeMessage(Long companyId) {
        log.info("*** Message received ***");
        reviewService.deleteReviewByCompId(companyId);
        log.info("*** Message consumed ***");
    }
}