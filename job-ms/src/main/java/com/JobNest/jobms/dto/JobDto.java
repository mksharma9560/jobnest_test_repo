package com.JobNest.jobms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class JobDto {

    private Long id;

    private String title;

    private String description;

    private String location;

    private String skills;

    private String postedAt;

    private String companyUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String companyName;
}