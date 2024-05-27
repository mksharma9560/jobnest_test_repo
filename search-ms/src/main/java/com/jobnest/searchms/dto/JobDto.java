package com.jobnest.searchms.dto;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String companyName;

//    private Long companyId;
    private String companyUrl;
}