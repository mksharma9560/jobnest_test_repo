package com.JobNest.companyms.external.clients;

import com.JobNest.companyms.dto.JobDto;
import com.JobNest.companyms.helper.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "JOB-SERVICE")
public interface JobClient {

    @GetMapping("/api/jobs/company/{compId}")
    ResponseEntity<ApiResponse<List<JobDto>>> getJobsByCompId(@PathVariable Long compId);

    @DeleteMapping("/api/jobs/company/{companyId}")
    ResponseEntity<ApiResponse<String>> deleteJobByCompId(@PathVariable Long companyId);
}