package com.ministorage.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDto {

	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	private String email;
	
}
