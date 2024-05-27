package com.jobnest.searchms.services;

import com.jobnest.searchms.dto.CompanyDto;
import com.jobnest.searchms.dto.JobDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SearchService {

    // Custom query method
    List<JobDto> getJobs(Map<String, String> searchCriteria);

    List<CompanyDto> getCompanies(Map<String, String> searchCriteria);

    List<JobDto> getJobsByKeyword(String keyword);

    List<CompanyDto> getCompaniesByKeyword(String keyword);
}