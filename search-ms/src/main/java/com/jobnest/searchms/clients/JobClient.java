package com.jobnest.searchms.clients;

import com.jobnest.searchms.dto.JobDto;
import com.jobnest.searchms.helper.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@FeignClient(name = "JOB-SERVICE")
public interface JobClient {

    @GetMapping("/api/jobs/advSearch")
//    public List<JobDto> searchJobs(@RequestParam Map<String, String> searchCriteria);
        public ResponseEntity<ApiResponse<List<JobDto>>> searchJobs(@RequestParam Map<String, String> searchCriteria);

    @GetMapping("/api/jobs/search")
    public ResponseEntity<ApiResponse<List<JobDto>>> searchJobByKeyword(@Valid @RequestParam("keyword") String keyword);
}