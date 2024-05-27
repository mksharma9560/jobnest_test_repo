package com.jobnest.searchms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
public class CompanyDto {

    private Long id;

    private String name;

    private String description;

    private String industry;

    private String location;

    private Double averageRating;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<JobDto> jobs;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String jobsUrl;

    private String reviewUrl;

}