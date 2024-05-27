package com.JobNest.jobms.helper;

import org.springframework.stereotype.Component;


public class URLConstants {
    public static final String GET_COMPANIES_URL = "http://COMPANY-SERVICE:8082/api/companies";
    public static final String GET_REVIEW_BY_COMP_ID_URL = "http://REVIEWS-SERVICE:8083/api/reviews/company?companyId=";
}