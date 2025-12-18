package com.ministorage.api.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


	private final JwtProvider jwtProvider;
	private final CustomUserDetailsService userDetailsService;



@Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)
                                throws ServletException, IOException {

    // 1. Authorization 헤더에서 Bearer 토큰 꺼내기
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
    }
    String token = authHeader.substring(7);

 // 2. 토큰 검증 및 username 추출 (JwtProvider 위임)
    String username = jwtProvider.verifyToken(token);
    if (username == null) {
        filterChain.doFilter(request, response);
        return;
    }

    // 3. UserDetails 로드 (DB에서 유저 정보 가져옴)
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    // 4. Authentication 생성 및 SecurityContext에 설정
    UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
    
    
    
    
    
}



//    try {
//        // 2. JWT 검증 + 디코딩 (issuer, signature 검사)
//        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256("mySuperSecretKey12345"))
//                                   .withIssuer("Mark")
//                                   .build()
//                                   .verify(token);
//
//        // 3. Subject(=username) 및 기타 클레임 추출 ( 원하는 정보 )
//        String username   = decodedJWT.getSubject(); // 사용자를 식별할 수 있는 고유 Id
//        String name       = decodedJWT.getClaim("name").asString();
//        
//
//        // 5. Principal 생성 및 SecurityContext에 Authentication 주입
//        JwtPrincipal principal = new JwtPrincipal(
//        		empno,roles.get(0),name);
//
//        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
//        
//        // 실질적으로 주입 되는 곳
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//        System.out.println("JWT 필터 통과: " + username + ", 권한: " + roles);
//
//    } catch (Exception e) {
//        System.out.println("JWT 검증 실패: " + e.getMessage());
//    }
//
//    filterChain.doFilter(request, response);

} // end class