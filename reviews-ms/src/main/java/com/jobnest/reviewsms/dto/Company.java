package com.jobnest.reviewsms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Company {

    private Long id;

    private String name;

    private String description;

    private String industry;

    private String location;

    private Double averageRating;
}