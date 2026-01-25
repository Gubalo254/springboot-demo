package com.first99.demo99.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
@Data
public class CreateJobApplicationRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Position is required")
    private String position;

    @NotNull(message = "Applied date is required")
    @PastOrPresent(message = "Applied date cannot be in the future")
    private LocalDate appliedDate;
}

