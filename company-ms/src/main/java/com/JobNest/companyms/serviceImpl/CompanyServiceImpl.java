package com.JobNest.companyms.serviceImpl;

import com.JobNest.companyms.dto.CompanyDto;
import com.JobNest.companyms.dto.JobDto;
import com.JobNest.companyms.entities.Company;
import com.JobNest.companyms.exceptions.ApplicationException;
import com.JobNest.companyms.exceptions.ExternalServiceException;
import com.JobNest.companyms.exceptions.ResourceNotFoundException;
import com.JobNest.companyms.external.clients.JobClient;
import com.JobNest.companyms.helper.ApiResponse;
import com.JobNest.companyms.helper.DtoMapper;
import com.JobNest.companyms.kafka.messaging.CompanyMsgProducer;
import com.JobNest.companyms.repository.CompanyRepository;
import com.JobNest.companyms.service.CompanyService;
import feign.FeignException;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);
    // ThreadLocal to maintain attempt count for each method call independently
    private static final ThreadLocal<Integer> attemptCounter = ThreadLocal.withInitial(() -> 0);
    private final CompanyRepository companyRepo;
    private final JobClient jobClient;
    private final DtoMapper dtoMapper;
    private final CompanyMsgProducer msgProducer;

    // Constructor Injection
    public CompanyServiceImpl(
            CompanyRepository companyRepo,
            JobClient jobClient,
            DtoMapper dtoMapper,
            CompanyMsgProducer msgProducer
    ) {
        this.companyRepo = companyRepo;
        this.jobClient = jobClient;
        this.dtoMapper = dtoMapper;
        this.msgProducer = msgProducer;
    }

    @Transactional
    @Override
    public CompanyDto createCompany(Company company) {
        log.info("Executing createCompany()");
        Company savedCompany = companyRepo.save(company);
        log.info("Company saved with Id: {}", savedCompany.getId());
        return dtoMapper.mapToCompanyDto(savedCompany);
    }

    @Override
    @Transactional
    public boolean updateCompany(Long id, Company newCompany) {
        log.info("Executing updateCompany()");
        Optional<Company> optCompany = companyRepo.findById(id);
        if (optCompany.isEmpty()) {
            log.warn("Company Id {} not found.", id);
            return false;
        }
        Company company = optCompany.get();
        Company copiedCompany = setNonNullPropertyNames(company, newCompany);

        companyRepo.save(copiedCompany);
        return true;
    }

    @Override
    // @CircuitBreaker(name = "external-client-breaker", fallbackMethod = "fallbackGetCompanyById")
    @Retry(name = "external-client-breaker", fallbackMethod = "fallbackGetCompanyById")
    public CompanyDto getCompanyById(Long id) {
        attemptCounter.set(attemptCounter.get() + 1);
        int attempt = attemptCounter.get();

        String reviewUrl = "/api/reviews/company?companyId=" + id;
        CompanyDto companyDto = null;
        Optional<Company> company = companyRepo.findById(id);

        if (company.isEmpty()) {
            log.info("Company with ID: {} not found", id);
            attemptCounter.remove();
            return null;
        }
        log.info("Feign call to Job-Service, attempt: {}", attempt);

        ResponseEntity<ApiResponse<List<JobDto>>> responseEntity = jobClient.getJobsByCompId(id);
        List<JobDto> jobDtos = responseEntity.getBody().getData();
        if (jobDtos == null || jobDtos.isEmpty()) {
            log.warn("No jobs found for company ID: {}, attempt: {}", id, attempt);
            attemptCounter.remove();
            return dtoMapper.mapToDtoWithJobsAndReviewUrl(company.get(), List.of(), reviewUrl);
        }

        companyDto = dtoMapper.mapToDtoWithJobsAndReviewUrl(company.get(), jobDtos, reviewUrl);
        log.info("Company with ID: {} fetched successfully.", id);

        attemptCounter.remove();
        return companyDto;
    }

    // FALLBACK Method
    public CompanyDto fallbackGetCompanyById(Long id, Throwable thr) {
        log.error("Executing fallback method");
        if (thr instanceof ResourceNotFoundException) {
            throw (ResourceNotFoundException) thr;
        } else if (thr instanceof FeignException ex) {
            throw new ExternalServiceException(ex.getMessage());
        } else if (thr instanceof DataAccessException) {
            throw (DataAccessException) thr;
        } else {
            throw new ApplicationException(thr.getMessage());
        }
    }

    @Override
    public List<CompanyDto> getCompanies() {
        log.info("Executing getCompanies()");
        List<Company> companies = companyRepo.findAll();
        List<CompanyDto> companyDtos = new ArrayList<>();

        if (companies.isEmpty()) {
            return companyDtos;
        }

        List<CompanyDto> companyDtos1 = retrieveCompanyDtos(companies);
        List<CompanyDto> sortedCompDtos = sortCompanies(companyDtos1);
        log.info("Returning {} companies.", sortedCompDtos.size());

        return sortedCompDtos;
    }

    @Override
    @Transactional
    @Retry(name = "external-client-breaker", fallbackMethod = "fallbackDeleteCompanyById")
    public boolean deleteCompanyById(Long companyId) {
        log.info("Executing deleteCompanyById() for Job ID: {}", companyId);
        attemptCounter.set(attemptCounter.get() + 1);
        int attempt = attemptCounter.get();

        if (!companyRepo.existsById(companyId)) {
            log.warn("Company ID: {} does not exist.", companyId);
            return false;
        }

        // first delete job
        log.info("Feign call to Job-Service, attempt: {}", attempt);
        jobClient.deleteJobByCompId(companyId);

        // then delete reviews
        log.info("Company delete event");
        msgProducer.publishMessage(companyId);

        // if job and reviews are deleted
        companyRepo.deleteById(companyId);
        return true;
    }

    // FALLBACK Method
    public boolean fallbackDeleteCompanyById(Long compid, Throwable thr) {
        log.error("Executing fallback method");
        if (thr instanceof ResourceNotFoundException) {
            throw (ResourceNotFoundException) thr;
        } else if (thr instanceof FeignException ex) {
            throw new ExternalServiceException(ex.getMessage());
        } else if (thr instanceof DataAccessException) {
            throw (DataAccessException) thr;
        } else {
            throw new ApplicationException(thr.getMessage());
        }
    }

    @Override
    public List<CompanyDto> searchCompany(Map<String, String> searchCriteria) {
        log.info("*** Executing searchCompany() ***");
        String name = searchCriteria.get("name").toLowerCase();
        String industry = searchCriteria.get("industry").toLowerCase();
        String location = searchCriteria.get("location").toLowerCase();

        log.info("Executing custom query with search criteria");
        List<Company> companies = companyRepo.fetchCompany(name, industry, location);
        return retrieveCompanyDtos(companies);
    }

    @Override
    public List<CompanyDto> getCompanyByKeyword(String keyword) {
        log.info("Executing custom query with search keyword");
        String keywordLC = keyword.toLowerCase();
        List<Company> companies = companyRepo.fetchCompaniesByKeyword(keywordLC);
        return retrieveCompanyDtos(companies);
    }

    public List<CompanyDto> retrieveCompanyDtos(List<Company> companies) {
        List<CompanyDto> companyDtos = new ArrayList<>();

        if (companies == null || companies.isEmpty()) {
            log.warn("Companies data not found, returning empty list");
            return companyDtos;
        }

        for (Company company : companies) {
            Long compId = company.getId();
            String reviewsUrl = "/api/reviews/company?companyId=" + compId;
            String jobsUrl = "/api/jobs/company/" + compId;
            CompanyDto companyDto = dtoMapper.mapToDtoWithJobAndReviewUrl(company, jobsUrl, reviewsUrl);
            companyDtos.add(companyDto);
        }
        log.info("Returning {} companies", companyDtos.size());
        return companyDtos;
    }

    public List<CompanyDto> sortCompanies(List<CompanyDto> companyDtos) {
        companyDtos.sort(Comparator.comparingDouble(CompanyDto::getAverageRating).reversed());
        return companyDtos;
    }

    // Using Reflection to run update query only for required fields not for all
    public Company setNonNullPropertyNames(Company company, Company newJob) {
        try {
            for (Field field : newJob.getClass().getDeclaredFields()) {
                field.setAccessible(true);  // Make the field accessible
                Object value = field.get(newJob);  // Get the value of the field in the newJob object
                if (value != null) {
                    field.set(company, value);  // Set the value of the field in the job
                }
            }
        } catch (IllegalAccessException ex) {
            throw new ApplicationException(ex.getMessage());
        }
        return company;
    }
}