package com.JobNest.companyms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Review {
    private Long id;
    private String description;
    private double rating;
    private Long companyId;
    private String postedAt;

}