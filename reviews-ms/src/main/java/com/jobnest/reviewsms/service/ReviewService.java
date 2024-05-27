package com.jobnest.reviewsms.service;

import com.jobnest.reviewsms.dto.ReviewDto;
import com.jobnest.reviewsms.entities.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {

    ReviewDto addReview(Review review, Long companyId);

    List<ReviewDto> getReviewsByCompId(Long companyId);

    boolean deleteReviewByCompId(Long companyId);
}