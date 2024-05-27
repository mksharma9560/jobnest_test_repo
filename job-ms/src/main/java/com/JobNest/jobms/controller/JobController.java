package com.JobNest.jobms.controller;

import com.JobNest.jobms.dto.JobDto;
import com.JobNest.jobms.entities.Job;
import com.JobNest.jobms.helper.ApiResponse;
import com.JobNest.jobms.helper.ResponseBuilder;
import com.JobNest.jobms.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private static final Logger log = LoggerFactory.getLogger(JobController.class);
    private final JobService jobService;
    private final ResponseBuilder responseBuilder;

    private boolean success;
    private String message;
    private HttpStatus httpStatus;

    // Constructor
    public JobController(JobService jobService, ResponseBuilder responseBuilder) {
        this.jobService = jobService;
        this.responseBuilder = responseBuilder;
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<JobDto>> createJob(@Valid @RequestBody Job job) {
        log.info("Received POST request to create a job");
        JobDto jobDto = jobService.createJob(job);

        success = jobDto != null;
        message = "Job saved successfully";
        httpStatus = HttpStatus.CREATED;

        ApiResponse<JobDto> response = responseBuilder.buildResponseWithSingleData(jobDto, success, message, httpStatus);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<ApiResponse<String>> updateJobById(@Valid @PathVariable Long jobId, @RequestBody Job newJob) {
        log.info("Received PUT request to update job with ID: {}", jobId);
        success = jobService.updateJobById(jobId, newJob);

        message = success ? "Job Updated" : "Job with ID: " + jobId + " not found";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<String> response = responseBuilder.buildResponseWithoutData(success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobDto>>> getAllJobs() {
        log.info("GET request received to fetch all job");
        List<JobDto> jobDtos = jobService.getJobs();

        success = !jobDtos.isEmpty();
        message = success ? "Jobs fetched successfully" : "Jobs data not available";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<List<JobDto>> response = responseBuilder.buildResponseWithData(jobDtos, success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<JobDto>>> getJobsByCompId(@Valid @PathVariable Long companyId) {
        log.info("GET request received to fetch all job");
        List<JobDto> jobDtos = jobService.findJobsByCompId(companyId);
        log.info("Returning {} jobs.", jobDtos.size());

        success = !jobDtos.isEmpty();
        message = success ? "Jobs fetched successfully" : "No jobs found for company ID: " + companyId;
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<List<JobDto>> response = responseBuilder.buildResponseWithData(jobDtos, success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getJobById(@Valid @PathVariable Long id) {
        log.info("*** Received GET request to fetch job with ID: {}", id);
        JobDto jobDto = jobService.getJobById(id);

        success = jobDto != null;
        message = success ? "Job fetched successfully" : "Job with ID: " + id + " not found";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<JobDto> response = responseBuilder.buildResponseWithSingleData(jobDto, success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse<String>> deleteByIdJob(@Valid @PathVariable Long jobId) {
        log.info("Received DELETE request for Job ID: {}", jobId);
        success = jobService.deleteJobById(jobId);

        message = success ? "Job deleted successfully." : "Job ID: " + jobId + " not found.";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<String> response = responseBuilder.buildResponseWithoutData(success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @DeleteMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<String>> deleteJobByCompId(@Valid @PathVariable Long companyId) {
        log.info("Received DELETE request for Job with company ID: {}", companyId);
        success = jobService.deleteJobByCompId(companyId);

        message = success ? "Jobs deleted successfully" : "Jobs with company ID: " + companyId + " not found.";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<String> response = responseBuilder.buildResponseWithoutData(success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/advSearch")
    public ResponseEntity<ApiResponse<List<JobDto>>> searchJobs(@Valid @RequestParam Map<String, String> searchCriteria) {
        log.info("Received GET request to search job based on criteria");
        List<JobDto> jobDtos = jobService.searchJobs(searchCriteria);

        success = !jobDtos.isEmpty();
        message = success ? "Jobs fetched successfully" : "No jobs match the given criteria";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<List<JobDto>> response = responseBuilder.buildResponseWithData(jobDtos, success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<JobDto>>> searchJobByKeyword(@Valid @RequestParam("keyword") String keyword) {
        log.info("GET request received to search jobs by keyword");
        List<JobDto> jobDtos = jobService.getJobByKeyword(keyword);

        success = !jobDtos.isEmpty();
        message = success ? "Jobs fetched successfully" : "No jobs match the given keyword";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<List<JobDto>> response = responseBuilder.buildResponseWithData(jobDtos, success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }
}