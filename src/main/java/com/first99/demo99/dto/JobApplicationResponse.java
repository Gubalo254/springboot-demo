package com.first99.demo99.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobApplicationResponse {
    private String message;
    private String companyName;

}
