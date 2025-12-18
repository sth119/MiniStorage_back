package com.ministorage.api.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ministorage.api.dto.TokenDto;
import com.ministorage.api.dto.UserLoginDto;
import com.ministorage.api.dto.UserResponse;
import com.ministorage.api.entity.User;
import com.ministorage.api.security.JwtProvider;
import com.ministorage.api.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

	private final UserService userService;
	private final JwtProvider jwtProvider;
	
	@PostMapping("/signup")
	public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserLoginDto dto) {
		User user = userService.signup(dto);
		
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setUsername(user.getUsername());
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/login")
	public ResponseEntity<TokenDto> login(@Valid @RequestBody UserLoginDto dto) {
		User user = userService.login(dto);
		
		String accessToken = jwtProvider.generateAccessToken(user.getUsername(), user);
		String refreshToken = jwtProvider.generateRefreshToken(user.getUsername());
		
//		jwtProvider.verifyRefreshToken(refreshToken);
		
		UserResponse userResponse = new UserResponse();
		userResponse.setId(user.getId());
		userResponse.setUsername(user.getUsername());
		
		TokenDto tokenDto = new TokenDto(accessToken, refreshToken, userResponse);
		
		return ResponseEntity.ok(tokenDto);
	}
	
	// 로그아웃 - Refresh Token 삭제 (블랙리스트 처리)
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Map<String, String> request) {
    	String refreshToken = request.get("refreshToken");
        
    	if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body("Refresh Token이 필요합니다.");
        }

    	try {
            jwtProvider.verifyRefreshToken(refreshToken);
            return ResponseEntity.ok("로그아웃 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("유효하지 않은 Refresh Token입니다.");
        }

    }
}
