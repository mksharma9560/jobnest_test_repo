package com.JobNest.companyms.helper;

import com.JobNest.companyms.dto.CompanyDto;
import com.JobNest.companyms.dto.JobDto;
import com.JobNest.companyms.entities.Company;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DtoMapper {
    public CompanyDto mapToDtoWithJobsAndReviewUrl(Company company, List<JobDto> jobDtos, String reviewUrl) {

        CompanyDto companyDto = new CompanyDto();

        companyDto.setId(company.getId());
        companyDto.setName(company.getName());
        companyDto.setDescription(company.getDescription());
        companyDto.setIndustry(company.getIndustry());
        companyDto.setLocation(company.getLocation());
        companyDto.setAverageRating(company.getAverageRating());

        companyDto.setJobs(jobDtos);
        companyDto.setReviewUrl(reviewUrl);
        return companyDto;
    }

    public CompanyDto mapToDtoWithJobAndReviewUrl(Company company, String jobsUrl, String reviewUrl) {

        CompanyDto companyDto = new CompanyDto();

        companyDto.setId(company.getId());
        companyDto.setName(company.getName());
        companyDto.setDescription(company.getDescription());
        companyDto.setIndustry(company.getIndustry());
        companyDto.setLocation(company.getLocation());
        companyDto.setAverageRating(company.getAverageRating());

        companyDto.setJobsUrl(jobsUrl);
        companyDto.setReviewUrl(reviewUrl);
        return companyDto;
    }

    public CompanyDto mapToCompanyDto(Company company) {

        CompanyDto companyDto = new CompanyDto();

        companyDto.setId(company.getId());
        companyDto.setName(company.getName());
        companyDto.setDescription(company.getDescription());
        companyDto.setIndustry(company.getIndustry());
        companyDto.setLocation(company.getLocation());
        companyDto.setAverageRating(company.getAverageRating());
        return companyDto;
    }
}