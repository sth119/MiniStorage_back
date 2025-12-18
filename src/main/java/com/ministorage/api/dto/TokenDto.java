package com.ministorage.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {

	private String accessToken;
	private String refreshToken;
	private UserResponse user;
}
