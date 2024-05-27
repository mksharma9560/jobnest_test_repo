package com.JobNest.companyms.controllers;

import com.JobNest.companyms.dto.CompanyDto;
import com.JobNest.companyms.entities.Company;
import com.JobNest.companyms.helper.ApiResponse;
import com.JobNest.companyms.helper.ResponseBuilder;
import com.JobNest.companyms.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);
    private final CompanyService companyService;
    private final ResponseBuilder responseBuilder;

    private boolean success;
    private String message;
    private HttpStatus httpStatus;

    // Constructor
    @Autowired
    public CompanyController(CompanyService companyService, ResponseBuilder responseBuilder) {
        this.companyService = companyService;
        this.responseBuilder = responseBuilder;
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<CompanyDto>> createCompany(@Valid @RequestBody Company company) {
        log.info("POST request received to create a company");
        CompanyDto companyDto = companyService.createCompany(company);

        success = companyDto != null;
        message = "Company created successfully";
        httpStatus = HttpStatus.CREATED;

        ApiResponse<CompanyDto> response = responseBuilder.buildResponseWithSingleData(companyDto, success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @PutMapping("/{compId}")
    public ResponseEntity<ApiResponse<String>> updateCompany(@PathVariable Long compId, @RequestBody Company newCompany) {
        log.info("PUT request received to update company with ID: {}", compId);
        success = companyService.updateCompany(compId, newCompany);
        message = success ? "Company updated successfully" : "Company ID: " + compId + " not found.";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<String> response = responseBuilder.buildResponseWithoutData(success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CompanyDto>>> getAllCompanies() {
        log.info("*** Received GET request to fetch all companies ***");
        List<CompanyDto> companyDtos = companyService.getCompanies();

        success = !companyDtos.isEmpty();
        message = success ? "Companies fetched successfully" : "No companies data available";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<List<CompanyDto>> response = responseBuilder.buildResponseWithData(companyDtos, success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<ApiResponse<CompanyDto>> getCompanyById(@PathVariable Long compId) {
        log.info("GET request received to fetch company by ID: {}", compId);
        CompanyDto companyDto = companyService.getCompanyById(compId);

        success = companyDto != null;
        message = success ? "Company fetched successfully" : "Company ID: " + compId + " not found";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<CompanyDto> response = responseBuilder.buildResponseWithSingleData(companyDto, success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<ApiResponse<String>> deleteCompanyById(@PathVariable Long companyId) {
        success = companyService.deleteCompanyById(companyId);
        message = success ? "Company deleted successfully" : "Company ID: " + companyId + " not found";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<String> response = responseBuilder.buildResponseWithoutData(success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/advSearch")
    public ResponseEntity<ApiResponse<List<CompanyDto>>> searchCompanies(@RequestParam Map<String, String> searchCriteria) {
        log.info("*** Received GET request to search companies ***");
        List<CompanyDto> companyDtos = companyService.searchCompany(searchCriteria);
        log.info("Returning {} companies.", companyDtos.size());

        success = !companyDtos.isEmpty();
        message = success ? "Companies fetched successfully" : "No companies match the given criteria";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<List<CompanyDto>> response = responseBuilder.buildResponseWithData(companyDtos, success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CompanyDto>>> searchJobsByKeyword(@Valid @RequestParam("keyword") String keyword) {
        log.info("*** Received GET request to search companies ***");
        List<CompanyDto> companyDtos = companyService.getCompanyByKeyword(keyword);
        log.info("Returning {} companies.", companyDtos.size());

        success = !companyDtos.isEmpty();
        message = success ? "Companies fetched successfully" : "No companies match the given keyword";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<List<CompanyDto>> response = responseBuilder.buildResponseWithData(companyDtos, success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }
}