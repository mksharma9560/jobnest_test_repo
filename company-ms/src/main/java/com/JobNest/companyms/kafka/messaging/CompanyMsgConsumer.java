package com.JobNest.companyms.kafka.messaging;

import com.JobNest.companyms.external.clients.ReviewClient;
import com.JobNest.companyms.entities.Company;
import com.JobNest.companyms.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CompanyMsgConsumer {
    private static final Logger log = LoggerFactory.getLogger(CompanyMsgConsumer.class);
    private CompanyRepository companyRepo;
    private ReviewClient reviewClient;

    // constructor
    public CompanyMsgConsumer(ReviewClient reviewClient, CompanyRepository companyRepo) {
        this.reviewClient = reviewClient;
        this.companyRepo = companyRepo;
    }

    @KafkaListener(topics = "review-added-topic", groupId = "company-service")
    public void consumeMessage(Long companyId) {
        log.info("*** Message received ***");

        updateAverageRating(companyId);

        log.info("*** Message consumed ***");
    }

    private void updateAverageRating(Long companyId) {
        Company company = companyRepo.findById(companyId).orElse(null);
        if (company != null) {
            double avgCompanyRating = calculateAverageRating(companyId);
            company.setAverageRating(avgCompanyRating);
            companyRepo.save(company);
            log.info("Average rating updated to {} for company Id {}: ", avgCompanyRating, companyId);
        } else {
            log.error("Company not found with ID {}", companyId);
        }
    }

    private double calculateAverageRating(Long companyId) {
        Double avgCompanyRating = reviewClient.getAvgCompanyRating(companyId);
        return avgCompanyRating;
    }
}