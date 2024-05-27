package com.jobnest.searchms.servicesImpl;

import com.jobnest.searchms.clients.CompanyClient;
import com.jobnest.searchms.clients.JobClient;
import com.jobnest.searchms.dto.CompanyDto;
import com.jobnest.searchms.dto.JobDto;
import com.jobnest.searchms.exceptions.ApplicationException;
import com.jobnest.searchms.exceptions.ExternalServiceException;
import com.jobnest.searchms.helper.ApiResponse;
import com.jobnest.searchms.services.SearchService;
import feign.FeignException;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final JobClient jobClient;
    private final CompanyClient companyClient;

    // Constructor Injection
    public SearchServiceImpl(JobClient jobClient, CompanyClient companyClient) {
        this.jobClient = jobClient;
        this.companyClient = companyClient;
    }

    @Override
    @Retry(name = "external-client-breaker", fallbackMethod = "fallbackGetJobs")
    public List<JobDto> getJobs(Map<String, String> searchCriteria) {
        log.info("Feign call to JOB-SERVICE to fetch jobs based on criteria");

        ResponseEntity<ApiResponse<List<JobDto>>> responseEntity = jobClient.searchJobs(searchCriteria);
        List<JobDto> jobDtos = new ArrayList<>();

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            jobDtos = responseEntity.getBody().getData();
        }
        log.info("Jobs fetched successfully");
        return jobDtos;
    }

    // FALLBACK - getJobs
    public List<JobDto> fallbackGetJobs(Map<String, String> searchCriteria, Throwable thr) {
        log.error("Executing fallback method");
        if (thr instanceof FeignException ex) {
            throw new ExternalServiceException(ex.getMessage());
        } else {
            log.error("An unexpected exception occurred", thr);
            throw new ApplicationException(thr.getMessage());
        }
    }

    @Override
    @Retry(name = "external-client-breaker", fallbackMethod = "fallbackGetCompanies")
    public List<CompanyDto> getCompanies(Map<String, String> searchCriteria) {
        log.info("Feign call to COMPANY-SERVICE to fetch companies based on criteria");

        ResponseEntity<ApiResponse<List<CompanyDto>>> responseEntity = companyClient.searchCompanies(searchCriteria);
        List<CompanyDto> companyDtos = new ArrayList<>();

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            companyDtos = responseEntity.getBody().getData();
        }
        return companyDtos;
    }

    // FALLBACK - getJobs
    public List<CompanyDto> fallbackGetCompanies(Map<String, String> searchCriteria, Throwable thr) {
        log.error("Executing fallback method");
        if (thr instanceof FeignException ex) {
            throw new ExternalServiceException(ex.getMessage());
        } else {
            throw new ApplicationException(thr.getMessage());
        }
    }

    @Override
    @Retry(name = "external-client-breaker", fallbackMethod = "fallbackGetJobsByKeyword")
    public List<JobDto> getJobsByKeyword(String keyword) {
        log.info("Feign call to JOB-SERVICE to fetch jobs based on keyword");
        String keywordLC = keyword.toLowerCase();

        ResponseEntity<ApiResponse<List<JobDto>>> responseEntity = jobClient.searchJobByKeyword(keywordLC);
        List<JobDto> jobDtos = new ArrayList<>();

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            jobDtos = responseEntity.getBody().getData();
        }
        return jobDtos;
    }

    // FALLBACK - getJobs
    public List<JobDto> fallbackGetJobsByKeyword(String keyword, Throwable thr) {
        log.error("Executing fallback method");
        if (thr instanceof FeignException ex) {
            throw new ExternalServiceException(ex.getMessage());
        } else {
            log.error("An unexpected exception occurred", thr);
            throw new ApplicationException(thr.getMessage());
        }
    }

    @Override
    @Retry(name = "external-client-breaker", fallbackMethod = "fallbackGetCompaniesByKeyword")
    public List<CompanyDto> getCompaniesByKeyword(String keyword) {
        log.info("Make feign call to COMPANY-SERVICE to fetch companies based on keyword");
        String keywordLC = keyword.toLowerCase();
        ResponseEntity<ApiResponse<List<CompanyDto>>> responseEntity = companyClient.searchJobsByKeyword(keywordLC);
        List<CompanyDto> companyDtos = new ArrayList<>();

        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            companyDtos = responseEntity.getBody().getData();
        }
        return companyDtos;
    }

    // FALLBACK - getJobs
    public List<JobDto> fallbackGetCompaniesByKeyword(String keyword, Throwable thr) {
        log.error("Executing fallback method");
        if (thr instanceof FeignException ex) {
            throw new ExternalServiceException(ex.getMessage());
        } else {
            throw new ApplicationException(thr.getMessage());
        }
    }
}