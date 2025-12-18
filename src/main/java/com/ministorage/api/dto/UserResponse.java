package com.ministorage.api.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    // password 등은 절대 반환하지 말 것
}