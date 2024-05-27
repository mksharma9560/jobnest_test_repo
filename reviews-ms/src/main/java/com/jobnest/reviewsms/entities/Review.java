package com.jobnest.reviewsms.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "review_tb")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Description is blank")
    @Size(max = 255, message = "Description must be less than or equal to 255 characters")
    private String description;

    @NotNull(message = "Rating is null")
    private double rating;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private String postedAt;

    @NotNull(message = "Company id is null")
    private Long companyId;

//    Add Later: user_id
}