package com.phanvanvinh.doan.security.jwt;

import com.phanvanvinh.doan.service.impl.UserDetailsServiceImpl;
import com.phanvanvinh.doan.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String email = jwtUtils.getUserNameFromJwtToken(jwt);

                // --- ĐOẠN NÀY LÀ QUAN TRỌNG NHẤT ---
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception e) {
                    // NẾU KHÔNG TÌM THẤY USER TRONG DB -> TRẢ VỀ LỖI 401 NGAY LẬP TỨC

                    // --- ĐÃ SỬA: Dùng dấu + thay vì dấu , ---
                    logger.error("User not found in Database: " + e.getMessage());

                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                    response.getWriter().write("{\"error\": \"UserNotFound\", \"message\": \"User no longer exists\"}");
                    return; // Dừng luôn, không cho đi tiếp nữa
                }
                // ------------------------------------
            }
        } catch (Exception e) {
            // --- ĐÃ SỬA: Dùng dấu + thay vì dấu , ---
            logger.error("Cannot set user authentication: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    // Hàm phụ: Lấy chuỗi token từ header "Authorization: Bearer ..."
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Cắt bỏ chữ "Bearer "
        }

        return null;
    }
}