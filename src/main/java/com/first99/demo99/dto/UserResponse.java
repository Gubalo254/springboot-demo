package com.first99.demo99.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {


    private String email;
    private String username;
    private String message;
}
