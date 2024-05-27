package com.JobNest.jobms.serviceImpl;

import com.JobNest.jobms.dto.DtoMapper;
import com.JobNest.jobms.dto.JobDto;
import com.JobNest.jobms.entities.Job;
import com.JobNest.jobms.exceptions.ApplicationException;
import com.JobNest.jobms.repository.JobRepository;
import com.JobNest.jobms.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);
    private final JobRepository jobRepo;
    private final DtoMapper dtoMapper;
    private final int attempt = 0;
    @Autowired
    private RestTemplate restTemplate;

    // Constructor Injection
    public JobServiceImpl(
            JobRepository jobRepo, DtoMapper dtoMapper) {
        this.jobRepo = jobRepo;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional
    public JobDto createJob(Job job) {
        log.info("Executing createJob()");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        job.setPostedAt(date);

        Job savedJob = jobRepo.save(job);
        String companyUrl = "/api/companies/" + savedJob.getCompanyId();

        log.info("Job saved with ID: {}", savedJob.getId());
        return dtoMapper.mapToDtoWithUrl(savedJob, companyUrl);
    }

    @Transactional
    @Override
    public boolean updateJobById(Long id, Job newJob) {
        log.info("Executing updateJobById()");
        Optional<Job> optionalOfJob = jobRepo.findById(id);
        if (optionalOfJob.isEmpty()) {
            log.warn("Job ID: {} not found.", id);
            return false;
        }
        Job job = optionalOfJob.get();
        Job copiedJob = setNonNullPropertyNames(job, newJob);

        jobRepo.save(copiedJob);
        return true;
    }

    @Override
    public List<JobDto> getJobs() {
        log.info("Executing getJobs()");
        List<Job> jobs = jobRepo.findAll();
        List<JobDto> jobDtos = new ArrayList<>();

        if (jobs.isEmpty()) {
            log.warn("No jobs found");
            return jobDtos;
        }

        log.info("Found {} jobs", jobs.size());
        return retrieveJobDtos(jobs);
    }

    @Override
    public List<JobDto> findJobsByCompId(Long companyId) {
        log.info("Executing findJobsByCompId()");
        List<Job> jobs = jobRepo.findByCompanyId(companyId);
        List<JobDto> jobDtos = new ArrayList<>();

        if (jobs.isEmpty()) {
            log.info("No jobs available for Company ID: {}", companyId);
            return jobDtos;
        }

        log.info("Found {} jobs", jobs.size());
        return retrieveJobDtos(jobs);
    }

    @Override
    public JobDto getJobById(Long id) {
        log.info("Executing getJobById()");
        JobDto jobDTO = null;
        Optional<Job> job = jobRepo.findById(id);
        if (job.isEmpty()) {
            return jobDTO;
        }
        String companyUrl = "/api/companies/" + job.get().getCompanyId();
        jobDTO = dtoMapper.mapToDtoWithUrl(job.get(), companyUrl);

        log.info("Job with ID: {} fetched successfully.", id);
        return jobDTO;
    }

    @Transactional
    @Override
    public boolean deleteJobById(Long id) {
        log.info("Executing deleteJobById() for Job ID: {}", id);
        if (!jobRepo.existsById(id)) {
            log.warn("Job ID: {} does not exist.", id);
            return false;
        }
        jobRepo.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteJobByCompId(Long companyId) {
        log.info("Executing deleteJobByCompId() with Company ID: {}", companyId);
        int deletedCount = jobRepo.deleteByCompanyId(companyId);
        if (deletedCount == 0) {
            log.warn("No jobs found for company id: {}", companyId);
            return false;
        }
        log.warn("Jobs deleted count: {}", deletedCount);
        return true;
    }

    //fetch jobs by advanced search
    @Override
    public List<JobDto> searchJobs(Map<String, String> searchCriteria) {
        log.info("Executing getJobByAdvSearch");
        String title = searchCriteria.get("title").toLowerCase();
        String location = searchCriteria.get("location").toLowerCase();

        log.info("Executing custom query with search criteria: {}", searchCriteria);
        List<Job> jobs = jobRepo.fetchJobByAdvSearch(title, location);
        List<JobDto> jobDtos = new ArrayList<>();
        if (jobs.isEmpty()) {
            log.info("No jobs available for criteria: {}", searchCriteria);
            return jobDtos;
        }
        log.info("Found {} jobs", jobs.size());
        return retrieveJobDtos(jobs);
    }

    //fetch jobs by Keyword
    @Override
    public List<JobDto> getJobByKeyword(String keyword) {
        log.info("Executing getJobByKeyword() with keyword: {}", keyword);
        String keywordLC = keyword.toLowerCase();
        List<Job> jobs = jobRepo.findJobByKeyword(keywordLC);
        List<JobDto> jobDtos = new ArrayList<>();

        if (jobs.isEmpty()) {
            log.info("No jobs available for keyword: {}", keywordLC);
            return jobDtos;
        }

        log.info("Found {} jobs", jobs.size());
        return retrieveJobDtos(jobs);
    }

    public List<JobDto> retrieveJobDtos(List<Job> jobs) {
        return jobs.stream()
                .map(job -> {
                    String companyUrl = "/api/companies/" + job.getCompanyId();
                    return dtoMapper.mapToDtoWithUrl(job, companyUrl);
                })
                .collect(Collectors.toList());
    }

    // Using Reflection to run update query only for required fields not for all
    public Job setNonNullPropertyNames(Job job, Job newJob) {
        try {
            for (Field field : newJob.getClass().getDeclaredFields()) {
                field.setAccessible(true);  // Make the field accessible
                Object value = field.get(newJob);  // Get the value of the field in the newJob object
                if (value != null) {
                    field.set(job, value);  // Set the value of the field in the job
                }
            }
        } catch (IllegalAccessException ex) {
            throw new ApplicationException(ex.getMessage());
        }
        return job;
    }
}