package com.jobnest.reviewsms.dto;

import com.jobnest.reviewsms.entities.Review;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {
    public ReviewDto mapToDto(Review review){

        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setId(review.getId());
        reviewDto.setDescription(review.getDescription());
        reviewDto.setRating(review.getRating());
        reviewDto.setPostedAt(review.getPostedAt());

        return reviewDto;
    }
}