package com.jobnest.reviewsms.controller;

import com.jobnest.reviewsms.dto.ReviewDto;
import com.jobnest.reviewsms.entities.Review;
import com.jobnest.reviewsms.helper.ApiResponse;
import com.jobnest.reviewsms.helper.ResponseBuilder;
import com.jobnest.reviewsms.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;
    private final ResponseBuilder responseBuilder;

    private boolean success;
    private String message;
    private HttpStatus httpStatus;

    // Controller
    public ReviewController(ReviewService reviewService, ResponseBuilder responseBuilder) {
        this.reviewService = reviewService;
        this.responseBuilder = responseBuilder;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addReview(@RequestBody Review review, @RequestParam Long companyId) {
        log.info("Received POST request to add review for Company ID: {}", companyId);
        ReviewDto reviewDto = reviewService.addReview(review, companyId);

        success = reviewDto != null;
        message = "Review saved successfully";
        httpStatus = HttpStatus.CREATED;

        ApiResponse<String> response = responseBuilder.buildResponseWithoutData(success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/company")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getReviewsByCompId(@RequestParam Long companyId) {
        log.info("Received GET request to fetch reviews for company ID: {}", companyId);
        List<ReviewDto> reviewDtos = reviewService.getReviewsByCompId(companyId);

        success = !reviewDtos.isEmpty();
        message = success ? "Reviews fetched successfully" : "No reviews found for company ID: " + companyId;
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<List<ReviewDto>> response = responseBuilder.buildResponseWithData(reviewDtos, success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/avgRating")
    public double getAvgCompanyRating(@RequestParam Long companyId) {
        log.info("Received GET request to fetch avg rating for company id: {}", companyId);
        List<ReviewDto> reviewDtos = reviewService.getReviewsByCompId(companyId);
        return reviewDtos.stream().mapToDouble(ReviewDto::getRating).average().orElse(0.0);
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<ApiResponse<String>> deleteReviewsByCompId(@PathVariable Long companyId) {
        log.info("Received request to delete review with id: {}", companyId);

        success = reviewService.deleteReviewByCompId(companyId);
        message = success ? "Reviews deleted successfully." : "company ID: " + companyId + " not found.";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<String> response = responseBuilder.buildResponseWithoutData(success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }
}