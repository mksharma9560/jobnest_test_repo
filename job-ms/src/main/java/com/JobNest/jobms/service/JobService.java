package com.JobNest.jobms.service;

import com.JobNest.jobms.dto.JobDto;
import com.JobNest.jobms.entities.Job;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface JobService {

    JobDto createJob(Job job);

    List<JobDto> getJobs();

    boolean updateJobById(Long id, Job newJob);

    List<JobDto> findJobsByCompId(Long companyId);

    JobDto getJobById(Long id);

    boolean deleteJobById(Long id);

    boolean deleteJobByCompId(Long companyId);

    // Custom query methods
    List<JobDto> getJobByKeyword(String keyword);

    List<JobDto> searchJobs(Map<String, String> searchCriteria);
}