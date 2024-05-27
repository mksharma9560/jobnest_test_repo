package com.JobNest.jobms.dto;

import com.JobNest.jobms.entities.Job;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {
    public JobDto mapToDtoWithUrl(Job job, String companyUrl) {

        JobDto jobDTo = new JobDto();

        jobDTo.setId(job.getId());
        jobDTo.setTitle(job.getTitle());
        jobDTo.setDescription(job.getDescription());
        jobDTo.setLocation(job.getLocation());
        jobDTo.setSkills(job.getSkills());
        jobDTo.setPostedAt(job.getPostedAt());

        jobDTo.setCompanyUrl(companyUrl);

        return jobDTo;
    }

    public JobDto mapToDto(Job job) {

        JobDto jobDTo = new JobDto();

        jobDTo.setId(job.getId());
        jobDTo.setTitle(job.getTitle());
        jobDTo.setDescription(job.getDescription());
        jobDTo.setLocation(job.getLocation());
        jobDTo.setSkills(job.getSkills());
        jobDTo.setPostedAt(job.getPostedAt());

        return jobDTo;
    }
}