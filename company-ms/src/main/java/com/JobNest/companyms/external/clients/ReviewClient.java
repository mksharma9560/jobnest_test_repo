package com.JobNest.companyms.external.clients;

import com.jobnest.reviewsms.helper.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "REVIEWS-SERVICE")
public interface ReviewClient {

    @GetMapping("/api/reviews/avgRating")
    Double getAvgCompanyRating(@RequestParam Long companyId);

    @DeleteMapping("/{companyId}")
    public ResponseEntity<ApiResponse<String>> deleteReviewsByCompId(@PathVariable Long companyId);
}