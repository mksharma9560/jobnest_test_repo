package com.jobnest.reviewsms.repository;

import com.jobnest.reviewsms.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCompanyId(Long companyId);

    @Modifying
    @Query("DELETE FROM Review r WHERE r.companyId = :companyId")
    int deleteByCompanyId(Long companyId);
}