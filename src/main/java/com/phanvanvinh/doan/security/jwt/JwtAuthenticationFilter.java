package com.phanvanvinh.doan.security.jwt; // Nhớ sửa package cho đúng project của vợ

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
            // 1. Lấy JWT từ Header request
            String jwt = parseJwt(request);

            // 2. Kiểm tra JWT có hợp lệ không
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                // 3. Lấy username (email) từ chuỗi JWT
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 4. Lấy thông tin người dùng từ Database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Set thông tin người dùng vào SecurityContext (để Controller dùng được)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
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