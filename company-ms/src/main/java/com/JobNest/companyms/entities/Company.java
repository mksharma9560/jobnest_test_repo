package com.JobNest.companyms.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NamedQueries(value = {
        @NamedQuery(
                name = "Company.findByKeyword",
                query = "SELECT c FROM Company c " +
                        "WHERE c.name LIKE :keyword " +
                        "OR c.industry LIKE :keyword " +
                        "OR c.location LIKE :keyword"),
        @NamedQuery(
                name = "Company.searchCompany",
                query = "SELECT c FROM Company c " +
                        "WHERE c.name=:name " +
                        "AND c.industry=:industry " +
                        "AND c.location=:location")
})

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "company_tb")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Company name is blank")
    @Size(max = 50, message = "Name must be less than or equal to 50 characters")
    private String name;

    @Size(max = 255, message = "Description must be less than or equal to 255 characters")
    private String description;

    @NotBlank(message = "Company industry is blank")
    @Size(max = 25, message = "Industry must be less than or equal to 25 characters")
    private String industry;

    @NotBlank(message = "Company location is blank")
    @Size(max = 25, message = "Location must be less than or equal to 25 characters")
    private String location;

    @NotNull(message = "Company averageRating is null")
    private Double averageRating;
}