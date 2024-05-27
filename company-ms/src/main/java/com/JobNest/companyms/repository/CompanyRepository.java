package com.JobNest.companyms.repository;

import com.JobNest.companyms.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query(name = "Company.searchCompany")
    List<Company> fetchCompany(
            @Param("name") String name,
            @Param("industry") String industry,
            @Param("location") String location
    );

    @Query(name = "Company.findByKeyword")
    List<Company> fetchCompaniesByKeyword(@Param("keyword") String keyword);
}