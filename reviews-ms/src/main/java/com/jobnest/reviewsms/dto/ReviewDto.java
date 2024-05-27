package com.jobnest.reviewsms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewDto {

    private Long id;

    private String description;

    private double rating;

    private String postedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long companyId;

}