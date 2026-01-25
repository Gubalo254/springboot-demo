package com.first99.demo99.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationsResponse {
    private Long id;
    private String companyName;
    private String position;
    private String status;
    private String appliedDate; // or LocalDateTime
}

