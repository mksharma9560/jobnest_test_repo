package com.JobNest.companyms.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class JobDto {
    private Long id;

    private String title;

    private String description;

    private String location;

    private String skills;

    private Date postedAt;
}