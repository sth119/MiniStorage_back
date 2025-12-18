//package com.ministorage.api.security;
//
//import java.util.UUID;
//
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Component;
//import org.zerock.myapp.entity.Employee;
//
//@Component
//public class SocialLoginMapper {
//
//	public Employee mapOAuth2UserToEmployee(OAuth2User oAuth2User) {
//		String email = oAuth2User.getAttribute("email");
//		String name = oAuth2User.getAttribute("name");
//		
//		
//        Employee emp = new Employee();
//        emp.setEmail(email);
//        emp.setName(name);
//        emp.setLoginId(email); // Google 계정 ID를 loginId로 사용
//        emp.setEmpno("TEMP-" + UUID.randomUUID()); // 소셜 사용자는 고유 임시 번호
//        emp.setPosition(0); // 기본 권한 설정
//
//        // 부서가 필수라면 기본 부서 ID도 설정
//        // emp.setDepartment(defaultDept); ← 필요 시
//
//        return emp;
//		
//	}
//	
//} // end class
