package com.JobNest.jobms.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NamedQueries(value = {
        @NamedQuery(
                name = "Job.findByKeyword",
                query = "SELECT j FROM Job j " +
                        "WHERE j.title LIKE :keyword " +
                        "OR j.description LIKE :keyword " +
                        "OR j.location LIKE :keyword"),
        @NamedQuery(
                name = "Job.findJobByAdvSearch",
                query = "SELECT j FROM Job j " +
                        "WHERE j.title=:title " +
                        "AND j.location=:location")
})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "job_tb")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The job title cannot be blank")
    @Size(max = 50, message = "Title must be less than or equal to 50 characters")
    private String title;

    @Size(max = 255, message = "Description must be less than or equal to 255 characters")
    private String description;

    @NotBlank(message = "The job location cannot be blank")
    @Size(max = 50, message = "Location must be less than or equal to 50 characters")
    private String location;

    @NotNull(message = "Atleast one skill is required")
    private String skills;

    @NotNull(message = "Company Id is blank")
    private Long companyId;

    @JsonFormat(pattern = "MM-dd-yyyy")
    private String postedAt;

    // add feild applyJob Url
}