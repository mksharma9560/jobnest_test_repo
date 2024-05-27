package com.jobnest.searchms.clients;

import com.jobnest.searchms.dto.CompanyDto;
import com.jobnest.searchms.helper.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@FeignClient(name = "COMPANY-SERVICE")
public interface CompanyClient {

    @GetMapping("/api/companies/advSearch")
    ResponseEntity<ApiResponse<List<CompanyDto>>> searchCompanies(@RequestParam Map<String, String> searchCriteria);

    @GetMapping("/api/companies/search")
    ResponseEntity<ApiResponse<List<CompanyDto>>> searchJobsByKeyword(@Valid @RequestParam("keyword") String keyword);
}