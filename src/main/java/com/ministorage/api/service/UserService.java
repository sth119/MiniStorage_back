package com.ministorage.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ministorage.api.dto.UserLoginDto;
import com.ministorage.api.entity.User;
import com.ministorage.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

	private final UserRepository userRepository;
//	private final PasswordEncoder passwordEncoder;
	
	
	// 로그인 로직
	public User login (UserLoginDto dto) {
		if (userRepository.existsByUsername(dto.getUsername())) {
			throw new IllegalArgumentException("이미 사용 중인 사용자명입니다.");
		}
		if (dto.getEmail() != null && userRepository.existsByEmail(dto.getEmail())) {
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
		}
		
		User user = new User();
		user.setUsername(dto.getUsername());
//		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setEmail(dto.getEmail());
		
		return userRepository.save(user);
	}
	
	// 로그인용 (나중에 Security와 연동)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
    
} // end Service
