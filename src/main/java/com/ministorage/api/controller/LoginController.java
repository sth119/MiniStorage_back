package com.ministorage.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ministorage.api.dto.UserLoginDto;
import com.ministorage.api.entity.User;
import com.ministorage.api.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

	private final UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<User> login(@Valid @RequestBody UserLoginDto dto) {
		User user = userService.login(dto);
		return ResponseEntity.ok(user);
	}
}
