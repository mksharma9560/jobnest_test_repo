package com.JobNest.jobms.repository;

import com.JobNest.jobms.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    // custom jpa methods
    List<Job> findByCompanyId(Long companyId);

    // custom named-query methods
    @Query(name = "Job.findJobByAdvSearch")
    List<Job> fetchJobByAdvSearch(@Param("title") String title, @Param("location") String location);

    @Query(name = "Job.findByKeyword")
    List<Job> findJobByKeyword(@Param("keyword") String keyword);

    @Modifying
    @Query("DELETE FROM Job j WHERE j.companyId = :companyId")
    int deleteByCompanyId(@Param("companyId") Long companyId);
}