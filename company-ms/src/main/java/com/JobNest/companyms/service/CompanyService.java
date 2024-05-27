package com.JobNest.companyms.service;

import com.JobNest.companyms.dto.CompanyDto;
import com.JobNest.companyms.entities.Company;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CompanyService {

    List<CompanyDto> getCompanies();

    CompanyDto getCompanyById(Long id);

    CompanyDto createCompany(Company company);

    boolean updateCompany(Long id, Company companyData);

    boolean deleteCompanyById(Long id);

    List<CompanyDto> searchCompany(Map<String, String> searchCriteria);

    List<CompanyDto> getCompanyByKeyword(String keyword);
}