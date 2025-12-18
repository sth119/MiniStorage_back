//package com.ministorage.api.security;
//
//import java.io.IOException;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import org.zerock.myapp.entity.Employee;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
//
//    private final JwtProvider jwtProvider;
//    private final SocialLoginMapper socialLoginMapper;
//
//    public OAuth2SuccessHandler(JwtProvider jwtProvider, SocialLoginMapper socialLoginMapper) {
//        this.jwtProvider = jwtProvider;
//        this.socialLoginMapper = socialLoginMapper;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//
//        // ✅ 소셜 사용자 → Employee 변환
//        Employee employee = socialLoginMapper.mapOAuth2UserToEmployee(oAuth2User);
//
//        // ✅ JWT 발급
//        String token = jwtProvider.generateToken(employee.getEmail(), employee);
//        String refreshToken = jwtProvider.generateRefreshToken(employee.getEmail());
//
//        // ✅ 프론트엔드로 리디렉션 + 토큰 전달
//        response.sendRedirect("http://localhost:3000/oauth2/success?token=" + token + "&refresh=" + refreshToken);
//    }
//}
