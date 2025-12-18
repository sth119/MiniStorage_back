package com.ministorage.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserLoginDto {

//	@NotBlank
//	private Long id;
	
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	private String email;
	
	
}
