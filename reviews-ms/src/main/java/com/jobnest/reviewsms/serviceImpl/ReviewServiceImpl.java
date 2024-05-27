package com.jobnest.reviewsms.serviceImpl;

import com.jobnest.reviewsms.dto.DtoMapper;
import com.jobnest.reviewsms.dto.ReviewDto;
import com.jobnest.reviewsms.entities.Review;
import com.jobnest.reviewsms.messaging.ReviewMsgProducer;
import com.jobnest.reviewsms.repository.ReviewRepository;
import com.jobnest.reviewsms.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewRepository reviewRepo;
    private final ReviewMsgProducer msgProducer;
    private final DtoMapper dtoMapper;

    // Constructor Injection
    public ReviewServiceImpl(
            ReviewRepository reviewRepo,
            ReviewMsgProducer msgProducer,
            DtoMapper dtoMapper) {
        this.reviewRepo = reviewRepo;
        this.msgProducer = msgProducer;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional
    public ReviewDto addReview(Review review, Long companyId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = dateFormat.format(new Date());

        review.setPostedAt(date);
        review.setCompanyId(companyId);
        Review savedReview = reviewRepo.save(review);

        Long compId = savedReview.getCompanyId();
        msgProducer.publishMessage(savedReview.getCompanyId());

        log.info(" Review has been saved with ID: {}", savedReview.getId());
        return dtoMapper.mapToDto(savedReview);
    }

    @Override
    public List<ReviewDto> getReviewsByCompId(Long companyId) {
        List<Review> reviews = reviewRepo.findByCompanyId(companyId);
        List<ReviewDto> reviewDtos = new ArrayList<>();
        if (reviews.isEmpty()) {
            log.warn("No reviews available for company ID: {}", companyId);
            return reviewDtos; // empty list
        }
        for (Review review : reviews) {
            ReviewDto reviewDto = dtoMapper.mapToDto(review);
            reviewDtos.add(reviewDto);
        }
        return reviewDtos;
    }

    @Override
    @Transactional
    public boolean deleteReviewByCompId(Long companyId) {
        log.info("Executing deleteReviewByCompId() with Company ID: {}", companyId);
        int deleteCount = reviewRepo.deleteByCompanyId(companyId);
        if (deleteCount == 0) {
            log.warn("No reviews found for company id: {}", companyId);
            return false;
        }
        log.info("Reviews deleted count: {}", deleteCount);
        return true;
    }
}